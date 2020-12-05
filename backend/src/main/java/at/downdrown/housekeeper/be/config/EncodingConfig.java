package at.downdrown.housekeeper.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

/**
 * Configuration class that produces encoding related configuration beans.
 *
 * @author Manfred Huber
 */
@Configuration
public class EncodingConfig {

    /**
     * Produces a {@link CharacterEncodingFilter} that forcibly encodes
     * requests and responses to UTF-8 to prevent encoding related issues.
     *
     * @return the configured {@link CharacterEncodingFilter}
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true); // force for request & response
        return filter;
    }
}
