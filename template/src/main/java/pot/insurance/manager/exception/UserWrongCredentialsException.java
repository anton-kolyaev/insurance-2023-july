package pot.insurance.manager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class UserWrongCredentialsException extends DataIntegrityViolationException{
    
    public UserWrongCredentialsException(String message) {
        super(message);
    }

    public UserWrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }


}
