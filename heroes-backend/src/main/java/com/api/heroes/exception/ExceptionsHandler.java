package com.api.heroes.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({ResponseStatusException.class})
    public ErrorResposta handleResponseStatusException(ResponseStatusException ex) {
        return new ErrorResposta(HttpStatus.BAD_REQUEST.value(), ex.getReason());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorValidacao handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        var messagemErro = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        var campoError = ex.getBindingResult().getFieldError().getField();

        return new ErrorValidacao(campoError, messagemErro);
    }
}