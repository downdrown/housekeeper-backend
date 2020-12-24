package at.downdrown.housekeeper.api.service;

import at.downdrown.housekeeper.api.dto.UserDTO;

import java.util.List;

/**
 * Business layer service for the User model.
 *
 * @author Manfred Huber
 */
public interface UserService {

    /**
     * Registers a new user in the application.
     *
     * @param user the {@link UserDTO} with the required informations about the new user.
     * @return the registered user object including it's ID.
     */
    UserDTO register(UserDTO user);

    /**
     * Fetches the user with the given {@code username} from the backend.
     *
     * @param username the username to search for.
     * @return the {@link UserDTO} with the given {@code username} or {@code null} if none is found.
     */
    UserDTO findByUsername(String username);

    /**
     * Fetches all registered users from the backend.
     *
     * @return a {@link List} of all registered {@link UserDTO}s.
     */
    List<UserDTO> findAll();

    /**
     * Updates the user with the given ID in the backend.
     *
     * @param user the {@link UserDTO} containing the ID of the user to be updated and the new details.
     * @return the updated user object.
     */
    UserDTO update(UserDTO user);

    /**
     * Deletes a single user with the given {@code username}.
     *
     * @param username the username of the user that should be deleted.
     */
    void delete(String username);

    /**
     * Updates the user's password.
     * Validates if the given {@code oldPassword} is valid and updates the user's password if so.
     *
     * @param username the username of the user whose password should be changed.
     * @param oldPassword the old (current) password of the user.
     * @param newPassword the new password to be set.
     * @throws IllegalArgumentException if the given {@code oldPassword} is incorrect.
     */
    void changePassword(String username, String oldPassword, String newPassword) throws IllegalArgumentException;

}
