package com.manji.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class ResponseUtils {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static void buildResponse(HttpServletResponse response, ResponseEntity<?> result, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(httpStatus.value());  // 状态码
        jsonMapper.writeValue(response.getOutputStream(), result);
    }
}
