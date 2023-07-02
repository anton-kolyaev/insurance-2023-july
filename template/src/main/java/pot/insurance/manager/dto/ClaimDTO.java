package pot.insurance.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDTO {
    private UUID id;
    private String consumer_name;
    private String employer;
    private Date date;
    private String plan;
    private int amount;
    private Enum status;
}

