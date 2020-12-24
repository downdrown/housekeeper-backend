package at.downdrown.housekeeper.web.security.jwt;

import at.downdrown.housekeeper.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

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
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtProviderTest extends TestBase {

    private @Autowired JwtProvider jwtProvider;
    private @Autowired UserDetailsService userDetailsService;

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldIssueToken() {

        UserDetails user = userDetailsService.loadUserByUsername("user");
        JwtToken issuedToken = jwtProvider.issue(user);

        assertNotNull(issuedToken, "Should return an issued token");
        assertNotNull(issuedToken.getAccessToken(), "Should have an access_token set");
        assertNotNull(issuedToken.getRefreshToken(), "Should have an refresh_token set");
        assertNotNull(issuedToken.getExpiresIn(), "Should have an expiry time set");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldVerifyToken() {

        UserDetails user = userDetailsService.loadUserByUsername("user");
        JwtToken issuedToken = jwtProvider.issue(user);
        UserDetails verified = jwtProvider.verify(issuedToken.getAccessToken());

        assertNotNull(verified, "Should verify an issued token");
        assertNotNull(verified.getUsername(), "Should have a username set");
        assertEquals("user", verified.getUsername(), "Should match 'user' as username");
        assertEquals(issuedToken.getAccessToken(), verified.getPassword(), "Should have the access token set as password");
        assertNotNull(verified.getAuthorities(), "Should have authorities set");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldRefreshToken() {

        // Issue a new token
        UserDetails user = userDetailsService.loadUserByUsername("user");
        JwtToken issuedToken = jwtProvider.issue(user);

        // Refresh the access_token
        String refreshedAccessToken = jwtProvider.refresh(issuedToken.getRefreshToken());

        assertNotNull(refreshedAccessToken, "Should create a new access token");
        assertNotEquals("", refreshedAccessToken, "Should create a non-empty access token");
    }
}
