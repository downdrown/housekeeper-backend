package at.downdrown.housekeeper.be.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configures JPA related componets.
 *
 * @author Manfred Huber
 */
@Configuration
@EnableJpaRepositories(basePackages = "at.downdrown.housekeeper.be.repository")
@EntityScan(basePackages = "at.downdrown.housekeeper.be.model")
public class JpaConfig {
    // add necessary configurations
}
