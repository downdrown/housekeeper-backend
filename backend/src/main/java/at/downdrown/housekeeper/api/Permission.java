package at.downdrown.housekeeper.api;

/**
 * Defines all available permissions for the backend.
 *
 * @author Manfred Huber
 */
public final class Permission {

    private Permission() {
        // no-op, just hide
    }

    public static final String CREATE_AVATAR = "create.avatar";
    public static final String UPDATE_AVATAR = "update.avatar";
    public static final String DELETE_AVATAR = "delete.avatar";

    public static final String CREATE_USER = "create.user";
    public static final String UPDATE_USER = "update.user";
    public static final String DELETE_USER = "delete.user";

}
