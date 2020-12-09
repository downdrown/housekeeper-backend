package at.downdrown.housekeeper.api.service;

import at.downdrown.housekeeper.api.dto.AvatarDTO;

/**
 * Business layer service for the Avatar model.
 *
 * @author Manfred Huber
 */
public interface AvatarService {

    /**
     * Sets the avatar for the user with the given {@code username}.
     * If the user already has an avatar it will be updated, else it will be created.
     *
     * @param username the username of the user whose avatar should be set.
     * @param avatar the {@link AvatarDTO} containing all necessary data.
     */
    void setAvatarForUser(String username, AvatarDTO avatar);

    /**
     * Fetches the avatar for the user with the given {@code username} from the backend.
     *
     * @param username the username of the user whose avatar should be fetched.
     * @return the user's avatar or {@code null} if none is found.
     */
    AvatarDTO getAvatarForUser(String username);

    /**
     * Deletes the avatar for the user with the given {@code username}.
     * If no user with the given {@code username} exists nothing will happen.
     *
     * @param username the username of the user whose avatar should be deleted.
     */
    void deleteAvatarForUser(String username);

}
