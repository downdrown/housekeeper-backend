package at.downdrown.housekeeper.be.mapper;

import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.be.model.Avatar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see AvatarDTO
 * @see Avatar
 *
 * @author Manfred Huber
 */
@SpringBootTest
public class AvatarMapperTest {

    private @Autowired AvatarMapper mapper;

    @Test
    void shouldMapToNull() {
        assertThat(mapper.toModel((AvatarDTO) null)).isNull();
        assertThat(mapper.toDto((Avatar) null)).isNull();
    }

    @Test
    void shouldMapToModel() {

        AvatarDTO dto = new AvatarDTO();
        dto.setId(1L);
        dto.setContentType("image/png");
        dto.setOriginalFileName("myimage.png");
        dto.setSize(10L);
        dto.setContent(new byte[] {1, 2, 3});

        assertThat(mapper.toModel(dto))
            .isNotNull()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize, Avatar::getContent)
            .containsExactly(1L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});

        assertThat(mapper.toModel(List.of(dto)))
            .isNotEmpty()
            .first()
            .extracting(Avatar::getId, Avatar::getContentType, Avatar::getOriginalFileName, Avatar::getSize, Avatar::getContent)
            .containsExactly(1L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});
    }

    @Test
    void shouldMapToDto() {

        Avatar model = new Avatar();
        model.setId(1L);
        model.setContentType("image/png");
        model.setOriginalFileName("myimage.png");
        model.setSize(10L);
        model.setContent(new byte[] {1, 2, 3});

        assertThat(mapper.toDto(model))
            .isNotNull()
            .extracting(AvatarDTO::getId, AvatarDTO::getContentType, AvatarDTO::getOriginalFileName, AvatarDTO::getSize, AvatarDTO::getContent)
            .containsExactly(1L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});

        assertThat(mapper.toDto(List.of(model)))
            .isNotEmpty()
            .first()
            .extracting(AvatarDTO::getId, AvatarDTO::getContentType, AvatarDTO::getOriginalFileName, AvatarDTO::getSize, AvatarDTO::getContent)
            .containsExactly(1L, "image/png", "myimage.png", 10L, new byte[] {1, 2, 3});
    }
}
