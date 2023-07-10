package pot.insurance.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pot.insurance.manager.dto.ClaimErrorResponse;
import pot.insurance.manager.exception.ClaimNotFoundException;
import pot.insurance.manager.exception.ClaimWrongCredentialsInput;

@ControllerAdvice
public class ClaimRestExceptionHandler {
    
    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<ClaimErrorResponse> handleException(ClaimNotFoundException exc){
        ClaimErrorResponse error = new ClaimErrorResponse();
        error.setStatus(404);
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
