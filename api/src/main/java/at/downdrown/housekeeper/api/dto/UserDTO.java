package at.downdrown.housekeeper.api.dto;

import at.downdrown.housekeeper.api.Role;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * DTO for the User model.
 *
 * @author Manfred Huber
 */
@Data
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    private ZonedDateTime lastLogin;

}
