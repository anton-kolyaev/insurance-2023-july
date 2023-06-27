package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pot.insurance.manager.controller.UserRestController;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.service.UserService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerTests {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRestController = new UserRestController(userService);
    }

    @Test
    public void testSaveUser_Success() {
        // Arrange
        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirst_name("Sammy");
            user.setLast_name("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userService.save(user)).thenReturn(user);

        // Act
        ResponseEntity<UserDTO> response = userRestController.saveUser(user);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testSaveUser_BadRequest() {
        // Arrange
        UserDTO userDTO = null;

        // Act
        ResponseEntity<UserDTO> response = userRestController.saveUser(userDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        List<UserDTO> userDTOList = new ArrayList<>();
        // ... Add some userDTO objects to the list

        when(userService.findAll()).thenReturn(userDTOList);

        // Act
        ResponseEntity<Object> response = userRestController.findAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOList, response.getBody());
    }

    @Test
    public void testFindUserById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();

        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirst_name("Sammy");
            user.setLast_name("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userService.findById(id)).thenReturn(user);

        // Act
        ResponseEntity<Object> response = userRestController.findUserById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testFindUserById_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(userService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = userRestController.findUserById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found!", response.getBody());
    }
}
