package at.downdrown.housekeeper.web.config;

import at.downdrown.housekeeper.web.security.jwt.JwtTokenFilter;
import at.downdrown.housekeeper.web.security.jwt.TokenEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Web-Security related configuration bean for production.
 *
 * @author Manfred Huber
 */
@Profile("prod")
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurationProd extends WebSecurityConfigurerAdapter {

    private final HttpFirewall httpFirewall;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public WebSecurityConfigurationProd(final HttpFirewall httpFirewall,
                                        final UserDetailsService userDetailsService,
                                        final PasswordEncoder passwordEncoder,
                                        final JwtTokenFilter jwtTokenFilter) {
        this.httpFirewall = httpFirewall;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenFilter = jwtTokenFilter;
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
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    /** Exposes the {@link AuthenticationManager} as Spring bean. */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
