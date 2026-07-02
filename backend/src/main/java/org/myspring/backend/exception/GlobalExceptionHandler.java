package org.myspring.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserIdNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handeUserIdNotFound(UserIdNotFound except){
        return "Ein Fehler ist aufgetreten: " + except.getLocalizedMessage();
    }
}
