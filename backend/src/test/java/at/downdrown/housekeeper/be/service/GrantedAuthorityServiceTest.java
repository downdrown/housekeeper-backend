package at.downdrown.housekeeper.be.service;

import at.downdrown.housekeeper.api.Permission;
import at.downdrown.housekeeper.api.Role;
import at.downdrown.housekeeper.api.service.GrantedAuthorityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see GrantedAuthorityService
 * @see GrantedAuthorityServiceImpl
 * @author Manfred Huber
 */
@SpringBootTest
public class GrantedAuthorityServiceTest {

    private @Autowired GrantedAuthorityService grantedAuthorityService;

    @Test
    void shouldCheckForCorrectImplementation() {
        assertThat(grantedAuthorityService).isInstanceOf(GrantedAuthorityServiceImpl.class);
    }

    @Test
    void shouldBeNullSafe() {
        assertThat(grantedAuthorityService.calculateAuthoritiesForRole(null))
            .isNotNull()
            .isEmpty();
    }

    @Test
    void shouldCalculateAdminAuthorities() {
        assertThat(grantedAuthorityService.calculateAuthoritiesForRole(Role.ADMIN))
            .hasSize(6)
            .map(GrantedAuthority::getAuthority)
            .contains(
                Permission.CREATE_AVATAR, Permission.UPDATE_AVATAR, Permission.DELETE_AVATAR,
                Permission.CREATE_USER, Permission.UPDATE_USER, Permission.DELETE_USER
            );
    }

    @Test
    void shouldCalculateUserAuthorities() {
        assertThat(grantedAuthorityService.calculateAuthoritiesForRole(Role.USER))
            .hasSize(3)
            .map(GrantedAuthority::getAuthority)
            .contains(Permission.CREATE_AVATAR, Permission.UPDATE_AVATAR, Permission.DELETE_AVATAR);
    }

    @Test
    void shouldCalculateGuestAuthorities() {
        assertThat(grantedAuthorityService.calculateAuthoritiesForRole(Role.GUEST))
            .isNotNull()
            .isEmpty();
    }
}
