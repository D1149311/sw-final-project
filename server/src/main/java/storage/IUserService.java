package storage;

import java.util.List;

public interface IUserService {
    User findUserById(String id);

    List<User> findTop10ByOrderByPointDesc();

    User createUser(String id, String password);

    void save();
}
