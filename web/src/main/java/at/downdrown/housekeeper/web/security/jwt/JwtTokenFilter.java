package at.downdrown.housekeeper.web.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * {@link Filter} that extracts and verifies a JWT from each request it processes and
 * sets the {@link Authentication} for the {@link SecurityContext} if the token is valid.
 *
 * @author Manfred Huber
 */
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Set<String> excludedEndpoints = Set.of(TokenEndpoint.URL_PATTERN);
    private static final String SESSION_COOKIE = "SESSIONID";
    private static final String USERDATA_COOKIE = "USERDATA";

    private final JwtProvider jwtProvider;

    @Autowired
    public JwtTokenFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        // Only proceed if there isn't already a authentication in the SecurityContext
        if (securityContext.getAuthentication() == null) {

            // Extract the bearer token from the request header
            final JwtToken token = extractToken(request);

            if (token != null) {
                try {

                    final UserDetails userDetails = jwtProvider.verify(token.getAccessToken());
                    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    securityContext.setAuthentication(authentication);

                } catch (AuthenticationException exception) {
                    throw new AccessDeniedException("access.denied");
                }
            }
        } else {
            log.warn("Found Authentication in SecurityContext, skipping JWT verification ...");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedEndpoints.stream().anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    private static JwtToken extractToken(final HttpServletRequest request) {

        final Cookie sessionIdCookie = WebUtils.getCookie(request, SESSION_COOKIE);

        if (sessionIdCookie != null) {
            return JwtEncodingUtils.decode(sessionIdCookie.getValue());
        } else {
            return null;
        }
    }
}
