package pot.insurance.manager;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.service.FunctionsService;
import pot.insurance.manager.type.DataValidation;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RunWith(MockitoJUnitRunner.class)
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class FunctionsIntegrationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FunctionsService functionsService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        functionsService = mock(FunctionsService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(functionsService).build();
    }

    @Test
    public void testSaveCompanyFunctions() throws Exception {
        // Prepare test data
        UUID companyId = UUID.randomUUID();
        CompanyFunctionsDTO companyFunctionsDTO = new CompanyFunctionsDTO();
        companyFunctionsDTO.setId(companyId);
        companyFunctionsDTO.setCompanyManager(true);
        companyFunctionsDTO.setConsumer(true);
        companyFunctionsDTO.setCompanyClaimManager(true);
        companyFunctionsDTO.setConsumerClaimManager(false);
        companyFunctionsDTO.setCompanySettingManager(true);
        companyFunctionsDTO.setCompanyReportManager(true);
        
        // Mock the service method
        when(functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO))
                .thenReturn(companyFunctionsDTO);
        // Perform the POST request
        mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(companyFunctionsDTO)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(companyId.toString())))
                .andExpect(jsonPath("$.companyManager", is(true)))
                .andExpect(jsonPath("$.consumer", is(true)))
                .andExpect(jsonPath("$.companyClaimManager", is(true)))
                .andExpect(jsonPath("$.consumerClaimManager", is(false)))
                .andExpect(jsonPath("$.companySettingManager", is(true)))
                .andExpect(jsonPath("$.companyReportManager", is(true)));

        // Verify the service method is called with the correct arguments
        verify(functionsService).saveCompanyFunctions(companyId, companyFunctionsDTO);
    }

    @Test
    public void testSaveCompanyFunctions_InvalidCompanyId() throws Exception {
        // Prepare test data
        UUID companyId = UUID.randomUUID();
        CompanyFunctionsDTO companyFunctionsDTO = new CompanyFunctionsDTO();
        companyFunctionsDTO.setId(companyId);
        // Mock the service method to throw DataValidationException
        when(functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO))
                .thenThrow(new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND));
        // Perform the POST request
        mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(companyFunctionsDTO)))
                .andExpect(status().isNotFound());
        // Verify the service method is called with the correct arguments
        verify(functionsService, never()).saveCompanyFunctions(any(UUID.class), any(CompanyFunctionsDTO.class));
        
    }
}