package at.downdrown.housekeeper.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Configures method security for the application and enables the
 * {@link org.springframework.security.access.annotation.Secured} permission check.
 *
 * @author Manfred Huber
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    // add necessary configurations
}
