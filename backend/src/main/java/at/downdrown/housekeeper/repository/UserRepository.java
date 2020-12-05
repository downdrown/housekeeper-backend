package at.downdrown.housekeeper.repository;

import at.downdrown.housekeeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data repository for the {@link User} model.
 *
 * @author Manfred Huber
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Fetches the user with the given {@code username} from the database.
     *
     * @param username the username to search for.
     * @return the {@link User} with the given {@code username} or {@code null} if none is found.
     */
    User findByUsername(String username);

    /**
     * Checks if a {@link User} with the given {@code username} exists.
     *
     * @param username the username to search for.
     * @return either {@code true} if the {@link User} exists or {@code false} if not.
     */
    boolean existsByUsername(String username);

    /**
     * Deletes a {@link User} with the given {@code username}.
     *
     * @param username the {@code username} of the {@link User} that should be deleted.
     */
    void deleteByUsername(String username);

}
