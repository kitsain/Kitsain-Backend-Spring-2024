package com.myblogbackend.blog.exception;

import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.response.ResponseEntityBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class ControllerExceptionHandler {
    private final static Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleException(final MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : errors) {
            details.putIfAbsent(fieldError.getField(), "");
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
            LOGGER.warn("Validation error for field '{}': {}", fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.BAD_REQUEST)
                .setMessage("Validation errors")
                .setDetails(details)
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<?> processMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception) {
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.METHOD_NOT_ALLOWED)
                .setMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<?> processAccessDeniedException(final AccessDeniedException e) {
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.FORBIDDEN)
                .setMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = BlogRuntimeException.class)
    @ResponseBody
    public ResponseEntity<?> handler(final BlogRuntimeException e, final HttpServletRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setCode(Integer.parseInt(String.valueOf(e.getStatus().value())))
                .setMessage(e.getMessage())
                .build();
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<?> handleConstraintViolationException(final ConstraintViolationException e) {
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.BAD_REQUEST)
                .setMessage(e.getMessage())
                .build();
    }

}