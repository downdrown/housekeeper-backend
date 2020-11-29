package at.downdrown.housekeeper.model;

/**
 * Defines available application roles.
 *
 * @author Manfred Huber
 */
public enum Role {

    /** Administrative role for managing users, topics, rewards, etc. */
    ADMIN,

    /** User role for everyone that is just a normal user in the application. */
    USER,

    /** A role for people that just want to try the app for once. */
    GUEST

}
