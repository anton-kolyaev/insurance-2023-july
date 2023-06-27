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
        
                MockitoAnnotations.openMocks(this);
                userService = mock(UserService.class);
                mockMvc = MockMvcBuilders.standaloneSetup(new UserRestController(userService)).build();
        }


        @Test
        public void saveUserReturnSuccessStatus() throws Exception {

        // Arrange
        UserDTO user = new UserDTO();
        user.setId(UUID.randomUUID());
        user.setFirst_name("Sammy");
        user.setLast_name("Sam");
        user.setSsn("123456789");
        user.setBirthday(Date.valueOf("1990-01-01"));
        user.setEmail("test@test.test");
        user.setUsername("test_sam");

        // Act
        when(userService.save(any(UserDTO.class))).thenReturn(user);

        // Assert
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.first_name", is(user.getFirst_name())))
                .andExpect(jsonPath("$.last_name", is(user.getLast_name())))
                .andExpect(jsonPath("$.ssn", is(user.getSsn())))
                .andExpect(jsonPath("$.birthday", is(user.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));

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
        user1.setFirst_name("Kenny");
        user1.setLast_name("Martin");
        user1.setSsn("123456789");
        user1.setBirthday(Date.valueOf("1990-01-01"));
        user1.setEmail("test@test.com");
        user1.setUsername("test_kenny");

        UserDTO user2 = new UserDTO();
                user2.setId(UUID.randomUUID());
                user2.setFirst_name("Jane");
                user2.setLast_name("Smith");
                user2.setSsn("987654321");
                user2.setBirthday(Date.valueOf("1990-01-01"));
                user2.setEmail("test@Test2.com");
                user2.setUsername("test_jane");

        List<UserDTO> userList = List.of(user1, user2);
        // Act
        when(userService.findAll()).thenReturn(userList);

        // Assert
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(user1.getId().toString())))
                .andExpect(jsonPath("$[0].first_name", is(user1.getFirst_name())))
                .andExpect(jsonPath("$[0].last_name", is(user1.getLast_name())))
                .andExpect(jsonPath("$[0].ssn", is(user1.getSsn())))
                .andExpect(jsonPath("$[0].birthday", is(user1.getBirthday().getTime())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
        // User2
                .andExpect(jsonPath("$[1].id", is(user2.getId().toString())))
                .andExpect(jsonPath("$[1].first_name", is(user2.getFirst_name())))
                .andExpect(jsonPath("$[1].last_name", is(user2.getLast_name())))
                .andExpect(jsonPath("$[1].ssn", is(user2.getSsn())))
                .andExpect(jsonPath("$[1].birthday", is(user2.getBirthday().getTime())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())));

        verify(userService).findAll();
        }



        @Test
        public void findUserById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();
        UserDTO user = new UserDTO();
                user.setId(id);
                user.setFirst_name("Sammy");
                user.setLast_name("Sam");
                user.setSsn("123456789");
                user.setBirthday(Date.valueOf("1990-01-01"));
                user.setEmail("test@test.test");
                user.setUsername("test_sam");

        // Act
        when(userService.findById(id)).thenReturn(user);
        
        // Assert
        mockMvc.perform(get("/v1/users/{id}", user.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.first_name", is(user.getFirst_name())))
                .andExpect(jsonPath("$.last_name", is(user.getLast_name())))
                .andExpect(jsonPath("$.ssn", is(user.getSsn())))
                .andExpect(jsonPath("$.birthday", is(user.getBirthday().getTime())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));

        verify(userService).findById(id);
        }
        
        @Test
        public void notFindUserById() throws Exception{

        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        when(userService.findById(id)).thenReturn(null);

        // Assert
        mockMvc.perform(get("/v1/users/{id}", id ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN+";charset=ISO-8859-1"));
                
        verify(userService).findById(id);
                
        }
}
