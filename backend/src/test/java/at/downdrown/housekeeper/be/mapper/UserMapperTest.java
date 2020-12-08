package at.downdrown.housekeeper.be.mapper;

import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.be.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see UserDTO
 * @see User
 *
 * @author Manfred Huber
 */
public class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapToNull() {
        assertThat(mapper.toModel((UserDTO) null)).isNull();
        assertThat(mapper.toDto((User) null)).isNull();
    }

    @Test
    void shouldMapToModel() {

        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("a-username");
        dto.setFirstName("a-firstname");
        dto.setLastName("a-lastname");
        dto.setRole(Role.USER);
        dto.setLastLogin(ZonedDateTime.now());

        assertThat(mapper.toModel(dto))
            .isNotNull()
            .extracting(User::getId, User::getUsername, User::getFirstName, User::getLastName, User::getRole, User::getLastLogin)
            .containsExactly(1L, "a-username", "a-firstname", "a-lastname", Role.USER, null);

        assertThat(mapper.toModel(List.of(dto)))
            .isNotEmpty()
            .first()
            .extracting(User::getId, User::getUsername, User::getFirstName, User::getLastName, User::getRole, User::getLastLogin)
            .containsExactly(1L, "a-username", "a-firstname", "a-lastname", Role.USER, null);
    }

    @Test
    void shouldMapToDto() {

        final ZonedDateTime lastLogin = ZonedDateTime.now();

        User model = new User();
        model.setId(1L);
        model.setUsername("a-username");
        model.setFirstName("a-firstname");
        model.setLastName("a-lastname");
        model.setRole(Role.USER);
        model.setLastLogin(lastLogin);

        assertThat(mapper.toDto(model))
            .isNotNull()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getRole, UserDTO::getLastLogin)
            .containsExactly(1L, "a-username", "a-firstname", "a-lastname", Role.USER, lastLogin);

        assertThat(mapper.toDto(List.of(model)))
            .isNotEmpty()
            .first()
            .extracting(UserDTO::getId, UserDTO::getUsername, UserDTO::getFirstName, UserDTO::getLastName, UserDTO::getRole, UserDTO::getLastLogin)
            .containsExactly(1L, "a-username", "a-firstname", "a-lastname", Role.USER, lastLogin);
    }
}
