package at.downdrown.housekeeper.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web-Security related configuration bean for unit-tests.
 * This is required because some security related settings have to
 * be disabled in order to be able to execute the controller tests.
 *
 * Disables CORS and CSRF.
 *
 * @author Manfred Huber
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable();
    }
}
