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

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public TokenEndpoint(final JwtProvider jwtProvider,
                         final AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtToken> authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(jwtProvider.issue(userDetails));
    }

    @GetMapping(value = "refresh/{refreshToken}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> refreshToken(@PathVariable("refreshToken") final String refreshToken) {
        return ResponseEntity.ok(jwtProvider.refresh(refreshToken));
    }

    @Data
    private static final class AuthenticationRequest {
        private String username;
        private String password;
    }
}
