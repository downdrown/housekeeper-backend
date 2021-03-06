package at.downdrown.housekeeper.web.rest;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.Gender;
import at.downdrown.housekeeper.api.Permission;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.be.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see at.downdrown.housekeeper.be.model.User
 * @see at.downdrown.housekeeper.be.repository.UserRepository
 * @see UserController
 * @author Manfred Huber
 */
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithUserDetails("admin")
public class UserControllerTest extends TestBase {

    private @Autowired MockMvc mockMvc;
    private @Autowired ObjectMapper objectMapper;

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldCreateUser() throws Exception {

        UserDTO user = new UserDTO();
        user.setUsername("maxi");
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setGender(Gender.MALE);
        user.setRole(Role.GUEST);
        user.setRegistrationPassword("a-password");

        mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(jsonPath("$.username").value("maxi"))
            .andExpect(jsonPath("$.firstName").value("Max"))
            .andExpect(jsonPath("$.lastName").value("Mustermann"))
            .andExpect(jsonPath("$.gender").value("MALE"))
            .andExpect(jsonPath("$.role").value("GUEST"))
            .andExpect(jsonPath("$.registrationPassword").doesNotExist());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadUser() throws Exception {
        mockMvc.perform(get("/user/admin"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.firstName").value("Admin"))
            .andExpect(jsonPath("$.lastName").value("Admin"))
            .andExpect(jsonPath("$.gender").value("FEMALE"))
            .andExpect(jsonPath("$.role").value("ADMIN"))
            .andExpect(jsonPath("$.registrationPassword").doesNotExist());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldFailOnReadUser() throws Exception {
        mockMvc.perform(get("/user/a-non-existent-user"))
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadUsers() throws Exception {
        mockMvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))

            .andExpect(jsonPath("$[0].username").value("admin"))
            .andExpect(jsonPath("$[0].firstName").value("Admin"))
            .andExpect(jsonPath("$[0].lastName").value("Admin"))
            .andExpect(jsonPath("$[0].gender").value("FEMALE"))
            .andExpect(jsonPath("$[0].role").value("ADMIN"))
            .andExpect(jsonPath("$.registrationPassword").doesNotExist())

            .andExpect(jsonPath("$[1].username").value("guest"))
            .andExpect(jsonPath("$[1].firstName").value("Guest"))
            .andExpect(jsonPath("$[1].lastName").value("Guest"))
            .andExpect(jsonPath("$[1].gender").value("MALE"))
            .andExpect(jsonPath("$[1].role").value("GUEST"))
            .andExpect(jsonPath("$.registrationPassword").doesNotExist())

            .andExpect(jsonPath("$[2].username").value("user"))
            .andExpect(jsonPath("$[2].firstName").value("User"))
            .andExpect(jsonPath("$[2].lastName").value("User"))
            .andExpect(jsonPath("$[2].gender").value("MALE"))
            .andExpect(jsonPath("$[2].role").value("USER"))
            .andExpect(jsonPath("$.registrationPassword").doesNotExist());
    }

    @Test
    @WithMockUser
    public void shouldReadUsersWithNoContent() throws Exception {
        mockMvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(content().string("[]"));
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldUpdateUser() throws Exception {

        UserDTO user = new UserDTO();
        user.setId(10L);
        user.setUsername("admin");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setGender(Gender.MALE);
        user.setRole(Role.USER);

        mockMvc.perform(put("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.firstName").value("Firstname"))
            .andExpect(jsonPath("$.lastName").value("Lastname"))
            .andExpect(jsonPath("$.gender").value("MALE"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(authorities = Permission.UPDATE_USER)
    public void shouldFailOnUpdateUser() throws Exception {

        User user = new User();
        user.setUsername("admin");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setRole(Role.USER);

        mockMvc.perform(put("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldChangePassword() throws Exception {
        mockMvc.perform(put("/user/admin/password")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content("{\"currentPassword\":\"password\", \"newPassword\":\"a-new-password\"}"))
            .andExpect(status().isOk());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldFailOnChangePassword() throws Exception {
        mockMvc.perform(put("/user/admin/password")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content("{\"currentPassword\":\"a-wrong-password\", \"newPassword\":\"a-new-password\"}"))
            .andExpect(status().isUnauthorized());
    }
}
