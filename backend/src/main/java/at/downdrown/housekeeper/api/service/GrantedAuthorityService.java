package at.downdrown.housekeeper.api.service;

import at.downdrown.housekeeper.api.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Service definition that calculates {@link org.springframework.security.core.GrantedAuthority}s from
 * the underlying {@link at.downdrown.housekeeper.api.Role} of a {@link at.downdrown.housekeeper.api.dto.UserDTO}.
 *
 * @author Manfred Huber
 */
public interface GrantedAuthorityService {

    /**
     * Calculates the {@link GrantedAuthority}s for the given {@link Role}.
     * These are <strong>static</strong> calculations in the backend.
     * This means that the user's permissions are essentially controlled via the {@link Role}.
     *
     * @param role the {@link Role} of the user.
     * @return a {@link Set} of the corresponding {@link GrantedAuthority}s.
     */
    Set<GrantedAuthority> calculateAuthoritiesForRole(Role role);

}
