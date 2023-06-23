package pot.insurance.manager;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;
import pot.insurance.manager.dao.UserDAOJpaImpl;
import pot.insurance.manager.dto.UserDTO;

@SpringBootTest
class ManagerApplicationTests {

	@Test
	void contextLoads() {
	}
	@Mock
    private EntityManager entityManager;
    
    private UserDAOJpaImpl userDAO;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserDAOJpaImpl(entityManager);
    }
    
    @Test
    public void testSaveUser() {
        // Create a sample user
        UserDTO user = new UserDTO();
        user.setId(UUID.randomUUID());
        user.setFirst_name("Sammy");
        user.setLast_name("Sam");
        // Set other properties
        
        // Mock the merge operation of EntityManager
        Mockito.when(entityManager.merge(Mockito.any(UserDTO.class))).thenReturn(user);
        
        // Call the saveUser method
        UserDTO savedUser = userDAO.saveUser(user);
        
        // Verify the result
        Assertions.assertEquals(user, savedUser);
        Mockito.verify(entityManager, Mockito.times(1)).merge(user);
    }
}
