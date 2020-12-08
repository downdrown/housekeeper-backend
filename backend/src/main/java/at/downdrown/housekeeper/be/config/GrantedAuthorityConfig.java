package at.downdrown.housekeeper.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * Configures the defaults for {@link org.springframework.security.core.GrantedAuthority} permission
 * checks in combination with Spring's {@link org.springframework.security.access.annotation.Secured}.
 *
 * @author Manfred Huber
 */
@Configuration
public class GrantedAuthorityConfig {

    /** Spring checks for a 'ROLE_' prefix per default which we want to disable. */
    @Bean
    public GrantedAuthorityDefaults configure() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix from @Secured permission check
    }
}
