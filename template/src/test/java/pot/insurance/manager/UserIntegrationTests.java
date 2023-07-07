package pot.insurance.manager;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import pot.insurance.manager.controller.UserRestController;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.exception.UserNotFoundException;
import pot.insurance.manager.service.UserService;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RunWith(MockitoJUnitRunner.class)

public class UserIntegrationTests {
        
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;



        @Before
        public void setup() {
                MockitoAnnotations.openMocks(this);
                userService = mock(UserService.class);
                mockMvc = MockMvcBuilders.standaloneSetup(new UserRestController(userService)).build();
        }

        @Test
        public void saveUserReturnSuccessStatus() throws Exception {

        // Arrange
        UserDTO user = new UserDTO();
                user.setId(UUID.randomUUID());
                user.setFirstName("Sammy");
                user.setLastName("Sam");
                user.setSsn("123456789");
                user.setBirthday(Date.valueOf("1990-01-01"));
                user.setEmail("test@test.test");

        // Act
        when(userService.save(any(UserDTO.class))).thenReturn(user);

        // Assert
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.ssn", is(user.getSsn())))
                .andExpect(jsonPath("$.birthday", is(user.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService).save(any(UserDTO.class));
        }

        @Test
        public void saveUserReturnBadRequestStatus() throws Exception {

        // Arrange
        String invalidUser = "{\"first_name\": \"Sammy\", \"last_name\": \"Sam\", \"ssn\": \"123456789\", \"birthday\": \"1990-01-01\", \"email\": \"}";

        // Act & Assert
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).save(any(UserDTO.class));
}

        @Test
        public void findAllUsers() throws Exception{

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

        // Assert
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(user1.getId().toString())))
                .andExpect(jsonPath("$[0].firstName", is(user1.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(user1.getLastName())))
                .andExpect(jsonPath("$[0].ssn", is(user1.getSsn())))
                .andExpect(jsonPath("$[0].birthday", is(user1.getBirthday().getTime())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
        // User2
                .andExpect(jsonPath("$[1].id", is(user2.getId().toString())))
                .andExpect(jsonPath("$[1].firstName", is(user2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(user2.getLastName())))
                .andExpect(jsonPath("$[1].ssn", is(user2.getSsn())))
                .andExpect(jsonPath("$[1].birthday", is(user2.getBirthday().getTime())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())));
        verify(userService, times(1)).findAll();
        }



        @Test
        public void findUserById() throws Exception{

        // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO user = new UserDTO();
                user.setId(userId);
                user.setFirstName("Sammy");
                user.setLastName("Sam");
                user.setSsn("123456789");
                user.setBirthday(Date.valueOf("1990-01-01"));
                user.setEmail("test@test.test");

        // Act
        when(userService.findById(userId)).thenReturn(user);
        
        // Assert
        mockMvc.perform(get("/v1/users/{userId}", user.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.ssn", is(user.getSsn())))
                .andExpect(jsonPath("$.birthday", is(user.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService).findById(userId);
        }
        
        @Test
        public void notFindUserById() throws Exception{

        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        when(userService.findById(userId)).thenThrow(new UserNotFoundException("User not found by id: " + userId));
        // Assert
        mockMvc.perform(get("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(userService, never()).findById(userId);
        }

        @Test
        public void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO userBeforeUpdate = new UserDTO();
                userBeforeUpdate.setId(userId);
                userBeforeUpdate.setFirstName("Sammy");
                userBeforeUpdate.setLastName("Sam");
                userBeforeUpdate.setSsn("123456789");
                userBeforeUpdate.setBirthday(Date.valueOf("1990-01-01"));
                userBeforeUpdate.setEmail("test@test.test");
        
        UserDTO updatedUserDTO = new UserDTO();
                updatedUserDTO.setId(userId);
                updatedUserDTO.setFirstName("James");
                updatedUserDTO.setLastName("Json");
                updatedUserDTO.setSsn("12345789");
                updatedUserDTO.setBirthday(Date.valueOf("1990-01-01"));
                updatedUserDTO.setEmail("test@test.test");

        // Act
        when(userService.update(eq(userId), any(UserDTO.class))).thenReturn(updatedUserDTO);

        // Assert
        mockMvc.perform(put("/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userBeforeUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedUserDTO.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(updatedUserDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedUserDTO.getLastName())))
                .andExpect(jsonPath("$.ssn", is(updatedUserDTO.getSsn())))
                .andExpect(jsonPath("$.birthday", is(updatedUserDTO.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(updatedUserDTO.getEmail())));
        
        verify(userService, times(1)).update(eq(userId), any(UserDTO.class));
        }

        @Test
        public void testDeleteUser() throws Exception {
         // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO beforeDeletionUser = new UserDTO();
                beforeDeletionUser.setId(userId);
                beforeDeletionUser.setDeletionStatus(false);
        UserDTO deletedUser = new UserDTO();
                deletedUser.setId(userId);
                deletedUser.setDeletionStatus(true);

        // Act
        when(userService.softDeleteById(userId)).thenReturn(deletedUser);

        // Assert
        mockMvc.perform(delete("/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(beforeDeletionUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(deletedUser.getId().toString())))
                .andExpect(jsonPath("$.deletionStatus", is(true)));

        verify(userService, times(1)).softDeleteById(userId);
        }
}