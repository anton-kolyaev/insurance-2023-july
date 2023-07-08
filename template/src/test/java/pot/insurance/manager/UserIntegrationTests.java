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
            try (final AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
                userService = mock(UserService.class);
                mockMvc = MockMvcBuilders.standaloneSetup(new UserRestController(userService)).build();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Test
        public void saveUserReturnSuccessStatus() throws Exception {

        // Arrange
        UserDTO userDTO = new UserDTO();
            userDTO.setId(UUID.randomUUID());
            userDTO.setFirstName("Sammy");
            userDTO.setLastName("Sam");
            userDTO.setSsn("123456789");
            userDTO.setBirthday(Date.valueOf("1990-01-01"));
            userDTO.setEmail("test@test.test");

        // Act
        when(userService.save(any(UserDTO.class))).thenReturn(userDTO);

        // Assert
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(userDTO.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.ssn", is(userDTO.getSsn())))
                .andExpect(jsonPath("$.birthday", is(userDTO.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail())));

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

        // Assert
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId", is(userDTO1.getId().toString())))
                .andExpect(jsonPath("$[0].firstName", is(userDTO1.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(userDTO1.getLastName())))
                .andExpect(jsonPath("$[0].ssn", is(userDTO1.getSsn())))
                .andExpect(jsonPath("$[0].birthday", is(userDTO1.getBirthday().getTime())))
                .andExpect(jsonPath("$[0].email", is(userDTO1.getEmail())))
        // User2
                .andExpect(jsonPath("$[1].userId", is(userDTO2.getId().toString())))
                .andExpect(jsonPath("$[1].firstName", is(userDTO2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(userDTO2.getLastName())))
                .andExpect(jsonPath("$[1].ssn", is(userDTO2.getSsn())))
                .andExpect(jsonPath("$[1].birthday", is(userDTO2.getBirthday().getTime())))
                .andExpect(jsonPath("$[1].email", is(userDTO2.getEmail())));

        verify(userService, times(1)).findAll();
        }



        @Test
        public void findUserById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
                userDTO.setId(id);
                userDTO.setFirstName("Sammy");
                userDTO.setLastName("Sam");
                userDTO.setSsn("123456789");
                userDTO.setBirthday(Date.valueOf("1990-01-01"));
                userDTO.setEmail("test@test.test");

        // Act
        when(userService.findById(id)).thenReturn(userDTO);
        
        // Assert
        mockMvc.perform(get("/v1/users/{id}", userDTO.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(userDTO.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.ssn", is(userDTO.getSsn())))
                .andExpect(jsonPath("$.birthday", is(userDTO.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail())));

        verify(userService).findById(id);
        }
        
        @Test
        public void notFindUserById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        when(userService.findById(id)).thenThrow(new DataNotFoundException("User not found by id: " + id));
        // Assert
        mockMvc.perform(get("/users/{user_id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        
        verify(userService, never()).findById(id);
        }
}