package at.downdrown.housekeeper.web.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static at.downdrown.housekeeper.api.exception.ExceptionUtils.throwIf;

/**
 * @see JwtProvider
 * @see JwtToken
 * @author Manfred Huber
 */
@Service
public class JwtProviderImpl implements JwtProvider {

    private static final String PERMISSIONS_CLAIM = "permissions";

    private final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

    private final UserDetailsService userDetailsService;
    private final JwtConfiguration jwtConfiguration;
    private final Algorithm tokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;

    @Autowired
    public JwtProviderImpl(final UserDetailsService userDetailsService,
                           final JwtConfiguration jwtConfiguration) {
        this.userDetailsService = userDetailsService;
        this.jwtConfiguration = jwtConfiguration;

        // set up used algoritms
        this.tokenAlgorithm = Algorithm.HMAC512(jwtConfiguration.getTokenSigningKey());
        this.refreshTokenAlgorithm = Algorithm.HMAC256(jwtConfiguration.getTokenSigningKey());
    }

    @Override
    public JwtToken issue(UserDetails userDetails) {

        final ZonedDateTime issuedAt = ZonedDateTime.now();

        final String accessToken = issueAccessToken(
            issuedAt,
            userDetails.getUsername(),
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));

        final String refreshToken = issueRefreshToken(
            issuedAt,
            userDetails.getUsername());

        return JwtToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(jwtConfiguration.getTokenExpirationTime())
            .build();
    }

    @Override
    public UserDetails verify(String accessToken) throws BadCredentialsException, CredentialsExpiredException {

        // Try to decode the given access token
        final DecodedJWT decodedToken = decode(tokenAlgorithm, accessToken);

        // Invalidate the token if it has been issued before the application start
        throwIf(tokenWasIssuedBeforeApplicationStart(decodedToken), () -> new CredentialsExpiredException("credentials.expired"));

        final String username = decodedToken.getSubject();
        final String[] permissions = decodedToken.getClaim(PERMISSIONS_CLAIM).asArray(String.class);

        return new User(username, accessToken, AuthorityUtils.createAuthorityList(permissions == null ? new String[]{} : permissions));
    }

    @Override
    public String refresh(String refreshToken) throws BadCredentialsException, CredentialsExpiredException {

        // Try to decode the given access token
        final DecodedJWT decodedToken = decode(refreshTokenAlgorithm, refreshToken);

        // Invalidate the token if it has been issued before the application start
        throwIf(tokenWasIssuedBeforeApplicationStart(decodedToken), () -> new CredentialsExpiredException("credentials.expired"));

        // Reload the UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(decodedToken.getSubject());

        // Issue a new token
        return issueAccessToken(
            ZonedDateTime.now(),
            userDetails.getUsername(),
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
    }

    @Override
    public String getHeader(String accessToken) throws BadCredentialsException, CredentialsExpiredException {
        return decode(tokenAlgorithm, accessToken).getHeader();
    }

    @Override
    public String getPayload(String accessToken) throws BadCredentialsException, CredentialsExpiredException {
        return decode(tokenAlgorithm, accessToken).getPayload();
    }

    @Override
    public String getSignature(String accessToken) throws BadCredentialsException, CredentialsExpiredException {
        return decode(tokenAlgorithm, accessToken).getSignature();
    }

    private String issueAccessToken(ZonedDateTime issuedAt, String subject, String[] permissions) {
        final ZonedDateTime tokenexpiresAt = issuedAt.plusSeconds(jwtConfiguration.getTokenExpirationTime());
        return JWT.create()
            .withSubject(subject)
            .withArrayClaim(PERMISSIONS_CLAIM, permissions)
            .withIssuer(jwtConfiguration.getTokenIssuer())
            .withIssuedAt(Date.from(issuedAt.toInstant()))
            .withExpiresAt(Date.from(tokenexpiresAt.toInstant()))
            .sign(tokenAlgorithm);
    }

    private String issueRefreshToken(ZonedDateTime issuedAt, String subject) {
        final ZonedDateTime tokenexpiresAt = issuedAt.plusSeconds(jwtConfiguration.getTokenExpirationTime());
        return JWT.create()
            .withSubject(subject)
            .withIssuer(jwtConfiguration.getTokenIssuer())
            .withIssuedAt(Date.from(issuedAt.toInstant()))
            .withExpiresAt(Date.from(tokenexpiresAt.toInstant()))
            .sign(refreshTokenAlgorithm);
    }

    private boolean tokenWasIssuedBeforeApplicationStart(DecodedJWT decodedToken) {
        final ZonedDateTime applicationStart = ZonedDateTime.now().minus(runtimeMXBean.getUptime(), ChronoUnit.MILLIS);
        return decodedToken.getIssuedAt().before(Date.from(applicationStart.toInstant()));
    }

    /** Decodes the given {@code token} using the passed {@code algorithm} */
    private static DecodedJWT decode(Algorithm algorithm, String token) throws BadCredentialsException, CredentialsExpiredException {
        try {
            return JWT.require(algorithm).build().verify(token);
        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
            throw new BadCredentialsException("credentials.invalid");
        } catch (TokenExpiredException e) {
            throw new CredentialsExpiredException("credentials.expired");
        }
    }
}
