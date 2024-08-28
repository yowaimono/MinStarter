package com.reservoir.core.hanlder;

import com.reservoir.core.entity.Result;
import com.reservoir.core.entity.ResultCode;
import com.reservoir.core.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.crypto.BadPaddingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Result<?>> handleAuthException(AuthException ex) {
        Result<?> result = Result.error(401,ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleException(Exception ex) {
        Result<?> result = Result.error(500,ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Result<?>> handleBindException(BindException ex) {
        return ResponseEntity.badRequest().body(Result.error(400, "验证错误！", ex.getAllErrors()));
    }

    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleServletRequestBindingException(ServletRequestBindingException e) {
        return Result.of(ResultCode.AUTHENTICATION_REQUIRED);
    }

//    @ExceptionHandler(value = AuthException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public Result<?> handleAuthBindingException(AuthException e) {
//        return Result.error(401, e.getMessage());
//    }

    @ExceptionHandler(value = BadPaddingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleBadPaddingException(BadPaddingException e) {
        return Result.of(ResultCode.AUTHENTICATION_REQUIRED);
    }

}