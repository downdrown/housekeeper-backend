package at.downdrown.housekeeper.web.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see JwtTokenFilter
 * @author Manfred Huber
 */
public class JwtTokenFilterTest {

    private JwtProvider mockJwtProvider;
    private JwtTokenFilter jwtTokenFilter;
    private HttpServletRequest mockHttpServletRequest;
    private HttpServletResponse mockHttpServletResponse;
    private FilterChain mockFilterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        mockJwtProvider = mock(JwtProvider.class);
        jwtTokenFilter = new JwtTokenFilter(mockJwtProvider);
        mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletResponse = mock(HttpServletResponse.class);
        mockFilterChain = mock(FilterChain.class);
    }

    @Test
    void shouldFilterAndSucceed() throws ServletException, IOException {

        JwtToken originalToken = new JwtToken("my-access-token", "my-refresh-token", 1);
        String encodedToken = JwtEncodingUtils.encode(originalToken);

        Cookie sessionCookie = new Cookie("SESSIONID", encodedToken);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        Cookie userdataCookie = new Cookie("USERDATA", "my-userdata");
        userdataCookie.setPath("/");

        Cookie[] cookies = new Cookie[] { sessionCookie, userdataCookie };

        when(mockHttpServletRequest.getCookies())
            .thenReturn(cookies);

        when(mockJwtProvider.verify(eq("my-access-token")))
            .thenReturn(new User("user", "password", Collections.emptySet()));

        jwtTokenFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        verify(mockFilterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNotNull()
            .isInstanceOf(UsernamePasswordAuthenticationToken.class)
            .extracting(Authentication::getPrincipal)
            .isInstanceOf(User.class);
    }

    @Test
    void shouldFilterAndFailByNoToken() throws ServletException, IOException {

        when(mockHttpServletRequest.getHeader(eq("Authorization")))
            .thenReturn(null);

        jwtTokenFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        verify(mockFilterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNull();
    }

    @Test
    void shouldFilterAndFailByAuthenticationException() {

        JwtToken originalToken = new JwtToken("my-access-token", "my-refresh-token", 1);
        String encodedToken = JwtEncodingUtils.encode(originalToken);

        Cookie sessionCookie = new Cookie("SESSIONID", encodedToken);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        Cookie userdataCookie = new Cookie("USERDATA", "my-userdata");
        userdataCookie.setPath("/");

        Cookie[] cookies = new Cookie[] { sessionCookie, userdataCookie };

        when(mockHttpServletRequest.getCookies())
            .thenReturn(cookies);

        when(mockJwtProvider.verify(eq("my-access-token")))
            .thenThrow(CredentialsExpiredException.class);

        assertThrows(AccessDeniedException.class, () -> jwtTokenFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain));
    }

    @Test
    void shouldFilterAndFailByExistingAuthentication() throws ServletException, IOException {

        // given
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        // when
        jwtTokenFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        verify(mockJwtProvider, never()).verify(any());
    }
}
