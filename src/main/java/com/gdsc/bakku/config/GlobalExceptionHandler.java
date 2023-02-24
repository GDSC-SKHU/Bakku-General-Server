package com.gdsc.bakku.config;

import com.gdsc.bakku.common.exception.CustomAbstractException;
import com.gdsc.bakku.common.response.FailureResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FailureResponseBody> handlerExceptions(CustomAbstractException e) {
        return FailureResponseBody.toResponseEntity(e.getStatusEnum());
    }
}
