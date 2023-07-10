package pot.insurance.manager.exception;

import lombok.Getter;

import org.springframework.dao.DataIntegrityViolationException;

import pot.insurance.manager.type.DataValidation;

public class DataValidationException extends DataIntegrityViolationException {

    @Getter
    DataValidation.Status status;

    public DataValidationException(DataValidation.Status status, String message) {
        super(message == null ? status.getDescription() : message);
        this.status = status;
    }

    public DataValidationException(DataValidation.Status status) {
        this(status, status.getDescription());
    }

}