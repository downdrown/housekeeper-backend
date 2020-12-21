package at.downdrown.housekeeper.web.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Will be generated when {@link JwtProvider#issue(UserDetails)} is triggered.
 * This object encapsulates all fields that need to be transferred to the client
 * to authenticate with the JWT and be able to obtain a new one with the refresh
 * token that is normally longer valid than the access token.
 *
 * @author Manfred Huber
 */
@Builder
@AllArgsConstructor
@Getter
public class JwtToken {

    /** The generated JWT token. */
    @JsonProperty("access_token")
    private final String accessToken;

    /** The refreh token used to refresh a claimed token. */
    @JsonProperty("refresh_token")
    private final String refreshToken;

    /** The time in seconds after that the token expires. */
    @JsonProperty("expires_in")
    private final Integer expiresIn;

}
