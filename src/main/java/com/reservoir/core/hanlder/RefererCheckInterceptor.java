package com.reservoir.core.hanlder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class RefererCheckInterceptor implements HandlerInterceptor {

    private static final String ALLOWED_REFERER = "http://localhost:8080";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("Referer");
        if (referer != null && referer.startsWith(ALLOWED_REFERER)) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return false;
        }
    }
}