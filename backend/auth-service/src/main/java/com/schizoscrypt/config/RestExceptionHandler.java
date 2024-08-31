package com.schizoscrypt.config;

import com.schizoscrypt.dtos.ErrorDto;
import com.schizoscrypt.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(AppException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorDto.builder().errorMessage(e.getMessage()).build());
    }
}
