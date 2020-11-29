package at.downdrown.housekeeper.repository;

import at.downdrown.housekeeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data repository for the {@link User} model.
 *
 * @author Manfred Huber
 */
public interface UserRepository extends JpaRepository<User, String> {
    // no-op
}
