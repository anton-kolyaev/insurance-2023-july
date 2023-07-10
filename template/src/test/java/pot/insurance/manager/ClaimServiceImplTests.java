package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import pot.insurance.manager.repository.ClaimRepository;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.entity.Claim;
import pot.insurance.manager.exception.ClaimNotFoundException;
import pot.insurance.manager.exception.ClaimWrongCredentialsInput;
import pot.insurance.manager.service.ClaimServiceImpl;
import pot.insurance.manager.status.ClaimStatus;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ClaimServiceImplTests {
    
    @Mock
    private ClaimRepository claimRepository;
    
    @InjectMocks
    private ClaimServiceImpl claimService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        claimService = new ClaimServiceImpl(claimRepository);
    }

    @Test
    public void testSaveClaim() {
        // Arrange
        ClaimDTO claimDTO = new ClaimDTO();
        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setConsumerId(UUID.randomUUID());
        claim.setEmployer("Test inc.");
        claim.setPlan("Dental");
        claim.setDate(Date.valueOf("2023-01-01"));
        claim.setAmount(BigDecimal.valueOf(75.10));
        claim.setStatus(ClaimStatus.DECLINED);
            

        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        // Act
        ClaimDTO savedClaim = claimService.save(claimDTO);

        // Assert
        assertNotNull(savedClaim);
        assertEquals(claim.getId(), savedClaim.getId());
        verify(claimRepository, times(1)).save(any(Claim.class));
    }
    
    @Test
    public void testSaveClaimWithDuplicateClaimId(){
        // Arrange
        ClaimDTO claim = new ClaimDTO();
        claim.setId(UUID.randomUUID());
        claim.setConsumerId(UUID.randomUUID());
        claim.setEmployer("Test inc.");
        claim.setPlan("Dental");
        claim.setDate(Date.valueOf("2023-01-01"));
        claim.setAmount(BigDecimal.valueOf(75.10));
        claim.setStatus(ClaimStatus.PENDING);

        // Act
        when(claimRepository.save(any(Claim.class))).thenThrow(DataIntegrityViolationException.class);

        // Assert
        assertThrows(ClaimWrongCredentialsInput.class, () -> claimService.save(claim));
        assertNull(assertThrows(ClaimWrongCredentialsInput.class, () -> claimService.save(claim)).getMessage());
        verify(claimRepository, times(2)).save(any(Claim.class));
        
    }
    

    @Test
    public void testSaveClaimDuplicateExistedClaim(){

        // Arrange
        ClaimDTO claimDTO = new ClaimDTO();
        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setConsumerId(UUID.randomUUID());
        claim.setEmployer("Test inc.");
        claim.setPlan("Dental");
        claim.setDate(Date.valueOf("2023-01-01"));
        claim.setAmount(BigDecimal.valueOf(75.10));
        claim.setStatus(ClaimStatus.PENDING);

        // Act
        when(claimRepository.save(any(Claim.class))).thenReturn(claim).thenReturn(null);
        ClaimDTO savedClaim1 = claimService.save(claimDTO);
        ClaimDTO savedClaim2 = claimService.save(claimDTO);

        // Assert
        assertEquals(claim.getId(), savedClaim1.getId());
        assertNull(savedClaim2);
        verify(claimRepository, times(2)).save(any(Claim.class));
    }

    @Test
    public void testGetAllClaims() {
        // Arrange
        List<Claim> claimList = new ArrayList<>();
        when(claimRepository.findAll()).thenReturn(claimList);

        // Act
        List<ClaimDTO> fetchedClaims = claimService.findAll();

        // Assert
        assertNotNull(fetchedClaims);
        assertEquals(claimList, fetchedClaims);
        verify(claimRepository, times(1)).findAll();
    }
}
