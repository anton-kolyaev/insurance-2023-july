package pot.insurance.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pot.insurance.manager.status.ClaimStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor()
@Builder
public class ClaimDTO {

    private UUID id;
    private UUID consumerId;
    private String employer;
    private Date date;
    private String plan;
    private BigDecimal amount;
    private ClaimStatus status;
}

