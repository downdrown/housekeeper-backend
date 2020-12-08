package at.downdrown.housekeeper.be;

import at.downdrown.housekeeper.be.config.GlobalMethodSecurityConfig;
import at.downdrown.housekeeper.be.config.GrantedAuthorityConfig;
import at.downdrown.housekeeper.be.config.PasswordEncoderConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Backend module configuration bean.
 *
 * @author Manfred Huber
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "at.downdrown.housekeeper.be")
@EnableJpaRepositories(basePackages = "at.downdrown.housekeeper.be.repository")
@EntityScan(basePackages = "at.downdrown.housekeeper.be.model")
@Import({
    GlobalMethodSecurityConfig.class,
    GrantedAuthorityConfig.class,
    PasswordEncoderConfig.class
})
public class BackendConfiguration {
    // Declare backend bean definitions if necessary
}
