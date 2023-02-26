package com.gdsc.bakku.auth.handler;

import com.gdsc.bakku.auth.util.ResponseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final FirebaseAuth firebaseAuth;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String value = request.getHeader("Authorization");

        if (Strings.isBlank(value)) {
            ResponseUtil.setResponse(response, 401, "TOKEN_IS_NULL");
        } else if (!value.contains("Bearer ")) {
            ResponseUtil.setResponse(response, 401, "INVALID_TOKEN(must start with 'Bearer ')");
        } else {
            String token = value.substring(7);
            try {
                firebaseAuth.verifyIdToken(token);
            } catch (FirebaseAuthException e) {
                ResponseUtil.setResponse(response, 401, String.valueOf(e.getAuthErrorCode()));
            }
        }
    }
}
