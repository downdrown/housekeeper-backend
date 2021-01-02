package at.downdrown.housekeeper.be.repository;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.Gender;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @see Credential
 * @see CredentialRepository
 * @author Manfred Huber
 */
@DataJpaTest
public class CredentialRepositoryTest extends TestBase {

    private @Autowired UserRepository userRepository;
    private @Autowired CredentialRepository credentialRepository;

    @BeforeEach
    public void insertUserWithoutCredential() {

        User user = new User();
        user.setUsername("max");
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setGender(Gender.MALE);
        user.setRole(Role.GUEST);

        userRepository.save(user);
    }

    @Test
    public void shouldInsertCredential() {

        User user = userRepository.findByUsername("max");
        assertThat(user).isNotNull();

        Credential credential = new Credential();
        credential.setUser(user);
        credential.setPassword("a-password");

        credentialRepository.save(credential);
        credentialRepository.flush();

        assertThat(credentialRepository.findByUsername("max"))
            .isNotNull()
            .extracting(Credential::getId, Credential::getUser, Credential::getPassword)
            .containsExactly(1000L, user, "a-password");
    }

    @Test
    public void shouldFailOnInsertCredential() {

        assertThrows(DataIntegrityViolationException.class, () -> {

            // non-existing user
            User user = new User();
            user.setId(123L);

            Credential credential = new Credential();
            credential.setUser(user);
            credential.setPassword("a-password");

            credentialRepository.save(credential);
            credentialRepository.flush();

        }, "Should fail with DataIntegrityViolationException");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadCredentialByUsername() {
        assertThat(credentialRepository.findByUsername("admin")).isNotNull();
        assertThat(credentialRepository.findByUsername("user")).isNotNull();
        assertThat(credentialRepository.findByUsername("guest")).isNotNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadCredentals() {
        assertThat(credentialRepository.findAll()).hasSize(3);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldUpdateCredential() {

        User user = userRepository.findByUsername("admin");
        Credential credential = credentialRepository.findByUsername("admin");

        assertThat(credential)
            .isNotNull()
            .extracting(Credential::getId, Credential::getUser, Credential::getPassword)
            .containsExactly(10L, user, "$2a$10$umThkjstS0ZuSaUmvyxkfucpi2wHH3q6y/8U.Sit8SNjVOAPeMjgi");

        credential.setPassword("a-new-password");

        credentialRepository.save(credential);
        credentialRepository.flush();

        assertThat(credentialRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(Credential::getId, Credential::getUser, Credential::getPassword)
            .containsExactly(10L, user, "a-new-password");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldDeleteCredential() {
        assertThat(credentialRepository.findByUsername("admin")).isNotNull();
        credentialRepository.delete(credentialRepository.findByUsername("admin"));
        assertThat(credentialRepository.findByUsername("admin")).isNull();
    }
}
