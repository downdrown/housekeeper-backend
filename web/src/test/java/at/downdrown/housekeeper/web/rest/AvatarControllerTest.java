package at.downdrown.housekeeper.web.rest;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.api.service.AvatarService;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.AvatarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see User
 * @see at.downdrown.housekeeper.be.repository.UserRepository
 * @see UserController
 * @author Manfred Huber
 */
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithUserDetails("admin")
public class AvatarControllerTest extends TestBase {

    private @Autowired MockMvc mockMvc;
    private @Autowired AvatarService avatarService;

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    public void shouldSetAvatarForUser() throws Exception {
        mockMvc.perform(multipart("/avatar/admin")
            .file(new MockMultipartFile("file", "myimage.png", "image/png", "content".getBytes())))
            .andExpect(status().isOk());

        assertThat(avatarService.getAvatarForUser("admin"))
            .isNotNull()
            .extracting(AvatarDTO::getId, AvatarDTO::getOriginalFileName, AvatarDTO::getContentType, AvatarDTO::getSize, AvatarDTO::getContent)
            .containsExactly(1000L, "myimage.png", "image/png", 7L, "content".getBytes());
    }

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    public void shouldSetAvatarForUser_FailByWrongFileType() throws Exception {
        mockMvc.perform(multipart("/avatar/admin")
            .file(new MockMultipartFile("file", "somedocument.pdf", "application/pdf", "content".getBytes())))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSetAvatarForUser_FailByNonExistentUser() throws Exception {
        mockMvc.perform(multipart("/avatar/some-random-dude")
            .file(new MockMultipartFile("file", "myimage.png", "image/png", "content".getBytes())))
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    @Sql(TestBase.CREATE_AVATARS_SQL)
    public void shouldGetAvatarForUser() throws Exception {
        mockMvc.perform(get("/avatar/admin"))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.CONTENT_DISPOSITION))
            .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG_VALUE))
            .andExpect(content().encoding(StandardCharsets.UTF_8.name()));
    }

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    public void shouldGetAvatarForUser_FailByNonExistentAvatar() throws Exception {
        mockMvc.perform(get("/avatar/admin"))
            .andExpect(status().isNotFound());
    }

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    @Sql(TestBase.CREATE_AVATARS_SQL)
    public void shouldDeleteAvatarForUser() throws Exception {
        mockMvc.perform(delete("/avatar/admin"))
            .andExpect(status().isOk());
    }

    @Test
    @Sql(TestBase.CREATE_USERS_SQL)
    public void shouldDeleteAvatarForUser_WithNonExistentAvatar() throws Exception {
        mockMvc.perform(delete("/avatar/admin"))
            .andExpect(status().isOk());
    }
}
