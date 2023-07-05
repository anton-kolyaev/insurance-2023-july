package pot.insurance.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pot.insurance.manager.exception.UserWrongCredentialsException;
import pot.insurance.manager.exception.UserNotFoundException;
import pot.insurance.manager.dto.UserErrorResponse;

@ControllerAdvice
public class UserRestExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException exc){
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(404);
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWrongCredentialsException.class)
    public ResponseEntity<UserErrorResponse> handleException(UserWrongCredentialsException exc){
    if (exc.getMessage().contains("USERNAME") && exc.getMessage().contains("Unique index or primary key violation")) {
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(409);
        error.setMessage("Username already exists!");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    } else if (exc.getMessage().contains("SSN") && exc.getMessage().contains("Unique index or primary key violation")) {
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(409);
        error.setMessage("SSN already exists!");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    } else {
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(400);
        error.setMessage("User credentials have wrong input!");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
    
}
