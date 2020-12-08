package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.service.GrantedAuthorityService;
import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.CredentialRepository;
import at.downdrown.housekeeper.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserDetailsService} to enable Spring to fetch
 * user details from the backend.
 *
 * @author Manfred Huber
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final GrantedAuthorityService grantedAuthorityService;

    @Autowired
    public UserDetailsServiceImpl(
        final UserRepository userRepository,
        final CredentialRepository credentialRepository,
        final GrantedAuthorityService grantedAuthorityService) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.grantedAuthorityService = grantedAuthorityService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("The user with the username '" + username + "' does not exist.");
        }

        // Fetch the user and it's credential with the given username from the backend
        final User user = userRepository.findByUsername(username);
        final Credential userCredential = credentialRepository.findByUsername(username);

        // Return the UserDetails object
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            userCredential.getPassword(),
            grantedAuthorityService.calculateAuthoritiesForRole(user.getRole()));
    }
}
