package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.api.service.UserService;
import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.CredentialRepository;
import at.downdrown.housekeeper.be.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest extends TestBase {

    private @Autowired UserService userService;
    private @Autowired UserRepository userRepository;
    private @Autowired CredentialRepository credentialRepository;

    @Test
    void shouldRegisterUser() {

        UserDTO user = new UserDTO();
        user.setUsername("maxi");
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setRole(Role.USER);
        user.setRegistrationPassword("a-password");

        assertThat(userService.register(user)).isNotNull()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getRole)
            .containsExactly(1000L, "maxi", "Max", "Mustermann", Role.USER);

        assertThat(credentialRepository.findByUsername("maxi"))
            .isNotNull()
            .extracting(Credential::getPassword, Credential::getSalt)
            .containsExactly("a-password", "a-salt");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    void shouldFindUserByUsername() {
        assertThat(userService.findByUsername("admin"))
            .isNotNull()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getRole)
            .containsExactly(10L, "admin", "Admin", "Admin", Role.ADMIN);
    }

    @Test
    void shouldFindNullByUsername() {
        assertThat(userService.findByUsername("a-nonexistet-user")).isNull();
    }

    @Test
    void shouldFindAllUsers() {
        assertThat(userService.findAll()).hasSize(3);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    void shouldUpdateUser() {

        UserDTO user = userService.findByUsername("admin");
        assertThat(user)
            .isNotNull()
            .extracting(
                UserDTO::getUsername,
                UserDTO::getFirstName,
                UserDTO::getLastName,
                UserDTO::getRole,
                UserDTO::getLastLogin)
            .containsExactly(
                "admin",
                "Admin",
                "Admin",
                Role.ADMIN,
                null);

        ZonedDateTime login = ZonedDateTime.now();

        user.setFirstName("a-new-firstname");
        user.setLastName("a-new-lastname");
        user.setRole(Role.GUEST);
        user.setLastLogin(login);

        assertThat(userService.update(user))
            .extracting(
                UserDTO::getUsername,
                UserDTO::getFirstName,
                UserDTO::getLastName,
                UserDTO::getRole,
                UserDTO::getLastLogin)
            .containsExactly(
                "admin",
                "a-new-firstname",
                "a-new-lastname",
                Role.GUEST,
                login);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldDeleteUserByUsername() {
        assertThat(userService.findByUsername("admin")).isNotNull();
        userService.delete("admin");
        assertThat(userService.findByUsername("admin")).isNull();
    }
}
