package at.downdrown.housekeeper;

/**
 * Base class for test which is basically a utility class that holds several constants that can be
 * re-used in every test class. It is important that these constants are really usable everywhere,
 * else this class will be dramatically polluted in a very short period of time.
 *
 * @author Manfred Huber
 */
public abstract class TestBase {

    /** Creates 3 users - one for each role. */
    protected static final String CREATE_USERS_SQL = "/sql/create-users.sql";

    /**
     * Creates 3 credentials - one for each user.
     * Requires {@link at.downdrown.housekeeper.TestBase#CREATE_USERS_SQL} to be executed first.
     */
    protected static final String CREATE_CREDENTIALS_SQL = "/sql/create-credentials.sql";

    /**
     * Creates 3 avatars - one for each user.
     * Requires {@link at.downdrown.housekeeper.TestBase#CREATE_USERS_SQL} to be executed first.
     */
    protected static final String CREATE_AVATARS_SQL = "/sql/create-avatars.sql";

}
