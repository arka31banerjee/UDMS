package com.udms.controller.advice;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.udms.dto.UdmsErrorResponse;
import com.udms.exception.ValidationAndBusinessException;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UdmsErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().parallelStream()
				.map(error -> error.getField() + ": "+ error.getDefaultMessage())
				.collect(Collectors.joining(","));
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(errorMessage,HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<UdmsErrorResponse> handleBindExceptions(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().parallelStream()
        				.map(error -> error.getField() + ": "+ error.getDefaultMessage())
        				.collect(Collectors.joining(","));
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(errorMessage,HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<UdmsErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid argument type: " + ex.getName();
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(message,HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<UdmsErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(message,HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UdmsErrorResponse> handleException(Exception ex) {
        String message = "An error occurred: " + ex.getMessage();
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(message,HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ValidationAndBusinessException.class)
    public ResponseEntity<UdmsErrorResponse> handleCustomException(ValidationAndBusinessException ex) {
        UdmsErrorResponse errorResponse = new UdmsErrorResponse(ex.getErrorMessage(),HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
