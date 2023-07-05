package pot.insurance.manager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class WrongCredentialsException extends DataIntegrityViolationException{
    
    public WrongCredentialsException(String message) {
        super(message);
    }

    public WrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }


}
