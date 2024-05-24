package com.manji.base.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalHandle {

    /**
     * NoResourceFoundException: 请求的接口不存在
     * HttpRequestMethodNotSupportedException: http请求方法不支持
     */
    @ExceptionHandler(value = {NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        log.error("请求接口不存在: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 业务处理异常
     */
    @ExceptionHandler(value = {BizException.class})
    public ResponseEntity<?> bizExceptionHandler(BizException e) {
        log.error("业务处理异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 登录授权异常处理类
     */
    @ExceptionHandler(value = {BadCredentialsException.class, InsufficientAuthenticationException.class})
    public ResponseEntity<?> authenticationExceptionHandler(Exception e) {
        log.error("登录授权失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 账号异常处理类
     */
    @ExceptionHandler(value = {DisabledException.class})
    public ResponseEntity<?> loginAccountExceptionHandler(Exception e) {
        log.error("登录账号异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * 保底机制拦截异常
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
