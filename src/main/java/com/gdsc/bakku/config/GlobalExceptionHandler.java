package com.gdsc.bakku.config;

import com.gdsc.bakku.common.exception.CustomAbstractException;
import com.gdsc.bakku.common.response.FailureResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomAbstractException.class)
    public ResponseEntity<FailureResponseBody> handlerExceptions(CustomAbstractException e) {
        return FailureResponseBody.toResponseEntity(e.getStatusEnum());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<Map<String, Object>> processValidationError(BindException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("](은)는 ");
            sb.append(fieldError.getDefaultMessage());
            sb.append(" 입력된 값: [");
            sb.append(fieldError.getRejectedValue());
            sb.append("]");
            fields.add(sb.toString());
            sb.setLength(0);
        }

        System.out.println(sb);

        Map<String, Object> body = new HashMap<>();
        body.put("code", HttpStatus.BAD_REQUEST.name());
        body.put("detail", fields);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }
}
