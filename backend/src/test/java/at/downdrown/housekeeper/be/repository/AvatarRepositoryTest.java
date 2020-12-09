package at.downdrown.housekeeper.be.repository;

import at.downdrown.housekeeper.TestBase;
import at.downdrown.housekeeper.be.model.Avatar;
import at.downdrown.housekeeper.be.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see Avatar
 * @see AvatarRepository
 * @author Manfred Huber
 */
@DataJpaTest
public class AvatarRepositoryTest extends TestBase {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    @Autowired
    public AvatarRepositoryTest(
        final AvatarRepository avatarRepository,
        final UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    void shouldInsertAvatar() {

        User admin = userRepository.findByUsername("admin");
        byte[] mockContent = new byte[]{1, 2, 3, 4, 5};

        Avatar avatar = new Avatar();
        avatar.setUser(admin);
        avatar.setOriginalFileName("my-picture.jpg");
        avatar.setContentType("image/jpeg");
        avatar.setSize(10L);
        avatar.setContent(mockContent);

        avatarRepository.saveAndFlush(avatar);

        assertThat(avatarRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(
                Avatar::getId,
                Avatar::getUser,
                Avatar::getOriginalFileName,
                Avatar::getContentType,
                Avatar::getSize,
                Avatar::getContent
            )
            .containsExactly(
                1000L,
                admin,
                "my-picture.jpg",
                "image/jpeg",
                10L,
                mockContent
            );
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    void shouldReadAvatar() {
        User admin = userRepository.findByUsername("admin");
        assertThat(avatarRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(
                Avatar::getId,
                Avatar::getUser,
                Avatar::getOriginalFileName,
                Avatar::getContentType,
                Avatar::getSize
            )
            .containsExactly(
                10L,
                admin,
                "placeholder.png",
                "image/png",
                255L
            );
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    void shouldReadAvatars() {
        assertThat(avatarRepository.findAll())
            .isNotEmpty()
            .hasSize(3);
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    void shouldUpdateAvatar() {
        User admin = userRepository.findByUsername("admin");
        Avatar avatar = avatarRepository.findByUsername("admin");
        assertThat(avatar)
            .isNotNull()
            .extracting(
                Avatar::getId,
                Avatar::getUser,
                Avatar::getOriginalFileName,
                Avatar::getContentType,
                Avatar::getSize
            )
            .containsExactly(
                10L,
                admin,
                "placeholder.png",
                "image/png",
                255L
            );

        avatar.setOriginalFileName("another-name.png");
        avatar.setContentType("unknown");
        avatar.setSize(99L);

        avatarRepository.saveAndFlush(avatar);

        assertThat(avatarRepository.findByUsername("admin"))
            .isNotNull()
            .extracting(
                Avatar::getId,
                Avatar::getUser,
                Avatar::getOriginalFileName,
                Avatar::getContentType,
                Avatar::getSize
            )
            .containsExactly(
                10L,
                admin,
                "another-name.png",
                "unknown",
                99L
            );
    }

    @Test
    @Sql(CREATE_USERS_SQL)
    @Sql(CREATE_AVATARS_SQL)
    void shouldDeleteAvatar() {
        Avatar avatar = avatarRepository.findByUsername("admin");
        assertThat(avatar).isNotNull();
        avatarRepository.delete(avatar);
        assertThat(avatarRepository.findByUsername("admin")).isNull();
    }
}
