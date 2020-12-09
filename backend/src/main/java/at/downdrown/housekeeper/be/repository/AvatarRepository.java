package at.downdrown.housekeeper.be.repository;

import at.downdrown.housekeeper.be.model.Avatar;
import at.downdrown.housekeeper.be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Data repository for the {@link Avatar} model.
 *
 * @author Manfred Huber
 */
@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    /**
     * Fetches the {@link Avatar} that belongs to the {@link User}
     * with the given {@code username}.
     *
     * @param username the {@code username} of the {@link User} whose {@link Avatar} should be loaded.
     * @return the {@link Avatar} for the {@link User} with the given {@code username} or {@code null} if none is found.
     */
    @Query("select a from Avatar a where a.user.username = :username")
    Avatar findByUsername(String username);
}
