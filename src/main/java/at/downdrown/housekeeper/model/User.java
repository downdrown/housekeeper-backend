package at.downdrown.housekeeper.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

import static at.downdrown.housekeeper.model.FieldConstants.SHORT_COLUMN_LENGTH;

/**
 * Represents a single user in the application.
 *
 * @author Manfred Huber
 */
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "USERNAME", length = SHORT_COLUMN_LENGTH, nullable = false, updatable = false, unique = true)
    private String username;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;

    @Column(name = "LASTNAME", nullable = false)
    private String lastName;

    @Column(name = "ROLE", length = SHORT_COLUMN_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "LAST_LOGIN")
    private ZonedDateTime lastLogin;

}
