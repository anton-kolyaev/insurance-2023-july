package pot.insurance.manager.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        @ExceptionHandler({JsonMappingException.class})
        public ResponseEntity<StatusResponse> handle(JsonMappingException exception) {
            DataValidation.Status status;
            if (exception instanceof InvalidFormatException) {
                status = DataValidation.Status.MALFORMED_DATA;
            } else {
                status = DataValidation.Status.MISSING_DATA;
            }
            System.out.println(exception.getPath().toString());
            String key = exception.getPath()
                .listIterator(exception.getPath().size())
                .previous()
                .getFieldName();
            StatusResponse response = StatusResponse.builder()
                .code(status.getCode())
                .timestamp(System.currentTimeMillis())
                .message("key '" + key + "' " + status.getDescription())
                .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(DataValidationException.class)
        public ResponseEntity<StatusResponse> handle(DataValidationException exception) {
            DataValidation.Status status = exception.getStatus();
            StatusResponse response = StatusResponse.builder()
                .code(status.getCode())
                .timestamp(System.currentTimeMillis())
                .message(exception.getMessage())
                .build();
            return ResponseEntity.status(this.mapper.toHttp(status.getCategory())).body(response);
        }
}