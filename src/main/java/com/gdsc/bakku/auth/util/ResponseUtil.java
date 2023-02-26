package com.gdsc.bakku.auth.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    public static void setResponse(HttpServletResponse response, int httpStatus, String code) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":\""+code+"\"}");
    }
}
