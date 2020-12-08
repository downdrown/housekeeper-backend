package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.Permission;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.service.GrantedAuthorityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * @see Permission
 * @see GrantedAuthority
 * @see GrantedAuthorityService
 * @author Manfred Huber
 */
@Service
public class GrantedAuthorityServiceImpl implements GrantedAuthorityService {

    private static final GrantedAuthority CREATE_USER = new SimpleGrantedAuthority(Permission.CREATE_USER);
    private static final GrantedAuthority UPDATE_USER = new SimpleGrantedAuthority(Permission.UPDATE_USER);
    private static final GrantedAuthority DELETE_USER = new SimpleGrantedAuthority(Permission.DELETE_USER);

    @Override
    public Set<GrantedAuthority> calculateAuthoritiesForRole(Role role) {
        if (role == null) {
            return Collections.emptySet();
        }
        switch (role) {
            case ADMIN:
                return Set.of(
                    CREATE_USER,
                    UPDATE_USER,
                    DELETE_USER
                );
            case USER:
                return Collections.emptySet();
            case GUEST:
                return Collections.emptySet();
            default:
                return Collections.emptySet();
        }
    }
}
