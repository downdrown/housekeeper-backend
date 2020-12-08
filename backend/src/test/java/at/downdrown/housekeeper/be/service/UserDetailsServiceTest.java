package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.be.mock.MockUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private @Autowired PasswordEncoder passwordEncoder;

    @Test
    void shouldCheckForCorrectImplementation() {
        assertThat(userDetailsService).isInstanceOf(MockUserDetailsService.class);
    }

    @Test
    void shouldReturnUserDetails() {
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        assertThat(user)
            .isNotNull()
            .extracting(
                UserDetails::getUsername,
                UserDetails::isAccountNonExpired,
                UserDetails::isAccountNonLocked,
                UserDetails::isCredentialsNonExpired,
                UserDetails::isEnabled)
            .containsExactly(
                "admin",
                true,
                true,
                true,
                true);
        assertThat(passwordEncoder.matches("password", user.getPassword())).isTrue();
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("a-user-that-doesnt-exist"));
    }
}
