package pot.insurance.manager.exception;

import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Value;

import org.springframework.dao.DataIntegrityViolationException;

import pot.insurance.manager.type.DataValidation;

public class DataValidationException extends DataIntegrityViolationException {

    @Getter
    DataValidation.Status status;

    public DataValidationException(DataValidation.Status status) {
        super(status.getDescription());

        this.status = status;
    }

}
