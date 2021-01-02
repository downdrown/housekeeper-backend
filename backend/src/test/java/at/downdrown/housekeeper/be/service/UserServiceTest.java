package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.Gender;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.api.service.UserService;
import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.CredentialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest extends TestBase {

    private @Autowired CredentialRepository credentialRepository;
    private @Autowired PasswordEncoder passwordEncoder;
    private @Autowired UserService userService;

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldRegisterUser() {

        UserDTO user = new UserDTO();
        user.setUsername("maxi");
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setGender(Gender.MALE);
        user.setRole(Role.USER);
        user.setRegistrationPassword("a-password");

        assertThat(userService.register(user)).isNotNull()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getGender, UserDTO::getRole)
            .containsExactly(1000L, "maxi", "Max", "Mustermann", Gender.MALE, Role.USER);

        Credential credential = credentialRepository.findByUsername("maxi");
        assertThat(credential).isNotNull();
        assertThat(passwordEncoder.matches("a-password", credential.getPassword())).isTrue();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldFindUserByUsername() {
        assertThat(userService.findByUsername("admin"))
            .isNotNull()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getGender, UserDTO::getRole)
            .containsExactly(10L, "admin", "Admin", "Admin", Gender.FEMALE, Role.ADMIN);
    }

    @Test
    public void shouldFindNullByUsername() {
        assertThat(userService.findByUsername("a-nonexistet-user")).isNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldFindAllUsers() {
        assertThat(userService.findAll()).hasSize(3);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldUpdateUser() {

        UserDTO user = userService.findByUsername("admin");
        assertThat(user)
            .isNotNull()
            .extracting(
                UserDTO::getUsername,
                UserDTO::getFirstName,
                UserDTO::getLastName,
                UserDTO::getGender,
                UserDTO::getRole,
                UserDTO::getLastLogin)
            .containsExactly(
                "admin",
                "Admin",
                "Admin",
                Gender.FEMALE,
                Role.ADMIN,
                null);

        user.setFirstName("a-new-firstname");
        user.setLastName("a-new-lastname");
        user.setGender(Gender.MALE);
        user.setRole(Role.GUEST);
        user.setLastLogin(ZonedDateTime.now());

        assertThat(userService.update(user))
            .extracting(
                UserDTO::getUsername,
                UserDTO::getFirstName,
                UserDTO::getLastName,
                UserDTO::getGender,
                UserDTO::getRole,
                UserDTO::getLastLogin)
            .containsExactly(
                "admin",
                "a-new-firstname",
                "a-new-lastname",
                Gender.MALE,
                Role.GUEST,
                null);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldDeleteUserByUsername() {
        assertThat(userService.findByUsername("admin")).isNotNull();
        userService.delete("admin");
        assertThat(userService.findByUsername("admin")).isNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldChangeUserPassword() {
        userService.changePassword("admin", "password", "new-password!");
        Credential credential = credentialRepository.findByUsername("admin");
        assertTrue(passwordEncoder.matches("new-password!", credential.getPassword()), "Should update the user's password");
        assertThat(credential.getLastChange())
            .withFailMessage("Should have an 'lastChange' date that is just now")
            .isNotNull()
            .isBetween(ZonedDateTime.now().minusMinutes(1), ZonedDateTime.now());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    public void shouldChangeUserPasswordAndFailByMismatchingPassword() {
        assertThrows(
            IllegalArgumentException.class,
            () -> userService.changePassword("admin", "a-non-matching-password", "new-password!"),
            "Should fail with a non-matching password");
    }
}
