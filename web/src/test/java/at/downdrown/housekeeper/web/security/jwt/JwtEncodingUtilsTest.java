package at.downdrown.housekeeper.web.security.jwt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @see JwtEncodingUtils
 * @author Manfred Huber
 */
public class JwtEncodingUtilsTest {

    @Test
    void shouldEncodeToken() {
        JwtToken token = new JwtToken("my-access-token", "my-refresh-token", 3600);
        assertThat(JwtEncodingUtils.encode(token))
            .isEqualTo("eyJhY2Nlc3NfdG9rZW4iOiJteS1hY2Nlc3MtdG9rZW4iLCJyZWZyZXNoX3Rva2VuIjoibXktcmVmcmVzaC10b2tlbiIsImV4cGlyZXNfaW4iOjM2MDB9");
    }

    @Test
    void shouldDecodeToken() {
        assertThat(JwtEncodingUtils.decode("eyJhY2Nlc3NfdG9rZW4iOiJteS1hY2Nlc3MtdG9rZW4iLCJyZWZyZXNoX3Rva2VuIjoibXktcmVmcmVzaC10b2tlbiIsImV4cGlyZXNfaW4iOjM2MDB9"))
            .extracting(JwtToken::getAccessToken, JwtToken::getRefreshToken, JwtToken::getExpiresIn)
            .containsExactly("my-access-token", "my-refresh-token", 3600);
    }

    @Test
    void shouldThrowOnInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> JwtEncodingUtils.encode(null));
        assertThrows(IllegalArgumentException.class, () -> JwtEncodingUtils.decode(null));
    }
}
