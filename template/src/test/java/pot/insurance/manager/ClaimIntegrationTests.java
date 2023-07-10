package pot.insurance.manager;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import pot.insurance.manager.controller.ClaimRestController;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.exception.ClaimNotFoundException;
import pot.insurance.manager.service.ClaimService;
import pot.insurance.manager.status.ClaimStatus;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RunWith(MockitoJUnitRunner.class)

public class ClaimIntegrationTests {
        
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ClaimService claimService;



        @Before
        public void setup() {
            MockitoAnnotations.openMocks(this);
            claimService = mock(ClaimService.class);
            mockMvc = MockMvcBuilders.standaloneSetup(new ClaimRestController(claimService)).build();
        }

        @Test
        public void saveClaimReturnSuccessStatus() throws Exception {

        // Arrange
        ClaimDTO claim = new ClaimDTO();
            claim.setId(UUID.randomUUID());
            claim.setConsumerId(UUID.randomUUID());
            claim.setEmployer("Test inc.");
            claim.setPlan("Dental");
            claim.setDate(Date.valueOf("2023-01-01"));
            claim.setAmount(BigDecimal.valueOf(75.10));
            claim.setStatus(ClaimStatus.DECLINED);

        // Act
        when(claimService.save(any(ClaimDTO.class))).thenReturn(claim);

        // Assert
        mockMvc.perform(post("/v1/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(claim)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.claimId", is(claim.getId().toString())))
                .andExpect(jsonPath("$.consumerId", is(claim.getConsumerId())))
                .andExpect(jsonPath("$.employer", is(claim.getEmployer())))
                .andExpect(jsonPath("$.plan", is(claim.getPlan())))
                .andExpect(jsonPath("$.date", is(claim.getDate().getTime())))
                .andExpect(jsonPath("$.amount", is(claim.getAmount())))
                .andExpect(jsonPath("$.status", is(claim.getStatus())));

        verify(claimService).save(any(ClaimDTO.class));
        }

        @Test
        public void saveClaimReturnBadRequestStatus() throws Exception {

        // Arrange
        String invalidClaim = "{\"first_name\": \"Sammy\", \"last_name\": \"Sam\", \"ssn\": \"123456789\", \"birthday\": \"1990-01-01\", \"email\": \"}";

        // Act & Assert
        mockMvc.perform(post("/v1/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidClaim)))
                .andExpect(status().isBadRequest());

        verify(claimService, never()).save(any(ClaimDTO.class));
}

        @Test
        public void findAllClaims() throws Exception{

        // Arrange
        ClaimDTO claim1 = new ClaimDTO();
            claim1.setId(UUID.randomUUID());
            claim1.setConsumerId(UUID.randomUUID());
            claim1.setEmployer("Test inc.");
            claim1.setPlan("Dental");
            claim1.setDate(Date.valueOf("2023-01-01"));
            claim1.setAmount(BigDecimal.valueOf(15.10));
            claim1.setStatus(ClaimStatus.DECLINED);

            ClaimDTO claim2 = new ClaimDTO();
            claim2.setId(UUID.randomUUID());
            claim2.setConsumerId(UUID.randomUUID());
            claim2.setEmployer("Testing Solutions");
            claim2.setPlan("Medical");
            claim2.setDate(Date.valueOf("2021-01-01"));
            claim2.setAmount(BigDecimal.valueOf(75.10));
            claim2.setStatus(ClaimStatus.APPROVED);

            ClaimDTO claim3 = new ClaimDTO();
            claim3.setId(UUID.randomUUID());
            claim3.setConsumerId(UUID.randomUUID());
            claim3.setEmployer("Testing Solutions");
            claim3.setPlan("Medical");
            claim3.setDate(Date.valueOf("2021-01-01"));
            claim3.setAmount(BigDecimal.valueOf(75.10));
            claim3.setStatus(ClaimStatus.PENDING);

            ClaimDTO claim4 = new ClaimDTO();
            claim4.setId(UUID.randomUUID());
            claim4.setConsumerId(UUID.randomUUID());
            claim4.setEmployer("Testing Solutions");
            claim4.setPlan("Medical");
            claim4.setDate(Date.valueOf("2021-01-01"));
            claim4.setAmount(BigDecimal.valueOf(75.10));
            claim4.setStatus(ClaimStatus.DELETED);

        List<ClaimDTO> claimList = List.of(claim1, claim2, claim3, claim4);
        // Act
        when(claimService.findAll()).thenReturn(claimList);

        // Assert
        mockMvc.perform(get("/v1/claims"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.claimId", is(claim1.getId().toString())))
                .andExpect(jsonPath("$.consumerId", is(claim1.getConsumerId())))
                .andExpect(jsonPath("$.employer", is(claim1.getEmployer())))
                .andExpect(jsonPath("$.plan", is(claim1.getPlan())))
                .andExpect(jsonPath("$.date", is(claim1.getDate().getTime())))
                .andExpect(jsonPath("$.amount", is(claim1.getAmount())))
                .andExpect(jsonPath("$.status", is(claim1.getStatus())));
            // Claim2
            mockMvc.perform(get("/v1/claims"))
                    .andExpect(jsonPath("$.claimId", is(claim2.getId().toString())))
                    .andExpect(jsonPath("$.consumerId", is(claim2.getConsumerId())))
                    .andExpect(jsonPath("$.employer", is(claim2.getEmployer())))
                    .andExpect(jsonPath("$.plan", is(claim2.getPlan())))
                    .andExpect(jsonPath("$.date", is(claim2.getDate().getTime())))
                    .andExpect(jsonPath("$.amount", is(claim2.getAmount())))
                    .andExpect(jsonPath("$.status", is(claim2.getStatus())));
            // Claim3
            mockMvc.perform(get("/v1/claims"))
                    .andExpect(jsonPath("$.claimId", is(claim3.getId().toString())))
                    .andExpect(jsonPath("$.consumerId", is(claim3.getConsumerId())))
                    .andExpect(jsonPath("$.employer", is(claim3.getEmployer())))
                    .andExpect(jsonPath("$.plan", is(claim3.getPlan())))
                    .andExpect(jsonPath("$.date", is(claim3.getDate().getTime())))
                    .andExpect(jsonPath("$.amount", is(claim3.getAmount())))
                    .andExpect(jsonPath("$.status", is(claim3.getStatus())));
            // Claim4
            mockMvc.perform(get("/v1/claims"))
                    .andExpect(jsonPath("$.claimId", is(claim4.getId().toString())))
                    .andExpect(jsonPath("$.consumerId", is(claim4.getConsumerId())))
                    .andExpect(jsonPath("$.employer", is(claim4.getEmployer())))
                    .andExpect(jsonPath("$.plan", is(claim4.getPlan())))
                    .andExpect(jsonPath("$.date", is(claim4.getDate().getTime())))
                    .andExpect(jsonPath("$.amount", is(claim4.getAmount())))
                    .andExpect(jsonPath("$.status", is(claim4.getStatus())));

        verify(claimService, times(1)).findAll();
        }
}