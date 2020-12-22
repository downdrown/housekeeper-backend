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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link Filter} that extracts and verifies a JWT from each request it processes and
 * sets the {@link Authentication} for the {@link SecurityContext} if the token is valid.
 *
 * @author Manfred Huber
 */
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_AUTH_PREFIX = "Bearer";

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
            final String token = extractToken(request);

            if (token != null) {
                try {

                    final UserDetails userDetails = jwtProvider.verify(token);
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

    private static String extractToken(final HttpServletRequest request) {

        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasLength(authorizationHeader) && authorizationHeader.startsWith(TOKEN_AUTH_PREFIX)) {
            return authorizationHeader.replace(TOKEN_AUTH_PREFIX, "").trim();
        }

        return null;
    }
}
