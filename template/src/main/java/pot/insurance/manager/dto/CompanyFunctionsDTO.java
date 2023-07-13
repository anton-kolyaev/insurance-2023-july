package pot.insurance.manager.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Builder
public class CompanyFunctionsDTO {
    
    @JsonProperty
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
