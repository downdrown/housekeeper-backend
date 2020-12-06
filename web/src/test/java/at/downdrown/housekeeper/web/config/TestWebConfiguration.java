package at.downdrown.housekeeper.web.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Configures the test {@link org.springframework.context.ApplicationContext}.
 * This class imports all web related configuration classes to set up the
 * {@link org.springframework.context.ApplicationContext} for unit testing.
 *
 * @author Manfred Huber
 */
@SpringBootConfiguration
@Import({WebConfiguration.class, WebSecurityConfigurationTest.class})
public class TestWebConfiguration {
    // extend the configuration of the web module for unit testing if necessary
}
