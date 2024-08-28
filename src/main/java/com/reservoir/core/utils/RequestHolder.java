package com.reservoir.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Objects;

@Slf4j
public class RequestHolder {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取HttpServletRequest请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取请求IP
     *
     * @return String IP
     */
    public static String getHttpServletRequestIpAddress() {
        HttpServletRequest request = getHttpServletRequest();
        return getHttpServletRequestIpAddress(request);
    }

    public static String getHttpServletRequestIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取请求URL
     *
     * @return String URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getRequestURL().toString();
    }

    /**
     * 获取请求方法
     *
     * @return String 请求方法
     */
    public static String getRequestMethod() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getMethod();
    }

    /**
     * 获取请求头信息
     *
     * @param headerName 请求头名称
     * @return String 请求头值
     */
    public static String getRequestHeader(String headerName) {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(headerName);
    }

    /**
     * 获取所有请求头信息
     *
     * @return String 所有请求头信息
     */
    public static String getAllRequestHeaders() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuilder headers = new StringBuilder();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        }
        return headers.toString();
    }

    /**
     * 获取请求参数
     *
     * @param paramName 参数名称
     * @return String 参数值
     */
    public static String getRequestParameter(String paramName) {
        HttpServletRequest request = getHttpServletRequest();
        return request.getParameter(paramName);
    }

    /**
     * 获取所有请求参数
     *
     * @return String 所有请求参数
     */
    public static String getAllRequestParameters() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuilder parameters = new StringBuilder();
        for (String paramName : request.getParameterMap().keySet()) {
            parameters.append(paramName).append(": ").append(request.getParameter(paramName)).append("\n");
        }
        return parameters.toString();
    }
}