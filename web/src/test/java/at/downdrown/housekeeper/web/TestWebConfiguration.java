package at.downdrown.housekeeper.web;

import at.downdrown.housekeeper.be.TestBackendConfiguration;
import at.downdrown.housekeeper.web.config.WebSecurityConfigurationTest;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Configures the test {@link org.springframework.context.ApplicationContext}.
 * This class imports all web related configuration classes to set up the
 * {@link org.springframework.context.ApplicationContext} for unit testing.
 *
 * @author Manfred Huber
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import({
    TestBackendConfiguration.class,
    WebConfiguration.class,
    WebSecurityConfigurationTest.class
})
public class TestWebConfiguration {
    // extend the configuration of the web module for unit testing if necessary
}
