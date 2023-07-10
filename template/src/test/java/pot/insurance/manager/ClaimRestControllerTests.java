package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import pot.insurance.manager.controller.ClaimRestController;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.service.ClaimService;
import pot.insurance.manager.status.ClaimStatus;

@WebMvcTest(ClaimRestController.class)
@RunWith(MockitoJUnitRunner.class)
public class ClaimRestControllerTests {
    
    @MockBean
    private ClaimService claimService;
    
    @InjectMocks
    private ClaimRestController claimRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        claimService = mock(ClaimService.class);
        claimRestController = new ClaimRestController(claimService);
    }

    @Test
    public void testSaveClaim_Success() {
        // Arrange
        ClaimDTO claim = new ClaimDTO();
        claim.setId(UUID.randomUUID());
        claim.setConsumerId(UUID.randomUUID());
        claim.setEmployer("Test inc.");
        claim.setPlan("Dental");
        claim.setDate(Date.valueOf("2023-01-01"));
        claim.setAmount(BigDecimal.valueOf(75.10));
        claim.setStatus(ClaimStatus.DECLINED);

        when(claimService.save(claim)).thenReturn(claim);

        // Act
        ClaimDTO response = claimRestController.saveClaim(claim);

        // Assert
        assertEquals(claim, response);
        verify(claimService, times(1)).save(claim);
    }

    @Test
    public void testSaveClaim_BadRequest() {
        // Arrange
        ClaimDTO claimDTO = null;

        // Act
        when(claimService.save(claimDTO)).thenReturn( null);
        ClaimDTO response = claimRestController.saveClaim(claimDTO);

        // Assert
        assertNull(response);
        verify(claimService, times(1)).save(claimDTO);
    }

    @Test
    public void testFindAllClaims() {
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
        Object result = claimRestController.findAllClaims();

        // Assert
        assertEquals(claimList, result);
        verify(claimService, times(1)).findAll();
        
    }

//    @Test
//    public void testFindClaimById_Success() {
//        // Arrange
//        UUID id = UUID.randomUUID();
//        ClaimDTO claim = new ClaimDTO();
//            claim.setId(UUID.randomUUID());
//            claim.setConsumerName("Andrius");
//            claim.setEmployer("Coherent inc.");
//            claim.setPlan("Dental");
//            claim.setDate(Date.valueOf("2023-09-01"));
//            claim.setAmount(BigDecimal.valueOf(146.99));
//            claim.setStatus(ClaimStatus.APPROVED);
//        // Act
//        when(claimService.findById(id)).thenReturn(claim);
//        Object result = claimRestController.findClaimById(id);
//
//        // Assert
//        assertEquals(claim, result);
//        verify(claimService, times(1)).findById(id);
//    }

//    @Test
//    public void testFindClaimById_NotFoundClaimId() {
//        // Arrange
//        UUID claimId = UUID.randomUUID();
//        when(claimService.findById(claimId)).thenReturn(null);
//        Object result = claimRestController.findClaimById(claimId);
//        // Act & Assert
//        assertNull(result);
//        verify(claimService, times(1)).findById(claimId);
//    }
}
