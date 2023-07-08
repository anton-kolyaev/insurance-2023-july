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

import pot.insurance.manager.controller.UserRestController;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.service.UserService;

@WebMvcTest(UserRestController.class)
@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerTests {
    
    @MockBean
    private UserService userService;
    
    @InjectMocks
    private UserRestController userRestController;

    @Before
    public void setUp() {
        try (final AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            userService = mock(UserService.class);
            userRestController = new UserRestController(userService);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testSaveUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO();
            userDTO.setId(UUID.randomUUID());
            userDTO.setFirstName("Sammy");
            userDTO.setLastName("Sam");
            userDTO.setSsn("123456789");
            userDTO.setBirthday(Date.valueOf("1990-01-01"));
            userDTO.setEmail("test@test.test");

        when(userService.save(userDTO)).thenReturn(userDTO);

        // Act
        UserDTO response = userRestController.saveUser(userDTO);

        // Asserert
        assertEquals(userDTO, response);
        verify(userService, times(1)).save(userDTO);
    }

    @Test
    public void testSaveUser_BadRequest() {
        // Arrange
        UserDTO userDTO = null;

        // Act
        when(userService.save(userDTO)).thenReturn( null);
        UserDTO response = userRestController.saveUser(userDTO);

        // Assert
        assertNull(response);
        verify(userService, times(1)).save(userDTO);
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        UserDTO userDTO1 = new UserDTO();
            userDTO1.setId(UUID.randomUUID());
            userDTO1.setFirstName("Kenny");
            userDTO1.setLastName("Martin");
            userDTO1.setSsn("123456789");
            userDTO1.setBirthday(Date.valueOf("1990-01-01"));
            userDTO1.setEmail("test@test.com");

        UserDTO userDTO2 = new UserDTO();
            userDTO2.setId(UUID.randomUUID());
            userDTO2.setFirstName("Jane");
            userDTO2.setLastName("Smith");
            userDTO2.setSsn("987654321");
            userDTO2.setBirthday(Date.valueOf("1990-01-01"));
            userDTO2.setEmail("test@Test2.com");

        List<UserDTO> userDTOList = List.of(userDTO1, userDTO2);

        // Act
        when(userService.findAll()).thenReturn(userDTOList);
        Object result = userRestController.findAllUsers();

        // Assert
        assertEquals(userDTOList, result);
        verify(userService, times(1)).findAll();
        
    }

    @Test
    public void testFindUserById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
            userDTO.setId(UUID.randomUUID());
            userDTO.setFirstName("Sammy");
            userDTO.setLastName("Sam");
            userDTO.setSsn("123456789");
            userDTO.setBirthday(Date.valueOf("1990-01-01"));
            userDTO.setEmail("test@test.test");

        // Act
        when(userService.findById(id)).thenReturn(userDTO);
        Object result = userRestController.findUserById(id);

        // Assert
        assertEquals(userDTO, result);
        verify(userService, times(1)).findById(id);
    }

    @Test
    public void testFindUserById_NotFoundUserId() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userService.findById(userId)).thenReturn(null);
        Object result = userRestController.findUserById(userId);
        // Act & Assert
        assertNull(result);
        verify(userService, times(1)).findById(userId);
    }
}
