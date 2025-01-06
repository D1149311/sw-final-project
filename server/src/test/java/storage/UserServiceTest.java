package storage;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService service;

    @BeforeEach
    void setup() throws IOException {
        File myObj = new File("test.txt");
        myObj.delete();

        service = new UserService("test.txt");
    }

    @AfterEach
    void reset() {
        File myObj = new File("test.txt");
        myObj.delete();
    }

    @Test
    void testCreateUser() {
        User user = service.createUser("hello", "world");
        List<User> userList = service.findTop10ByOrderByPointDesc();

        assertEquals(1, userList.size());
        assertEquals("0", user.point);

        user = service.findUserById("hello");
        assertEquals("world", user.password);

        User user1 = service.createUser("hello", "world");
        assertNull(user1);
    }

    @Test
    void restart() throws IOException {
        service.createUser("hello", "world");
        service.createUser("hello1", "world");
        service = new UserService("test.txt");
        assertEquals(2, service.users.size());
    }

    @Test
    void testFindUser() {
        service.createUser("hello", "world");

        assertNull(service.findUserById("abc"));
        assertEquals("world", service.findUserById("hello").password);
    }

    @Test
    void testSave() throws FileNotFoundException {
        service.createUser("hello", "world");
        service.createUser("hello1", "world");

        service.findUserById("hello").point = "100";
        assertTrue(service.save());

        File myObj = new File("test.txt");
        Scanner myReader = new Scanner(myObj);
        String data = myReader.nextLine();
        assertEquals("hello world 100", data);

        data = myReader.nextLine();
        assertEquals("hello1 world 0", data);
        myReader.close();
    }

    @Test
    void cannotSave() {
        File file = new File("test.txt");
        file.setReadOnly();
        assertFalse(service.save());
    }

    @Test
    void testFindTop10() {
        service.createUser("hello", "world");
        service.createUser("hello1", "world");

        service.findUserById("hello1").point = "100";
        assertTrue(service.save());

        assertEquals("hello1", service.findTop10ByOrderByPointDesc().get(0).userId);
        assertEquals("100", service.findTop10ByOrderByPointDesc().get(0).point);
    }
}