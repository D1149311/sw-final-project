package storage;

public class User {
    public final String id;
    public String password;
    public String point;

    public User(String id, String password, String point) {
        this.id = id;
        this.password = password;
        this.point = point;
    }
}
