package com.reservoir.core.utils;



import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpContextUtils {

    /**
     * 获取当前请求的 HttpServletRequest 对象
     *
     * @return HttpServletRequest 对象，如果当前没有请求则返回 null
     */
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取请求参数的 Map
     *
     * @param request HttpServletRequest 对象
     * @return 包含请求参数的 Map，键为参数名，值为参数值
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            String value = request.getParameter(parameter);
            if (StringUtils.isNotBlank(value)) {
                params.put(parameter, value);
            }
        }
        return params;
    }

    /**
     * 获取请求的域名
     *
     * @return 请求的域名
     */
    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取请求的 Origin 头
     *
     * @return Origin 头的值
     */
    public static String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(HttpHeaders.ORIGIN);
    }

    /**
     * 获取请求的所有头信息
     *
     * @return 包含所有请求头的 Map，键为头名，值为头值
     */
    public static Map<String, String> getHeaders() {
        HttpServletRequest request = getHttpServletRequest();
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * 获取请求的方法（GET、POST 等）
     *
     * @return 请求的方法
     */
    public static String getMethod() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getMethod();
    }

    /**
     * 获取请求的路径（不包括域名和查询参数）
     *
     * @return 请求的路径
     */
    public static String getRequestPath() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getRequestURI();
    }

    /**
     * 获取请求的完整 URL（包括查询参数）
     *
     * @return 请求的完整 URL
     */
    public static String getFullRequestUrl() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }
}