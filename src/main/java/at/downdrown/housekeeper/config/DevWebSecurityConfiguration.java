package at.downdrown.housekeeper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web-Security related configuration bean for development.
 * Disables CORS and CSRF for easier development.
 *
 * @author Manfred Huber
 */
@Profile("dev")
@Configuration
@EnableWebSecurity
public class DevWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable();
    }
}
