package at.downdrown.housekeeper.repository;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.model.Role;
import at.downdrown.housekeeper.model.User;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see User
 * @see UserRepository
 * @author Manfred Huber
 */
@DataJpaTest
public class UserRepositoryTest extends TestBase {

    private final UserRepository repository;

    @Autowired
    public UserRepositoryTest(UserRepository repository) {
        this.repository = repository;
    }

    @Test
    public void shouldInsertUser() {

        final ZonedDateTime lastLogin = ZonedDateTime.now();

        // given
        User user = new User();
        user.setUsername("max");
        user.setFirstName("Maximilian");
        user.setLastName("Mustermann");
        user.setRole(Role.USER);
        user.setLastLogin(lastLogin);

        // when
        repository.save(user);
        repository.flush();

        // then
        User max = repository.findByUsername("max");
        assertThat(max)
            .isNotNull()
            .extracting(
                User::getUsername,
                User::getFirstName,
                User::getLastName,
                User::getRole,
                User::getLastLogin)
            .containsExactly(
                "max",
                "Maximilian",
                "Mustermann",
                Role.USER,
                lastLogin);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadUser() {
        assertThat(repository.findByUsername("admin")).isNotNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldReadUsers() {
        assertThat(repository.findAll()).hasSize(3);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldUpdateUser() {
        User user = repository.findByUsername("admin");
        assertThat(user)
            .isNotNull()
            .extracting(
                User::getUsername,
                User::getFirstName,
                User::getLastName,
                User::getRole,
                User::getLastLogin)
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

        repository.save(user);
        repository.flush();

        assertThat(user)
            .extracting(
                User::getUsername,
                User::getFirstName,
                User::getLastName,
                User::getRole,
                User::getLastLogin)
            .containsExactly(
                "admin",
                "a-new-firstname",
                "a-new-lastname",
                Role.GUEST,
                login);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldDeleteUser() {
        assertThat(repository.findByUsername("admin")).isNotNull();
        repository.deleteByUsername("admin");
        assertThat(repository.findByUsername("admin")).isNull();
    }
}
