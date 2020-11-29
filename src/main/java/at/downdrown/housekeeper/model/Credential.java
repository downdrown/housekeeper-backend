package at.downdrown.housekeeper.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;

import static at.downdrown.housekeeper.model.FieldConstants.SHORT_COLUMN_LENGTH;

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
    @OneToOne
    @JoinColumn(name = "USERS_FK")
    private User user;

    @Column(name = "PASSWORD", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String password;

    @Column(name = "SALT", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String salt;

}
