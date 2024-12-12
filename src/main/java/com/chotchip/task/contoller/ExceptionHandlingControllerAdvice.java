package com.chotchip.task.contoller;

import com.chotchip.task.dto.response.ExceptionResponseDTO;
import com.chotchip.task.execption.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerException(RuntimeException e) {

        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDTO(e.getMessage()));
    }

}
