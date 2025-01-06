package storage;

/**
 * A user should have follow data
 */
public class User {
    public final String userId;
    public String password;
    public String point;

    /**
     * Initialize user data
     */
    public User(final String userId, final String password, final String point) {
        this.userId = userId;
        this.password = password;
        this.point = point;
    }
}
