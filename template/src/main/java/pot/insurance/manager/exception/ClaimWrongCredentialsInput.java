package pot.insurance.manager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class ClaimWrongCredentialsInput extends DataIntegrityViolationException{

    public ClaimWrongCredentialsInput(String message) {
        super(message);
    }
}
