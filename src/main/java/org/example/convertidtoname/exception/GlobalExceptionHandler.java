package org.example.convertidtoname.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理非法参数异常 (如 CSV 格式错误)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidData(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    // 处理 CSV 文件上传错误
    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<String> handleMultipartException(org.springframework.web.multipart.MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Invalid file upload\"}");
    }
}
