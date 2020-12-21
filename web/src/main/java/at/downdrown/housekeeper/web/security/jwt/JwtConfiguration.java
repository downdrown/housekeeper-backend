package at.downdrown.housekeeper.web.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.token")
public class JwtConfiguration {

    /** Token issuer. */
    private String tokenIssuer;

    /** Key that is used to sign the token. */
    private String tokenSigningKey;

    /** Token expiration time in seconds. */
    private Integer tokenExpirationTime;

    /** Refreshtoken expiration time in seconds. */
    private Integer refreshTokenExpirationTime;

}
