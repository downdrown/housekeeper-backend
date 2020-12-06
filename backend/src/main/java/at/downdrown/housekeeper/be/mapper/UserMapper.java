package at.downdrown.housekeeper.be.mapper;

import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.be.model.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapstruct mapper that maps between {@link UserDTO} and {@link User}.
 *
 * @author Manfred Huber
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /** Maps a single model to a single DTO. */
    UserDTO toDto(User user);

    /** Maps a single DTO to a single model. */
    User toModel(UserDTO dto);

    /** Maps a {@link List} of models to a {@link List} of DTOs. */
    List<UserDTO> toDto(List<User> user);

    /** Maps a {@link List} of DTOs to a {@link List} of models. */
    List<User> toModel(List<UserDTO> dto);
}
