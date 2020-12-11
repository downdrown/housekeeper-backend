package at.downdrown.housekeeper.be.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static at.downdrown.housekeeper.be.model.FieldConstants.SHORT_COLUMN_LENGTH;

/**
 * Represents an avatar for a single user.
 *
 * @author Manfred Huber
 */
@Getter
@Setter
@Entity
@Table(name = "AVATARS")
public class Avatar {

    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVATARS_SQ")
    @SequenceGenerator(name = "AVATARS_SQ", sequenceName = "AVATARS_SQ", initialValue = 1000, allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USERS_FK", referencedColumnName = "ID", unique = true, updatable = false, nullable = false)
    private User user;

    @Column(name = "ORIGINAL_FILE_NAME", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String originalFileName;

    @Column(name = "CONTENT_TYPE", length = SHORT_COLUMN_LENGTH, nullable = false)
    private String contentType;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private byte[] content;

}
