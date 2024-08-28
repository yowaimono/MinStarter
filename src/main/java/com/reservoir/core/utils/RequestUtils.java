package com.reservoir.core.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

public class RequestUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    // GET 方法
    public static <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    public static <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    public static <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    // POST 方法
    public static <T> T post(String url, Object request, Class<T> responseType) {
        return restTemplate.postForObject(url, request, responseType);
    }

    public static <T> T post(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.postForObject(url, request, responseType, uriVariables);
    }

    public static <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(url, request, responseType, uriVariables);
    }

    // PUT 方法
    public static void put(String url, Object request) {
        restTemplate.put(url, request);
    }

    public static void put(String url, Object request, Map<String, ?> uriVariables) {
        restTemplate.put(url, request, uriVariables);
    }

    public static void put(String url, Object request, Object... uriVariables) {
        restTemplate.put(url, request, uriVariables);
    }

    // DELETE 方法
    public static void delete(String url) {
        restTemplate.delete(url);
    }

    public static void delete(String url, Map<String, ?> uriVariables) {
        restTemplate.delete(url, uriVariables);
    }

    public static void delete(String url, Object... uriVariables) {
        restTemplate.delete(url, uriVariables);
    }

    // PATCH 方法
    public static <T> T patch(String url, Object request, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
        return response.getBody();
    }

    public static <T> T patch(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType, uriVariables);
        return response.getBody();
    }

    public static <T> T patch(String url, Object request, Class<T> responseType, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType, uriVariables);
        return response.getBody();
    }

    // HEAD 方法
    public static HttpHeaders head(String url) {
        return restTemplate.headForHeaders(url);
    }

    public static HttpHeaders head(String url, Map<String, ?> uriVariables) {
        return restTemplate.headForHeaders(url, uriVariables);
    }

    public static HttpHeaders head(String url, Object... uriVariables) {
        return restTemplate.headForHeaders(url, uriVariables);
    }

    // OPTIONS 方法
    public static Set<HttpMethod> options(String url) {
        return restTemplate.optionsForAllow(url);
    }

    public static Set<HttpMethod> options(String url, Map<String, ?> uriVariables) {
        return restTemplate.optionsForAllow(url, uriVariables);
    }

    public static Set<HttpMethod> options(String url, Object... uriVariables) {
        return restTemplate.optionsForAllow(url, uriVariables);
    }
}