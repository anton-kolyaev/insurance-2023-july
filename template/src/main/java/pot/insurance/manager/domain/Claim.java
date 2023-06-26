package pot.insurance.manager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(schema = "java_internship", name="claims")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "consumer_name")
    private String consumer_name;
    @Column(name = "employer")
    private String employer;
    @Column(name = "date")
    private Date date;
    @Column(name = "plan")
    private String plan;

    @Column(name = "amount")
    private int amount;

    @Column(name = "status")
    private Enum status;
}
