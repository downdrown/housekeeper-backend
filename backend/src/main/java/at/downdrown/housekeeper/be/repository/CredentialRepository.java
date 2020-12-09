package at.downdrown.housekeeper.be.repository;

import at.downdrown.housekeeper.be.model.Credential;
import at.downdrown.housekeeper.be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Data repository for the {@link Credential} model.
 *
 * @author Manfred Huber
 */
@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    /**
     * Fetches the {@link Credential} that belongs to the {@link User}
     * with the given {@code username}.
     *
     * @param username the {@code username} of the {@link User} whose {@link Credential} should be loaded.
     * @return the {@link Credential} for the {@link User} with the given {@code username} or {@code null} if none is found.
     */
    @Query("select c from Credential c where c.user.username = :username")
    Credential findByUsername(String username);

}
