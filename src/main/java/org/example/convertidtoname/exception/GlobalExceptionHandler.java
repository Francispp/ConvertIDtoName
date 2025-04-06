package org.example.convertidtoname.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidData(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<String> handleMultipartException(org.springframework.web.multipart.MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Invalid file upload\"}");
    }
}
