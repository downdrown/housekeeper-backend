package at.downdrown.housekeeper.be.model;

import at.downdrown.housekeeper.api.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

import static at.downdrown.housekeeper.be.model.FieldConstants.SHORT_COLUMN_LENGTH;

/**
 * Represents a single user in the application.
 *
 * @author Manfred Huber
 */
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SQ")
    @SequenceGenerator(name = "USERS_SQ", sequenceName = "USERS_SQ", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME", length = SHORT_COLUMN_LENGTH, nullable = false, unique = true)
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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Credential credential;

}
