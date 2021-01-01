package at.downdrown.housekeeper.web.security.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Provider interface that separates the concrete JWT implementation from the rest of the code.
 * This is intended to keep the app as flexible as possible - different JWT providers can then
 * be used in a few hours if there occur security issues on the current implementation for example.
 *
 * @author Manfred Huber
 */
public interface JwtProvider {

    /**
     * Claims a new token for an already authenticated user.
     *
     * @param userDetails the {@link UserDetails} related to the already authenticated user.
     * @return a {@link JwtToken} containing the signed {@code access_token} and {@code refresh_token}.
     */
    JwtToken issue(UserDetails userDetails);

    /**
     * Validates that the given token actually is a {@code refresh_token} and generates a new {@link JwtToken}
     * containing a new {@code access_token} if the refresh token hasn't expired yet.
     *
     * @param refreshToken the transmitted refresh token.
     * @return a new {@code access_token}.
     * @throws BadCredentialsException if the validation fails.
     */
    String refresh(String refreshToken) throws BadCredentialsException, CredentialsExpiredException;

    /**
     * Validates that the given token actually is a {@code access_token} and parses the {@link UserDetails} from the token.
     * The implementation <strong>does not</strong> reload the permissions from the {@link UserDetailsService}!
     *
     * @param accessToken the transmitted access token.
     * @return the parsed {@link UserDetails} that were contained in the token.
     * @throws BadCredentialsException if the algorithm doesn't match, the signature couldn't be verified or invalid claims have been transmitted.
     * @throws CredentialsExpiredException if the token is expired.
     */
    UserDetails verify(String accessToken) throws BadCredentialsException, CredentialsExpiredException;

    /**
     * Extracts the header from the given access token.
     *
     * @param accessToken the token whose header should be extracted.
     * @return the extracted header.
     * @throws BadCredentialsException if the algorithm doesn't match, the signature couldn't be verified or invalid claims have been transmitted.
     * @throws CredentialsExpiredException if the token is expired.
     */
    String getHeader(String accessToken) throws BadCredentialsException, CredentialsExpiredException;

    /**
     * Extracts the payload from the given access token.
     *
     * @param accessToken the token whose payload should be extracted.
     * @return the extracted payload.
     * @throws BadCredentialsException if the algorithm doesn't match, the signature couldn't be verified or invalid claims have been transmitted.
     * @throws CredentialsExpiredException if the token is expired.
     */
    String getPayload(String accessToken) throws BadCredentialsException, CredentialsExpiredException;

    /**
     * Extracts the signature from the given access token.
     *
     * @param accessToken the token whose signature should be extracted.
     * @return the extracted signature.
     * @throws BadCredentialsException if the algorithm doesn't match, the signature couldn't be verified or invalid claims have been transmitted.
     * @throws CredentialsExpiredException if the token is expired.
     */
    String getSignature(String accessToken) throws BadCredentialsException, CredentialsExpiredException;

}
