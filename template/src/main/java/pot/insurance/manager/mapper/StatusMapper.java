package pot.insurance.manager.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pot.insurance.manager.type.DataValidation;

// MapStruct mapping doesn't work for package level constructors :(
@Component
public class StatusMapper {

    public HttpStatus toHttp(DataValidation.Category category) {
        return switch (category) {
            case VALID -> HttpStatus.OK;
            case INVALID -> HttpStatus.BAD_REQUEST;
            case PRESENT -> HttpStatus.CONFLICT;
            case ABSENT -> HttpStatus.NOT_FOUND;
        };
    }
}
