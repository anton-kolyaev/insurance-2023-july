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
        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");

        when(userService.save(user)).thenReturn(user);

        // Act
        UserDTO response = userRestController.saveUser(user);

        // Asserert
        assertEquals(user, response);
        verify(userService, times(1)).save(user);
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
        UserDTO user1 = new UserDTO();
            user1.setId(UUID.randomUUID());
            user1.setFirstName("Kenny");
            user1.setLastName("Martin");
            user1.setSsn("123456789");
            user1.setBirthday(Date.valueOf("1990-01-01"));
            user1.setEmail("test@test.com");

        UserDTO user2 = new UserDTO();
            user2.setId(UUID.randomUUID());
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setSsn("987654321");
            user2.setBirthday(Date.valueOf("1990-01-01"));
            user2.setEmail("test@Test2.com");

        List<UserDTO> userList = List.of(user1, user2);

        // Act
        when(userService.findAll()).thenReturn(userList);
        Object result = userRestController.findAllUsers();

        // Assert
        assertEquals(userList, result);
        verify(userService, times(1)).findAll();
        
    }

    @Test
    public void testFindUserById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");

        // Act
        when(userService.findById(id)).thenReturn(user);
        Object result = userRestController.findUserById(id);

        // Assert
        assertEquals(user, result);
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
