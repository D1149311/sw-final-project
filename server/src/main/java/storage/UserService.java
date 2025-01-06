package storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * service that provide search and save the data of users
 */
public class UserService implements IUserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    public final List<User> users = new ArrayList<>();
    public final String filename;

    /**
     * load the data
     */
    public UserService(final String filename) throws IOException {
        this.filename = filename;

        try {
            final File myObj = new File(filename);
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    final String data = myReader.nextLine();
                    final String[] userData = data.split(" ");
                    users.add(new User(userData[0], userData[1], userData[2]));
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.warning(e.getMessage());
            final File myObj = new File(filename);
            myObj.createNewFile();
        }
    }

    @Override
    public User findUserById(final String userId) {
        User result = null;

        for (final User user : users) {
            if (user.userId.equals(userId)) {
                result = user;
                break;
            }
        }

        return result;
    }

    @Override
    public List<User> findTop10ByOrderByPointDesc() {
        users.sort((u1, u2) -> Integer.parseInt(u2.point) - Integer.parseInt(u1.point));

        return users.subList(0, Math.min(users.size(), 10));
    }

    @Override
    public User createUser(final String userId, final String password) {
        User user = null;
        
        if (findUserById(userId) == null) {
            user = new User(userId, password, "0");
            users.add(user);
            save();
        }
        
        return user;
    }

    @Override
    public boolean save() {
        boolean success = false;

        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(filename))) {
            for (final User user : users) {
                myWriter.write(user.userId + " " + user.password + " " + user.point);
                myWriter.newLine(); // 更可讀的換行方式
            }
            LOGGER.info("Successfully wrote to the file.");
            success = true;
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        return success;
    }
}
