package com.example.builtinboard.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// Controller 전역 예외처리
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public void handleException(EntityNotFoundException exception, WebRequest request){
        log.error("Exception 발생 -> " ,exception);
        log.error("요청 내용 -> " ,request);
    }


    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(EntityNotFoundException exception, WebRequest request){
        log.error("RuntimeException 발생 -> " ,exception);
        log.error("요청 내용 -> " ,request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public void handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request){
        log.error("EntityNotFoundException 발생 -> " ,exception);
        log.error("요청 내용 -> " ,request);
    }

}
