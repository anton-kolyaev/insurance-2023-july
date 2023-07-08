package pot.insurance.manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pot.insurance.manager.type.PlanType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PlanType type;

    @Column(name = "contributions")
    private BigDecimal contributions;

}
