package at.downdrown.housekeeper.web.security.jwt;

import at.downdrown.housekeeper.TestBase;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see TokenEndpoint
 * @author Manfred Huber
 */
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TokenEndpointTest extends TestBase {

    private @Autowired MockMvc mockMvc;

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldAuthenticate() throws Exception {

        String authRequest = "{\"username\":\"admin\", \"password\":\"password\"}";

        mockMvc.perform(post("/auth/token/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(authRequest))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(jsonPath("$.access_token").exists())
            .andExpect(jsonPath("$.refresh_token").exists())
            .andExpect(jsonPath("$.expires_in").value("3600"));
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldAuthenticateAndFail() throws Exception {

        String authRequest = "{\"username\":\"admin\", \"password\":\"a-wrong-password\"}";

        mockMvc.perform(post("/auth/token/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(authRequest))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    public void shouldRefreshToken() throws Exception {

        String authRequest = "{\"username\":\"admin\", \"password\":\"password\"}";

        MvcResult mvcResult = mockMvc.perform(post("/auth/token/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())
            .content(authRequest))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
            .andExpect(jsonPath("$.access_token").exists())
            .andExpect(jsonPath("$.refresh_token").exists())
            .andExpect(jsonPath("$.expires_in").value("3600"))
            .andReturn();

        String refreshToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.refresh_token");

        mockMvc.perform(get("/auth/token/refresh/" + refreshToken))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()));
    }
}
