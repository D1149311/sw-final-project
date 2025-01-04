package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class UserService implements IUserService {
    public List<User> users = new ArrayList<>();
    public final String filename = "user.txt";

    public UserService() throws IOException {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] userData = data.split(" ");
                users.add(new User(userData[0], userData[1], userData[2]));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            File myObj = new File(filename);
            myObj.createNewFile();
        }
    }

    @Override
    public User findUserById(String id) {
        for (User user : users) {
            if (user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findTop10ByOrderByPointDesc() {
        users.sort(Comparator.comparingInt(u -> Integer.parseInt(u.point)));

        return users.subList(0, Math.min(users.size(), 10));
    }

    @Override
    public User createUser(String id, String password) {
        User user = new User(id, password, "0");
        users.add(user);
        save();
        return user;
    }

    @Override
    public void save() {
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (User user : users) {
                myWriter.write(user.id + " " + user.password + " " + user.point + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
