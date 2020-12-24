package at.downdrown.housekeeper.web;

import at.downdrown.housekeeper.web.config.EncodingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * Web module configuration bean.
 *
 * @author Manfred Huber
 */
@Configuration
@Import({
    EncodingConfig.class
})
@ComponentScan(basePackages = "at.downdrown.housekeeper.web")
public class WebConfiguration {

    @Bean
    public HttpFirewall httpFirewall() {
        return new StrictHttpFirewall();
    }
}
