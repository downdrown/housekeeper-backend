package at.downdrown.housekeeper.be.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.io.Serializable;

import static at.downdrown.housekeeper.be.model.FieldConstants.SHORT_COLUMN_LENGTH;

/**
 * Represents credentials for a single user.
 *
 * @author Manfred Huber
 */
@Getter
@Setter
@Entity
@Table(name = "CREDENTIALS")
public class Credential implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDENTIALS_SQ")
    @SequenceGenerator(name = "CREDENTIALS_SQ", sequenceName = "CREDENTIALS_SQ", initialValue = 1000, allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USERS_FK", referencedColumnName = "ID", unique = true, updatable = false, nullable = false)
    private User user;

    @Column(name = "PASSWORD", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String password;

    @Column(name = "SALT", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String salt;

}
