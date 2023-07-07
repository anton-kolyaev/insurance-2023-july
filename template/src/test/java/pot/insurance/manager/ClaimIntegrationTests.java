package pot.insurance.manager;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import pot.insurance.manager.exception.exeptions.ClaimNotFoundException;
import pot.insurance.manager.service.ClaimService;


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
            claim.setClaimId(UUID.randomUUID());
            claim.setFirstName("Sammy");
            claim.setLastName("Sam");
            claim.setSsn("123456789");
            claim.setBirthday(Date.valueOf("1990-01-01"));
            claim.setEmail("test@test.test");
            claim.setClaimname("test_sam");

        // Act
        when(claimService.save(any(ClaimDTO.class))).thenReturn(claim);

        // Assert
        mockMvc.perform(post("/v1/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(claim)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.claimId", is(claim.getClaimId().toString())))
                .andExpect(jsonPath("$.firstName", is(claim.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(claim.getLastName())))
                .andExpect(jsonPath("$.ssn", is(claim.getSsn())))
                .andExpect(jsonPath("$.birthday", is(claim.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(claim.getEmail())))
                .andExpect(jsonPath("$.claimname", is(claim.getClaimname())));

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
            claim1.setClaimId(UUID.randomUUID());
            claim1.setFirstName("Kenny");
            claim1.setLastName("Martin");
            claim1.setSsn("123456789");
            claim1.setBirthday(Date.valueOf("1990-01-01"));
            claim1.setEmail("test@test.com");
            claim1.setClaimname("test_kenny");

        ClaimDTO claim2 = new ClaimDTO();
                claim2.setClaimId(UUID.randomUUID());
                claim2.setFirstName("Jane");
                claim2.setLastName("Smith");
                claim2.setSsn("987654321");
                claim2.setBirthday(Date.valueOf("1990-01-01"));
                claim2.setEmail("test@Test2.com");
                claim2.setClaimname("test_jane");

        List<ClaimDTO> claimList = List.of(claim1, claim2);
        // Act
        when(claimService.findAll()).thenReturn(claimList);

        // Assert
        mockMvc.perform(get("/v1/claims"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].claimId", is(claim1.getClaimId().toString())))
                .andExpect(jsonPath("$[0].firstName", is(claim1.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(claim1.getLastName())))
                .andExpect(jsonPath("$[0].ssn", is(claim1.getSsn())))
                .andExpect(jsonPath("$[0].birthday", is(claim1.getBirthday().getTime())))
                .andExpect(jsonPath("$[0].email", is(claim1.getEmail())))
                .andExpect(jsonPath("$[0].claimname", is(claim1.getClaimname())))
        // Claim2
                .andExpect(jsonPath("$[1].claimId", is(claim2.getClaimId().toString())))
                .andExpect(jsonPath("$[1].firstName", is(claim2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(claim2.getLastName())))
                .andExpect(jsonPath("$[1].ssn", is(claim2.getSsn())))
                .andExpect(jsonPath("$[1].birthday", is(claim2.getBirthday().getTime())))
                .andExpect(jsonPath("$[1].email", is(claim2.getEmail())))
                .andExpect(jsonPath("$[1].claimname", is(claim2.getClaimname())));

        verify(claimService, times(1)).findAll();
        }



        @Test
        public void findClaimById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();
        ClaimDTO claim = new ClaimDTO();
                claim.setClaimId(id);
                claim.setFirstName("Sammy");
                claim.setLastName("Sam");
                claim.setSsn("123456789");
                claim.setBirthday(Date.valueOf("1990-01-01"));
                claim.setEmail("test@test.test");
                claim.setClaimname("test_sam");

        // Act
        when(claimService.findById(id)).thenReturn(claim);
        
        // Assert
        mockMvc.perform(get("/v1/claims/{id}", claim.getClaimId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.claimId", is(claim.getClaimId().toString())))
                .andExpect(jsonPath("$.firstName", is(claim.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(claim.getLastName())))
                .andExpect(jsonPath("$.ssn", is(claim.getSsn())))
                .andExpect(jsonPath("$.birthday", is(claim.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(claim.getEmail())))
                .andExpect(jsonPath("$.claimname", is(claim.getClaimname())));

        verify(claimService).findById(id);
        }
        
        @Test
        public void notFindClaimById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        when(claimService.findById(id)).thenThrow(new ClaimNotFoundException("Claim not found by id: " + id));
        // Assert
        mockMvc.perform(get("/claims/{claim_id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(claimService, never()).findById(id);
        }
}