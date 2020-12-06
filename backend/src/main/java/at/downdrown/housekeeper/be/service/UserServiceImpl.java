package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.dto.UserDTO;
import at.downdrown.housekeeper.api.exception.ModelConflictException;
import at.downdrown.housekeeper.api.exception.ModelNotFoundException;
import at.downdrown.housekeeper.api.service.UserService;
import at.downdrown.housekeeper.be.mapper.UserMapper;
import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.CredentialRepository;
import at.downdrown.housekeeper.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static at.downdrown.housekeeper.api.exception.ExceptionUtils.throwIf;

/**
 * @author Manfred Huber
 * @see UserDTO
 * @see User
 * @see UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(
        final UserRepository userRepository,
        final CredentialRepository credentialRepository,
        final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO register(UserDTO user) {

        throwIf(userRepository.existsByUsername(user.getUsername()), ModelConflictException::new);

        final User model = userMapper.toModel(user);
        final User saved = userRepository.save(model);

        Credential credential = new Credential();
        credential.setUser(saved);
        credential.setPassword(user.getRegistrationPassword());
        credential.setSalt("a-salt");

        credentialRepository.save(credential);

        return userMapper.toDto(saved);
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username));
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.toDto(userRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName", "firstName")));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO update(UserDTO user) {
        throwIf(!userRepository.existsByUsername(user.getUsername()), ModelNotFoundException::new);
        User model = userRepository.save(userMapper.toModel(user));
        return userMapper.toDto(model);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String username) {
        throwIf(!userRepository.existsByUsername(username), ModelNotFoundException::new);
        userRepository.deleteByUsername(username);
    }
}