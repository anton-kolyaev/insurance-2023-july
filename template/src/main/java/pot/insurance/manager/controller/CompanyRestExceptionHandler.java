package pot.insurance.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pot.insurance.manager.dto.ErrorResponse;
import pot.insurance.manager.exception.WrongCredentialsException;

@ControllerAdvice
public class CompanyRestExceptionHandler {
    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(WrongCredentialsException exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(400);
        error.setMessage("User credentials have wrong input!");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
