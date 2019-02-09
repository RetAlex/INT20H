package INT20H.task.controllers;

import INT20H.task.exception.IncorrectRequestParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectRequestParamException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        return ex.getMessage();
    }
}
