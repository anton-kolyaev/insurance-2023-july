package pot.insurance.manager.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class CompanyWrongCredentialsException extends DataIntegrityViolationException {

    public CompanyWrongCredentialsException(String message) {
      super(message);
    }

    public CompanyWrongCredentialsException(String message, Throwable cause) {
      super(message, cause);
    }
}
