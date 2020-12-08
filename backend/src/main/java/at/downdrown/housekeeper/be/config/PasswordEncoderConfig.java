package at.downdrown.housekeeper.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures the {@link PasswordEncoder} for the whole backend.
 * This is necessary because we need it in the {@link WebSecurityConfiguration} and also in the backend service
 * that stores new users in the database (to encrypt their password with the correct encoder)
 *
 * @author Manfred Huber
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Configures a {@link PasswordEncoder} for the application.
     *
     * @return a valid {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
