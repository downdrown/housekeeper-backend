package at.downdrown.housekeeper.be.mapper;

import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.be.model.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapstruct mapper that maps between {@link AvatarDTO} and {@link Avatar}.
 *
 * @author Manfred Huber
 */
@Mapper(componentModel = "spring")
public interface AvatarMapper {

    /** Maps a single model to a single DTO. */
    AvatarDTO toDto(Avatar user);

    /** Maps a single DTO to a single model. */
    @Mapping(target = "user", ignore = true)
    Avatar toModel(AvatarDTO dto);

    /** Maps a {@link List} of models to a {@link List} of DTOs. */
    List<AvatarDTO> toDto(List<Avatar> user);

    /** Maps a {@link List} of DTOs to a {@link List} of models. */
    List<Avatar> toModel(List<AvatarDTO> dto);
}
