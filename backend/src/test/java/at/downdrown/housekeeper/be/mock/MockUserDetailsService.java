package at.downdrown.housekeeper.be.mock;

import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.service.GrantedAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Mock for the {@link UserDetailsService} that can be shared across all modules for
 * development and unit testing purposes.
 *
 * @author Manfred Huber
 */
@Primary
@Service
public class MockUserDetailsService implements UserDetailsService {

    private static final String DEFAULT_PASSWORD = "password";
    private static final Map<String, Role> USER_ROLE_MAPPING = Map.of(
        "admin", Role.ADMIN,
        "user", Role.USER,
        "guest", Role.GUEST
    );

    private final PasswordEncoder passwordEncoder;
    private final GrantedAuthorityService grantedAuthorityService;

    @Autowired
    public MockUserDetailsService(
        final PasswordEncoder passwordEncoder,
        final GrantedAuthorityService grantedAuthorityService) {
        this.passwordEncoder = passwordEncoder;
        this.grantedAuthorityService = grantedAuthorityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }
        if (!USER_ROLE_MAPPING.containsKey(username)) {
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }

        return new User(
            username,
            passwordEncoder.encode(DEFAULT_PASSWORD),
            grantedAuthorityService.calculateAuthoritiesForRole(USER_ROLE_MAPPING.get(username)));
    }
}
