package com.chotchip.task.contoller;

import com.chotchip.task.dto.response.ExceptionResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerException(RuntimeException e) {

        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerException(ExpiredJwtException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDTO(e.getMessage()));
    }
}
