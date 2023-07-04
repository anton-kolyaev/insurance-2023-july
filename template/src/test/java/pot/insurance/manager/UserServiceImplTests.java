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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.exception.user.exceptions.UserNotFoundException;
import pot.insurance.manager.exception.user.exceptions.UserWrongCredentialsInput;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.service.UserServiceImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
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
            user.setUsername("test_sam");
            

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO savedUser = userService.save(userDTO);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testSaveUserWithDuplicateUsername(){
        // Arrange
        UserDTO user = new UserDTO();
            user.setUserId(UUID.randomUUID());
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.com");
            user.setUsername("test_sam");

        // Act
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Assert
        assertThrows(UserWrongCredentialsInput.class, () -> userService.save(user));
        assertNull(assertThrows(UserWrongCredentialsInput.class, () -> userService.save(user)).getMessage());
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
            user.setUsername("test_sam");

        // Act
        when(userRepository.save(any(User.class))).thenReturn(user).thenReturn(null);
        UserDTO savedUser1 = userService.save(userDTO);
        UserDTO savedUser2 = userService.save(userDTO);

        // Assert
        assertEquals(user.getId(), savedUser1.getUserId());
        assertNull(savedUser2);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
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
        User user = new User();
            user.setId(userId);
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.test");
            user.setUsername("test_sam");

        when(userRepository.findByIdNotDeletedUser(userId, false)).thenReturn(Optional.of(user));

        // Act
        UserDTO fetchedUser = userService.findById(userId);

        // Assert
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getUserId());
        verify(userRepository, times(1)).findByIdNotDeletedUser(userId, false);
    }

    @Test
    public void testFindNonExistingUserThtowException() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        when(userRepository.findByIdNotDeletedUser(userId, false)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findByIdNotDeletedUser(userId, false);
    }

    @Test
    public void testUpdateUserSuccessfullyById() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        UUID userId = UUID.randomUUID();
        User user = new User();
            user.setId(userId);
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.com");
            user.setUsername("test_sam");
            user.setDeletionStatus(false);
        
        // Act
        when(userRepository.findByIdNotDeletedUser(userId, false)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO updatedUser = userService.update(userId, userDTO);

        // Assert
        assertEquals(userId, updatedUser.getUserId());
        verify(userRepository, times(1)).findByIdNotDeletedUser(userId, false);
        verify(userRepository, times(1)).save(any(User.class));
        
    }

    @Test
    public void testUpdateUserThrowNotFoundException(){
        // Arrange
        UserDTO userDTO = new UserDTO();
        UUID userId = UUID.randomUUID();

        // Act
        when(userRepository.findByIdNotDeletedUser(userId, false)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.update(userId, userDTO));
        verify(userRepository, times(1)).findByIdNotDeletedUser(userId, false);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testUpdateUserThrowUserWrongCredentialsExcwption(){
        // Arrange
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        User user = new User();
        user.setId(id);
        
        // Act
        when(userRepository.findByIdNotDeletedUser(id, false)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        // Assert
        assertThrows(UserWrongCredentialsInput.class, () -> userService.update(id, userDTO));
        verify(userRepository, times(1)).findByIdNotDeletedUser(id, false);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSoftDeleteUserByIdSuccessfully(){
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
            user.setId(userId);
            user.setFirstName("Sammy");
            user.setLastName("Sam");
            user.setSsn("123456789");
            user.setBirthday(Date.valueOf("1990-01-01"));
            user.setEmail("test@test.com");
            user.setUsername("test_sam");
            user.setDeletionStatus(false);

        // Act
        when(userRepository.findByIdNotDeletedUser(userId, false)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserDTO result = userService.softDeleteById(userId);

        // Assert
        assertEquals(userId, result.getUserId());
        assertTrue(result.isDeletionStatus());
        verify(userRepository, times(1)).findByIdNotDeletedUser(userId, false);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSoftDeleteByIdThrowUserNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        when(userRepository.findByIdNotDeletedUser(id, false)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.softDeleteById(id));
        verify(userRepository, times(1)).findByIdNotDeletedUser(id, false);
        verify(userRepository, never()).save(any());
    }
}
