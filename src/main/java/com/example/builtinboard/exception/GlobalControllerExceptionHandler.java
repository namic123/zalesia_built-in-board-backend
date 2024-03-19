package com.example.builtinboard.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// Controller 전역 예외처리
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(EntityNotFoundException exception, WebRequest request){
        log.error("RuntimeException 발생 : {} " ,exception);
        log.error("요청 내용 : {} " ,request);
        return ResponseEntity.internalServerError().body("서버 내부에 오류가 발생했습니다.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request){
        log.error("EntityNotFoundException 발생 : {}" ,exception);
        log.error("요청 내용 : {} " ,request);
        return ResponseEntity.notFound().build();
    }

}
