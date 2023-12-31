package pot.insurance.manager.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class CompanyFunctionsDTO {

    @JsonCreator
    public CompanyFunctionsDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("companyManager") boolean companyManager,
        @JsonProperty("consumer") boolean consumer,
        @JsonProperty("companyClaimManager") boolean companyClaimManager,
        @JsonProperty("consumerClaimManager") boolean consumerClaimManager,
        @JsonProperty("companySettingManager") boolean companySettingManager,
        @JsonProperty("companyReportManager") boolean companyReportManager) {

            this.id = id;
            this.companyManager = companyManager;
            this.consumer = consumer;
            this.companyClaimManager = companyClaimManager;
            this.consumerClaimManager = consumerClaimManager;
            this.companySettingManager = companySettingManager;
            this.companyReportManager = companyReportManager;
    }
    
    @JsonProperty("id")
    private UUID id;
    
    @Value("${companyManagerDefaultValue:false}")
    @JsonProperty
    private boolean companyManager;
    
    @Value("${consumerDefaultValue:false}")
    @JsonProperty
    private boolean consumer;
    
    @Value("${companyClaimManagerDefaultValue:false}")
    @JsonProperty
    private boolean companyClaimManager;
    
    @Value("${consumerClaimManagerDefaultValue:false}")
    @JsonProperty
    private boolean consumerClaimManager;

    @Value("${companySettingManagerDefaultValue:false}")
    @JsonProperty
    private boolean companySettingManager;
    
    @Value("${companyReportManagerDefaultValue:false}")
    @JsonProperty
    private boolean companyReportManager;


}
