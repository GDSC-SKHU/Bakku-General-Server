package com.gdsc.bakku.auth.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseUtil {
    public static void setResponse(HttpServletResponse response, HttpStatus httpStatus, String detail) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"code\":\"%s\",\"detail\":\"%s\"}", httpStatus.name(), detail));
    }
}
