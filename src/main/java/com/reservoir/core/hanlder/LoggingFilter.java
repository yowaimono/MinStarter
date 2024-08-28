package com.reservoir.core.hanlder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Value("${min.config.http.debug:false}")
    private boolean httpDebug;

    private boolean isJson(String string) {
        try {
            new ObjectMapper().readTree(string);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private String prettyPrintJson(String json) {
        try {
            if (json.trim().startsWith("{")) {
                Object obj = new ObjectMapper().readValue(json, Object.class);
                return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return json;
        } catch (Exception e) {
            log.error("Error formatting JSON", e);
            return json;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!httpDebug) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n-----------------------------------------\n");
        requestLog.append("Request URL: ").append(request.getRequestURL()).append("\n");
        requestLog.append("Request Method: ").append(request.getMethod()).append("\n");
        requestLog.append("-----------------------------------------\n\n");

// 打印查询参数
        requestLog.append("Query Parameters:\n");
        requestLog.append("+------------------+----------------------------------+\n");
        requestLog.append("| Parameter        | Value                             |\n");
        requestLog.append("+------------------+----------------------------------+\n");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            requestLog.append(String.format("| %-17s | %-40s |\n", paramName, request.getParameter(paramName)));
        }
        requestLog.append("+------------------+----------------------------------+\n\n");

// 打印请求头
        requestLog.append("Request Headers:\n");
        requestLog.append("+------------------+----------------------------------+\n");
        requestLog.append("| Header           | Value                             |\n");
        requestLog.append("+------------------+----------------------------------+\n");
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            requestLog.append(String.format("| %-17s | %-40s |\n", headerName, request.getHeader(headerName)));
        });
        requestLog.append("+------------------+----------------------------------+\n\n");

// 打印请求体
//        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
//        requestLog.append("Request Body:\n");
//        requestLog.append(requestBody);
//        requestLog.append("\n");

        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        String formattedRequestBody = isJson(requestBody) ? prettyPrintJson(requestBody) : requestBody;
        requestLog.append("Request Body:\n").append(formattedRequestBody).append("\n");

        log.info(requestLog.toString());

        // 缓存响应体内容
        byte[] responseBodyBytes = responseWrapper.getContentAsByteArray();

        // 复制响应体到实际响应
        responseWrapper.copyBodyToResponse();

        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n-----------------------------------------\n");
        responseLog.append("Response Status: ").append(responseWrapper.getStatus()).append(" OK\n");
        responseLog.append("-----------------------------------------\n\n");

// 打印响应头部信息
        responseLog.append("Response Headers:\n");
        responseLog.append("+------------------+----------------------------------+\n");
        responseLog.append("| Header           | Value                            |\n");
        responseLog.append("+------------------+----------------------------------+\n");

        for (String headerName : responseWrapper.getHeaderNames()) {
            responseLog.append(String.format("| %-17s | %-40s |\n", headerName, responseWrapper.getHeader(headerName)));
        }
        responseLog.append("+------------------+----------------------------------+\n\n");

// 打印响应体
//        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
//        responseLog.append("Response Body:\n");
//        responseLog.append(responseBody);
//        responseLog.append("\n");

        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
        String formattedResponseBody = isJson(responseBody) ? prettyPrintJson(responseBody) : responseBody;
        responseLog.append("Response Body:---------------------------------------------------------\n").append(formattedResponseBody).append("\n");

        log.info(responseLog.toString());
    }
}