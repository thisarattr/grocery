package com.thisarattr.grocery.controllers;

import com.thisarattr.grocery.exception.APIException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ APIException.class})
    protected ResponseEntity<Object> handleApiException(APIException ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format("{ \"message\": \"%s\" }", ex.getMessage()), new HttpHeaders(), HttpStatus.valueOf(ex.getCode()), request);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format("{ \"message\": \"%s\" }", ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}