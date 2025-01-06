package storage;

import java.util.List;

/**
 * define user storage service
 */
public interface IUserService {
    /**
     * find user by userId, return null if not found
     */
    User findUserById(String userId);

    /**
     * get the top 10 point list
     */
    List<User> findTop10ByOrderByPointDesc();

    /**
     * create a new user, and should save it
     */
    User createUser(String userId, String password);

    /**
     * save the data
     */
    boolean save();
}
