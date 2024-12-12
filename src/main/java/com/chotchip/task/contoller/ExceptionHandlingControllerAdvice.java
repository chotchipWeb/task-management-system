package com.chotchip.task.contoller;

import com.chotchip.task.dto.response.ExceptionResponseDTO;
import com.chotchip.task.execption.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerException(BaseException e) {

        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDTO(List.of(e.getMessage())));
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<ProblemDetail> handlerException(BindException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("errors", e.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

}
