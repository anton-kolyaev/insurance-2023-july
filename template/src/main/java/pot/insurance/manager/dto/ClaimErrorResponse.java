package pot.insurance.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimErrorResponse {
    
    private int status;
    private String message;
    private String timeStamp;
    
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = formatTimeStamp(timeStamp);
    }
    private String formatTimeStamp(long timeStamp) {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd.MM.yy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
