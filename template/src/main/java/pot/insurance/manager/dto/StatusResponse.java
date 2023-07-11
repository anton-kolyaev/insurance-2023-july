package pot.insurance.manager.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.*;

@Value
public class StatusResponse {

    int code;
    String message, timestamp;

    @Builder
    public StatusResponse(int code, String message, long timestamp) {
        this.code = code;
        this.message = message;

        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        this.timestamp = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}