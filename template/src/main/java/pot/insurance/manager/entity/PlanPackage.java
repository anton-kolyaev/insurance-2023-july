package pot.insurance.manager.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pot.insurance.manager.type.PayrollFrequency;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan_packages")
public class PlanPackage {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "payroll")
    @Enumerated(EnumType.STRING)
    private PayrollFrequency payroll;

    @Column(name = "starts")
    private LocalDate starts;

    @Column(name = "expires")
    private LocalDate expires;
}
