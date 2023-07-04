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
        MockitoAnnotations.openMocks(this);
        userService = mock(UserService.class);
        userRestController = new UserRestController(userService);
    }

    @Test
    public void testSaveUser_Success() {
        // Arrange
        UserDTO user = new UserDTO();
            user.setUserId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

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
            user1.setUserId(UUID.randomUUID());
            user1.setFirstName("Kenny");
            user1.setLastName("Martin");
            user1.setSsn("123456789");
            user1.setBirthday(Date.valueOf("1990-01-01"));
            user1.setEmail("test@test.com");
            user1.setUsername("test_kenny");

        UserDTO user2 = new UserDTO();
            user2.setUserId(UUID.randomUUID());
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setSsn("987654321");
            user2.setBirthday(Date.valueOf("1990-01-01"));
            user2.setEmail("test@Test2.com");
            user2.setUsername("test_jane");

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
            user.setUserId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

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


    @Test
    public void testUpdateUser_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO userBeforeUpdate = new UserDTO();
        UserDTO updatedUser = new UserDTO();
            updatedUser.setUserId(UUID.randomUUID());
            updatedUser.setFirstName("Sammy");
            updatedUser.setLastName("Sam");
            updatedUser.setSsn("123456789");
            updatedUser.setBirthday(Date.valueOf("1990-01-01"));
            updatedUser.setEmail("test@test.test");
            updatedUser.setUsername("test_sam");

        // Act
        when(userService.update(userId, userBeforeUpdate)).thenReturn(updatedUser);
        Object result = userRestController.updateUser(userId, userBeforeUpdate);

        // Assert
        assertEquals(updatedUser, result);
        verify(userService, times(1)).update(any(UUID.class), any(UserDTO.class));
    }

    @Test
    public void testDeleteUserById_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO deletedUserDTO = new UserDTO();
        deletedUserDTO.setUserId(userId);
        deletedUserDTO.setDeletionStatus(true);

        when(userService.softDeleteById(userId)).thenReturn(deletedUserDTO);

        // Act
        Object result = userRestController.deleteUserById(userId);

        // Assert
        assertEquals(deletedUserDTO, result);
        verify(userService, times(1)).softDeleteById(userId);
    }

}
