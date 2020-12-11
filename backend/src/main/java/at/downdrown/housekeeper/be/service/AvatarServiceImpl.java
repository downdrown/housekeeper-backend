package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.Permission;
import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.api.exception.ModelNotFoundException;
import at.downdrown.housekeeper.api.service.AvatarService;
import at.downdrown.housekeeper.be.mapper.AvatarMapper;
import at.downdrown.housekeeper.be.model.Avatar;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.AvatarRepository;
import at.downdrown.housekeeper.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static at.downdrown.housekeeper.api.exception.ExceptionUtils.throwIf;

/**
 * @author Manfred Huber
 * @see AvatarDTO
 * @see Avatar
 * @see AvatarService
 */
@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarMapper avatarMapper;
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);

    @Autowired
    public AvatarServiceImpl(
        final AvatarMapper avatarMapper,
        final AvatarRepository avatarRepository,
        final UserRepository userRepository) {
        this.avatarMapper = avatarMapper;
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Secured({Permission.CREATE_AVATAR, Permission.UPDATE_AVATAR})
    @Transactional
    public void setAvatarForUser(String username, AvatarDTO avatar) {

        Objects.requireNonNull(username, "username cannot be null");

        // Fetch the user from the backend
        final User user = userRepository.findByUsername(username);

        throwIf(user == null, () -> new ModelNotFoundException("user.notfound"));
        throwIf(!ALLOWED_CONTENT_TYPES.contains(avatar.getContentType()), () -> new IllegalArgumentException("Invalid content type"));

        final Avatar existingAvatar = avatarRepository.findByUsername(username);
        if (existingAvatar != null) {
            avatar.setId(existingAvatar.getId());
        }

        final Avatar avatarModel = avatarMapper.toModel(avatar);
        avatarModel.setUser(user);

        avatarRepository.save(avatarModel);
    }

    @Override
    public AvatarDTO getAvatarForUser(String username) {
        return avatarMapper.toDto(avatarRepository.findByUsername(username));
    }

    @Override
    @Secured(Permission.DELETE_AVATAR)
    @Transactional
    public void deleteAvatarForUser(String username) {
        if (userRepository.existsByUsername(username)) {
            Avatar avatar = avatarRepository.findByUsername(username);
            if (avatar != null) {
                avatarRepository.delete(avatar);
            }
        }
    }
}
