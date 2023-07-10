package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
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
            claim.setClaimId(UUID.randomUUID());
            claim.setFirstName("Sammy");
            claim.setLastName("Sam");
            claim.setSsn("123456789");
            claim.setBirthday(Date.valueOf("1990-01-01"));
            claim.setEmail("test@test.test");
            claim.setClaimname("test_sam");

        when(claimService.save(claim)).thenReturn(claim);

        // Act
        ClaimDTO response = claimRestController.saveClaim(claim);

        // Asserert
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
        Object result = claimRestController.findAllClaims();

        // Assert
        assertEquals(claimList, result);
        verify(claimService, times(1)).findAll();
        
    }

    @Test
    public void testFindClaimById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClaimDTO claim = new ClaimDTO();
            claim.setClaimId(UUID.randomUUID());
            claim.setFirstName("Sammy");
            claim.setLastName("Sam");
            claim.setSsn("123456789");
            claim.setBirthday(Date.valueOf("1990-01-01"));
            claim.setEmail("test@test.test");
            claim.setClaimname("test_sam");

        // Act
        when(claimService.findById(id)).thenReturn(claim);
        Object result = claimRestController.findClaimById(id);

        // Assert
        assertEquals(claim, result);
        verify(claimService, times(1)).findById(id);
    }

    @Test
    public void testFindClaimById_NotFoundClaimId() {
        // Arrange
        UUID claimId = UUID.randomUUID();
        when(claimService.findById(claimId)).thenReturn(null);
        Object result = claimRestController.findClaimById(claimId);
        // Act & Assert
        assertNull(result);
        verify(claimService, times(1)).findById(claimId);
    }
}
