package at.downdrown.housekeeper.web.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @see JwtProvider
 * @see JwtProviderImpl
 * @see JwtToken
 * @author Manfred Huber
 */
@SpringBootTest
public class JwtProviderTest {

    private @Autowired JwtProvider jwtProvider;

    @Test
    public void shouldIssueToken() {

        JwtToken issuedToken = jwtProvider.issue(mockUserDetails());

        assertNotNull(issuedToken, "Should return an issued token");
        assertNotNull(issuedToken.getAccessToken(), "Should have an access_token set");
        assertNotNull(issuedToken.getRefreshToken(), "Should have an refresh_token set");
        assertNotNull(issuedToken.getExpiresIn(), "Should have an expiry time set");
    }

    @Test
    public void shouldVerifyToken() {

        JwtToken issuedToken = jwtProvider.issue(mockUserDetails());
        UserDetails verified = jwtProvider.verify(issuedToken.getAccessToken());

        assertNotNull(verified, "Should verify an issued token");
        assertNotNull(verified.getUsername(), "Should have a username set");
        assertEquals("user", verified.getUsername(), "Should match 'user' as username");
        assertEquals(issuedToken.getAccessToken(), verified.getPassword(), "Should have the access token set as password");
        assertNotNull(verified.getAuthorities(), "Should have authorities set");
    }

    @Test
    public void shouldRefreshToken() {

        // Issue a new token
        JwtToken issuedToken = jwtProvider.issue(mockUserDetails());

        // Refresh the access_token
        String refreshedAccessToken = jwtProvider.refresh(issuedToken.getRefreshToken());

        assertNotNull(refreshedAccessToken, "Should create a new access token");
        assertNotEquals("", refreshedAccessToken, "Should create a non-empty access token");
    }

    /** Generates a mocked {@link UserDetails} object. */
    private static UserDetails mockUserDetails() {
        return new User(
            "user",
            "my-mock-password",
            Set.of(new SimpleGrantedAuthority("auth.1"), new SimpleGrantedAuthority("auth.2")));
    }
}
