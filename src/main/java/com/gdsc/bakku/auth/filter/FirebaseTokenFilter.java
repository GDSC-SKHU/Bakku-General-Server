package com.gdsc.bakku.auth.filter;

import com.gdsc.bakku.auth.domain.entity.Role;
import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.auth.service.UserService;
import com.gdsc.bakku.auth.util.ResponseUtil;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {
    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        FirebaseToken decodedToken;

        String header = request.getHeader("Authorization");
        if (Strings.isBlank(header) || !header.startsWith("Bearer ")) {
            ResponseUtil.setUnauthorizedResponse(response, "INVALID_HEADER(require 'Bearer ')");
            return;
        }

        String token = header.substring(7);
        try {
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            ResponseUtil.setUnauthorizedResponse(response, "INVALID_TOKEN");
            return;
        }

        User user;

        try {
            user = userService.loadUserByUsername(decodedToken.getUid());
        } catch (UsernameNotFoundException e) {
            user = userService.create(decodedToken, Role.ROLE_USER);
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
