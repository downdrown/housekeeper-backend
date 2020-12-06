package at.downdrown.housekeeper.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web-Security related configuration bean for production.
 * TODO Manfred Huber : set up
 *
 * @author Manfred Huber
 */
@Profile("prod")
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurationProd extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }
}
