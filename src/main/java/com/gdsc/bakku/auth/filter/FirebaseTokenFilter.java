package com.gdsc.bakku.auth.filter;

import com.gdsc.bakku.auth.domain.entity.Role;
import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.auth.service.UserService;
import com.gdsc.bakku.common.exception.UserNotFoundException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {
    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String value = request.getHeader("Authorization");

        if (!Strings.isBlank(value) && value.contains("Bearer ")) {
            String token = value.substring(7);

            try {
                FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);

                User user;
                try {
                    user = userService.updateByUsername(firebaseToken);
                } catch (UserNotFoundException e) {
                    user = userService.create(firebaseToken, Role.ROLE_USER);
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (FirebaseAuthException ignore) {}
        }

        filterChain.doFilter(request, response);
    }
}
