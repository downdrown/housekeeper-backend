package at.downdrown.housekeeper.web.security.jwt;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest endpoint that let's users authenticate and obtain an valid JWT.
 * This JWT can than be used to authenticate & authorize subsequent requests.
 *
 * @see JwtProvider
 * @see JwtToken
 * @see JwtTokenFilter
 * @author Manfred Huber
 */
@RestController
@RequestMapping("auth/token")
public class TokenEndpoint {

    public static final String URL_PATTERN = "/auth/token/**";

    private static final String SESSION_COOKIE = "SESSIONID";
    private static final String USERDATA_COOKIE = "USERDATA";

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public TokenEndpoint(final AuthenticationManager authenticationManager,
                         final JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final JwtToken issuedToken = jwtProvider.issue(userDetails);
        final String issuedTokenJsonEncoded = JwtEncodingUtils.encode(issuedToken);

        Cookie sessionCookie = new Cookie(SESSION_COOKIE, issuedTokenJsonEncoded);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        Cookie userdataCookie = new Cookie(USERDATA_COOKIE, jwtProvider.getPayload(issuedToken.getAccessToken()));
        userdataCookie.setPath("/");

        response.addCookie(sessionCookie);
        response.addCookie(userdataCookie);
    }

    @PostMapping(value = "logout")
    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        request.getSession().invalidate();
        if (WebUtils.getCookie(request, SESSION_COOKIE) != null) {
            Cookie sessionCookie = new Cookie(SESSION_COOKIE, null);
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(0);
            response.addCookie(sessionCookie);
        }
        if (WebUtils.getCookie(request, USERDATA_COOKIE) != null) {
            Cookie userdataCookie = new Cookie(USERDATA_COOKIE, null);
            userdataCookie.setPath("/");
            userdataCookie.setMaxAge(0);
            response.addCookie(userdataCookie);
        }
    }

    @GetMapping(value = "refresh/{refreshToken}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> refreshToken(@PathVariable("refreshToken") final String refreshToken) {
        // FIXME refresh mechanism needs to be re-done
        return ResponseEntity.ok(jwtProvider.refresh(refreshToken));
    }

    @Data
    private static final class AuthenticationRequest {
        private String username;
        private String password;
    }
}
