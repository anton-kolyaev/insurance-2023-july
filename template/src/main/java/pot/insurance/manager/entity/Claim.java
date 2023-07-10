package pot.insurance.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pot.insurance.manager.status.ClaimStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "claims", schema = "java_internship")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "consumerId")
    private UUID consumerId;

    @Column(name = "employer")
    private String employer;

    @Column(name = "date")
    private Date date;

    @Column(name = "plan")
    private String plan;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private ClaimStatus status;
}
