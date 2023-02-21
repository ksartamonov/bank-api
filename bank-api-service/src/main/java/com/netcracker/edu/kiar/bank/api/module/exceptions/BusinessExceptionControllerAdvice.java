package com.netcracker.edu.kiar.bank.api.module.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Map;

@ControllerAdvice
public class BusinessExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private final BusinessExceptionManager exceptionManager;

    public BusinessExceptionControllerAdvice(BusinessExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<BusinessExceptionResponse> handle(Exception ex) {
        try {
            exceptionManager.throwsException("ERR-0000", Map.of(
                    "message", ex.getMessage(),
                    "stacktrace", Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
        } catch (BusinessException e) {
            return handle(e);
        }
        return null;
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<BusinessExceptionResponse> handle(BusinessException ex) {

        return ResponseEntity.status(ex.getHttp()).body(
                BusinessExceptionResponse.fromException(ex)
        );
    }


}

