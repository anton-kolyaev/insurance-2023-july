package pot.insurance.manager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class UserWrongCredentialsInput extends DataIntegrityViolationException{
    
    public UserWrongCredentialsInput(String message) {
        super(message);
    }

    public UserWrongCredentialsInput(String message, Throwable cause) {
        super(message, cause);
    }


}
