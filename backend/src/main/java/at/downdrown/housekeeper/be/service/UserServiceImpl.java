package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.Permission;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(
        final CredentialRepository credentialRepository,
        final PasswordEncoder passwordEncoder,
        final UserMapper userMapper,
        final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Secured(Permission.CREATE_USER)
    @Transactional
    public UserDTO register(UserDTO user) {

        throwIf(userRepository.existsByUsername(user.getUsername()), ModelConflictException::new);

        final User model = userMapper.toModel(user);
        final User saved = userRepository.save(model);

        Credential credential = new Credential();
        credential.setUser(saved);
        credential.setPassword(passwordEncoder.encode(user.getRegistrationPassword()));

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
    @Secured(Permission.UPDATE_USER)
    @Transactional
    public UserDTO update(UserDTO user) {
        throwIf(!userRepository.existsByUsername(user.getUsername()), ModelNotFoundException::new);
        User model = userRepository.save(userMapper.toModel(user));
        return userMapper.toDto(model);
    }

    @Override
    @Secured(Permission.DELETE_USER)
    @Transactional
    public void delete(String username) {
        throwIf(!userRepository.existsByUsername(username), ModelNotFoundException::new);
        userRepository.deleteByUsername(username);
    }
}
