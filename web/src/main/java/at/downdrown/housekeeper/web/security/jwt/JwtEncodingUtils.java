package at.downdrown.housekeeper.web.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static at.downdrown.housekeeper.api.exception.ExceptionUtils.throwIf;

/**
 * Utilities for encoding and decoding generated {@link JwtToken}s into Base64 strings and back.
 * This is necessary to set the encoded token in a {@link Cookie}.
 *
 * @author Manfred Huber
 * @see JwtToken
 * @see TokenEndpoint
 * @see JwtTokenFilter
 */
final class JwtEncodingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    private JwtEncodingUtils() {
        // no-op
    }

    static String encode(JwtToken token) {
        try {
            throwIf(token == null, IllegalArgumentException::new);
            final String tokenJson = objectMapper.writeValueAsString(token);
            return encoder.encodeToString(tokenJson.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static JwtToken decode(String encodedToken) {
        try {
            throwIf(encodedToken == null || encodedToken.isEmpty(), IllegalArgumentException::new);
            byte[] decodedToken = decoder.decode(encodedToken);
            return objectMapper.readValue(decodedToken, JwtToken.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
