package pot.insurance.manager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_functions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFunctions {
    
    @Id
    @Column(name = "id")
    private UUID id;

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
