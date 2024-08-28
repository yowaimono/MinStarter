package com.reservoir.core.entity;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Result<T> {
    private int code;
    private String message;
    private T data;
    private String timestamp;

    public Result(int code, String message, T data, String timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, getCurrentTimestamp());
    }

    public static <T> Result<T> success(Integer code,String message, T data) {
        return new Result<>(code, message, data, getCurrentTimestamp());
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, getCurrentTimestamp());
    }

    public static <T> Result<T> of(ResultCode resultCode, T data) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), data, getCurrentTimestamp());
    }

    public static <T> Result<T> of(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null, getCurrentTimestamp());
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, getCurrentTimestamp());
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data, getCurrentTimestamp());
    }


    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}