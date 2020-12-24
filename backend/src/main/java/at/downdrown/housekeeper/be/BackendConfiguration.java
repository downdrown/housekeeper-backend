package at.downdrown.housekeeper.be;

import at.downdrown.housekeeper.be.config.GlobalMethodSecurityConfig;
import at.downdrown.housekeeper.be.config.GrantedAuthorityConfig;
import at.downdrown.housekeeper.be.config.JpaConfig;
import at.downdrown.housekeeper.be.config.PasswordEncoderConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Backend module configuration bean.
 *
 * @author Manfred Huber
 */
@Configuration
@Import({
    GlobalMethodSecurityConfig.class,
    GrantedAuthorityConfig.class,
    JpaConfig.class,
    PasswordEncoderConfig.class
})
@ComponentScan(basePackages = "at.downdrown.housekeeper.be")
public class BackendConfiguration {

    /** Returns the system default clock so that Clock's can be autowired. */
    @Bean
    public Clock clockBean() {
        return Clock.systemDefaultZone();
    }
}
