package pot.insurance.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.mapper.UserMapper;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.service.UserServiceImpl;
import pot.insurance.manager.service.UserService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Before
    public void setup() {
        try (final AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            this.userService = new UserServiceImpl(this.userRepository, this.userMapper);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testSaveUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        User user = new User();
            user.setId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");


        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO savedUserDTO = userService.save(userDTO);

        // Assert
        assertNotNull(savedUserDTO);
        assertEquals(user.getId(), savedUserDTO.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testSaveUserWithDuplicateUsername(){
        // Arrange
        UserDTO userDTO = new UserDTO();
            userDTO.setId(UUID.randomUUID());
            userDTO.setFirstName("Sammy");
            userDTO.setLastName("Sam");
            userDTO.setSsn("123456789");
            userDTO.setBirthday(Date.valueOf("1990-01-01"));
            userDTO.setEmail("test@test.com");

        // Act
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Assert
        assertThrows(DataValidationException.class, () -> userService.save(userDTO));
        assertNull(assertThrows(DataValidationException.class, () -> userService.save(userDTO)).getMessage());
        verify(userRepository, times(2)).save(any(User.class));
        
    }
    

    @Test
    public void testSaveUserDuplicateExistedUser(){

        // Arrange
        UserDTO userDTO = new UserDTO();
        User user = new User();
            user.setId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");

        // Act
        when(userRepository.save(any(User.class))).thenReturn(user).thenReturn(null);
        UserDTO savedUser1DTO = userService.save(userDTO);
        UserDTO savedUser2DTO = userService.save(userDTO);

        // Assert
        assertEquals(user.getId(), savedUser1DTO.getId());
        assertNull(savedUser2DTO);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDTO> fetchedUserDTOS = userService.findAll();

        // Assert
        assertNotNull(fetchedUserDTOS);
        assertEquals(userList, fetchedUserDTOS);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
            user.setId(userId);
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO fetchedUserDTO = userService.findById(userId);

        // Assert
        assertNotNull(fetchedUserDTO);
        assertEquals(user.getId(), fetchedUserDTO.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindNonExistingUserThtowException() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assert
        assertThrows(DataNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

}
