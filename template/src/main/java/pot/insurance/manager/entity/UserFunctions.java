package pot.insurance.manager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_functions", schema = "insurance_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserFunctionsId.class)
public class UserFunctions {
    
    @Id
    @Column(name="user_id", columnDefinition = "BINARY(16)")
    private UUID userId;
    
    @Id
    @Column(name = "company_id", columnDefinition = "BINARY(16)")
    private UUID companyId;

    @Column(name = "company_manager")
    private boolean companyManager;

    @Column(name = "consumer")
    private boolean consumer;

    @Column(name = "company_claim_manager")
    private boolean companyClaimManager;

    @Column(name = "consumer_claim_manager")
    private boolean consumerClaimManager;

    @Column(name = "company_setting_manager")
    private boolean companySettingManager;

    @Column(name = "company_report_manager")
    private boolean companyReportManager;
}
