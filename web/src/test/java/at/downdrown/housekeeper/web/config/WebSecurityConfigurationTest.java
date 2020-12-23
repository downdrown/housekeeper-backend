package at.downdrown.housekeeper.web.config;

import at.downdrown.housekeeper.be.mock.MockUserDetailsService;
import at.downdrown.housekeeper.web.security.jwt.TokenEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Web-Security related configuration bean for unit-tests.
 * This is required because some security related settings have to
 * be disabled in order to be able to execute the controller tests.
 *
 * Disables CORS and CSRF.
 *
 * @author Manfred Huber
 */
@TestConfiguration
@EnableWebSecurity
public class WebSecurityConfigurationTest extends WebSecurityConfigurerAdapter {

    private final HttpFirewall httpFirewall;
    private final PasswordEncoder passwordEncoder;
    private final MockUserDetailsService mockUserDetailsService;

    @Autowired
    public WebSecurityConfigurationTest(final HttpFirewall httpFirewall,
                                        final PasswordEncoder passwordEncoder,
                                        final MockUserDetailsService mockUserDetailsService) {
        this.httpFirewall = httpFirewall;
        this.passwordEncoder = passwordEncoder;
        this.mockUserDetailsService = mockUserDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(httpFirewall);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(TokenEndpoint.URL_PATTERN)
            .permitAll()
        .and()
            .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
        .and()
            .httpBasic();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(mockUserDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    /**
     * Exposes the {@link AuthenticationManager} as Spring bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
