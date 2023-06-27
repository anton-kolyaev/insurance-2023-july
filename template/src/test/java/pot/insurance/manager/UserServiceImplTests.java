package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import pot.insurance.manager.dao.UserRepository;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.service.UserServiceImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testSaveUser() {
        // Arrange
        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirst_name("Sammy");
            user.setLast_name("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userRepository.save(any(UserDTO.class))).thenReturn(user);
        // Act
        UserDTO savedUser = userService.save(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    public void testSaveUserDuplicateExistedUser(){

        UserDTO user = new UserDTO();
            user.setId(UUID.randomUUID());
            user.setFirst_name("Sammy");
            user.setLast_name("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userRepository.save(any(UserDTO.class))).thenReturn(user).thenReturn(null);

        // Act
        UserDTO savedUser1 = userService.save(user);
        UserDTO savedUser2 = userService.save(user);

        // Assert
        assertEquals(user, savedUser1);
        assertNull(savedUser2);
        verify(userRepository, times(2));
        userRepository.save(user);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<UserDTO> userList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDTO> fetchedUsers = userService.findAll();

        // Assert
        assertNotNull(fetchedUsers);
        assertEquals(userList, fetchedUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO user = new UserDTO();
            user.setId(userId);
            user.setFirst_name("Sammy");
            user.setLast_name("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO fetchedUser = userService.findById(userId);

        // Assert
        assertNotNull(fetchedUser);
        assertEquals(user, fetchedUser);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindNonExistingUserById() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findById(userId);
        });
        // Assert
        assertEquals("User not found for id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

}
