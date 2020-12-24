package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.api.exception.ModelNotFoundException;
import at.downdrown.housekeeper.api.service.AvatarService;
import at.downdrown.housekeeper.be.model.Avatar;
import at.downdrown.housekeeper.be.repository.AvatarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AvatarServiceTest extends TestBase {

    private @Autowired AvatarService avatarService;
    private @Autowired AvatarRepository avatarRepository;

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    void shouldSetAvatarForUser_WithNoExistingAvatar() {

        AvatarDTO avatar = new AvatarDTO();
        avatar.setContentType("image/png");
        avatar.setOriginalFileName("myimage.png");
        avatar.setSize(10L);
        avatar.setContent(new byte[] {1, 2, 3});

        avatarService.setAvatarForUser("admin", avatar);

        assertThat(avatarRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize, Avatar::getContent)
            .containsExactly(1000L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    @WithUserDetails("admin")
    void shouldSetAvatarForUser_WithExistingAvatar() {

        Avatar existingAvatar = avatarRepository.findByUsername("admin");
        assertThat(existingAvatar)
            .isNotNull()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize)
            .containsExactly(10L, "image/png", "placeholder.png", 255L);
        assertThat(existingAvatar.getContent()).isNotNull();

        AvatarDTO avatar = new AvatarDTO();
        avatar.setContentType("image/png");
        avatar.setOriginalFileName("myimage.png");
        avatar.setSize(10L);
        avatar.setContent(new byte[] {1, 2, 3});

        avatarService.setAvatarForUser("admin", avatar);

        assertThat(avatarRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize, Avatar::getContent)
            .containsExactly(10L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    void shouldSetAvatarForUser_FailByNonExistentUser() {
        ModelNotFoundException expectedException = assertThrows(
            ModelNotFoundException.class,
            () -> avatarService.setAvatarForUser("a-non-existent-user", new AvatarDTO()));

        assertThat(expectedException.getMessageKey()).isEqualTo("user.notfound");
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("guest")
    void shouldSetAvatarForUser_FailByAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () -> avatarService.setAvatarForUser("admin", new AvatarDTO()));
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    void shouldReturnAvatar() {
        AvatarDTO existingAvatar = avatarService.getAvatarForUser("admin");
        assertThat(existingAvatar)
            .isNotNull()
            .extracting(AvatarDTO::getId, AvatarDTO::getContentType, AvatarDTO::getOriginalFileName, AvatarDTO::getSize)
            .containsExactly(10L, "image/png", "placeholder.png", 255L);
        assertThat(existingAvatar.getContent()).isNotNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    @WithUserDetails("admin")
    void shouldDeleteAvatar() {
        Avatar existingAvatar = avatarRepository.findByUsername("admin");
        assertThat(existingAvatar)
            .isNotNull()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize)
            .containsExactly(10L, "image/png", "placeholder.png", 255L);
        assertThat(existingAvatar.getContent()).isNotNull();
        avatarService.deleteAvatarForUser("admin");
        assertThat(avatarRepository.findByUsername("admin")).isNull();
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("guest")
    void shouldDeleteAvatar_FailByAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () -> avatarService.deleteAvatarForUser("admin"));
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @WithUserDetails("admin")
    void shouldDeleteAvatar_WithNonExistentUser() {
        avatarService.deleteAvatarForUser("a-non-existent-user");
    }
}
