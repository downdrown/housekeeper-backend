package at.downdrown.housekeeper.be;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Configures the test {@link org.springframework.context.ApplicationContext}.
 * This class imports all backend related configuration classes to set up the
 * {@link org.springframework.context.ApplicationContext} for unit testing.
 *
 * @author Manfred Huber
 */
@SpringBootConfiguration
@Import({BackendConfiguration.class})
public class TestBackendConfiguration {
    // extend the configuration of the backend module for unit testing if necessary
}
