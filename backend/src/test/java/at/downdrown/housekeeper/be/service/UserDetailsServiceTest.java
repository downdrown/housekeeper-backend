package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @see UserDetailsService
 * @see UserDetailsServiceImpl
 * @author Manfred Huber
 */
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDetailsServiceTest extends TestBase {

    private @Autowired UserDetailsService userDetailsService;

    @Test
    void shouldCheckForCorrectImplementation() {
        assertThat(userDetailsService).isInstanceOf(UserDetailsServiceImpl.class);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_CREDENTIALS_SQL)
    void shouldReturnUserDetails() {
        assertThat(userDetailsService.loadUserByUsername("admin"))
            .isNotNull()
            .extracting(
                UserDetails::getUsername,
                UserDetails::getPassword,
                UserDetails::isAccountNonExpired,
                UserDetails::isAccountNonLocked,
                UserDetails::isCredentialsNonExpired,
                UserDetails::isEnabled)
            .containsExactly(
                "admin",
                "a-password",
                true,
                true,
                true,
                true);
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("a-user-that-doesnt-exist"));
    }
}
