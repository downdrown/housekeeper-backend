package at.downdrown.housekeeper.be.listener;

import at.downdrown.housekeeper.be.model.User;
import at.downdrown.housekeeper.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * Event listener that updates the last login of the logged-in user in the database.
 *
 * @author Manfred Huber
 */
@Component
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Autowired
    public LoginListener(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final User user = userRepository.findByUsername(event.getAuthentication().getName());
        user.setLastLogin(ZonedDateTime.now());
        userRepository.save(user);
    }
}
