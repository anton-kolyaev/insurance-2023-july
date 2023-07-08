package pot.insurance.manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.dto.StatusResponse;
import pot.insurance.manager.mapper.StatusMapper;
import pot.insurance.manager.type.DataValidation;

@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final StatusMapper mapper;

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<StatusResponse> handleException(DataValidationException exception) {
        DataValidation.Status status = exception.getStatus();
        StatusResponse response = StatusResponse.builder()
            .code(status.getCode())
            .timestamp(System.currentTimeMillis())
            .message(status.getDescription())
            .build();
        return ResponseEntity.status(this.mapper.toHttp(status.getCategory())).body(response);
    }
}
