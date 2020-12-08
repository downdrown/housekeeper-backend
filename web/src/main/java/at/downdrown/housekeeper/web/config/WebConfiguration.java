package at.downdrown.housekeeper.web.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@EnableAutoConfiguration
@ComponentScan(basePackages = "at.downdrown.housekeeper.web")
@Import({
    EncodingConfig.class,
    WebSecurityConfigurationDev.class,
    WebSecurityConfigurationProd.class
})
public class WebConfiguration {

    @Bean
    public HttpFirewall httpFirewall() {
        return new StrictHttpFirewall();
    }
}
