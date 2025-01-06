package network.room;

import game.IGameService;
import network.ChessWebSocket;
import network.MyClient;
import network.MyServer;
import org.junit.jupiter.api.*;
import storage.IUserService;
import storage.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthRoomTest {
    private static ChessWebSocket server;
    private MyClient client;

    private static IUserService userService;
    private static IGameService gameService;

    @BeforeAll
    static void setup() {
        server = MyServer.getServer();
        userService = MyServer.getUserService();
        gameService = MyServer.getGameService();
    }

    @BeforeEach
    void connectClient() throws URISyntaxException, InterruptedException {
        client = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client.connectBlocking();

        Thread.sleep(100);
    }

    @AfterEach
    void closeClient() throws InterruptedException {
        if (client != null) client.closeBlocking();
    }

    @AfterAll
    static void tearDown() throws IOException, InterruptedException {
//        if (server != null) server.stop();
    }

    @Test
    void testLogin() throws InterruptedException {
        // one client connected to the server, so the auth room should have 1 client
        assertEquals(1, server.rooms.get("auth").clients.size());

        // login with wrong username
        client.send("login hello 1");
        assertEquals(client.getBuffer(), "login wrong username or password");

        // login with wrong password
        client.send("login duke 111");
        assertEquals(client.getBuffer(), "login wrong username or password");

        // login with true username and password
        client.send("login duke 123");
        assertEquals(client.getBuffer(), "login success");

        // login success, so the auth should have 0 client, and the lobby room should have 1 client
        assertEquals(0, server.rooms.get("auth").clients.size());
        assertEquals(1, server.rooms.get("lobby").clients.size());
    }

    @Test
    void testRegister() throws InterruptedException {
        assertEquals(1, server.rooms.get("auth").clients.size());

        // signup with the username that has already exists
        client.send("signup duke 123");
        assertEquals(client.getBuffer(), "signup username has already token");

        // client 1 signup with the username that has not exists
        client.send("signup hello 111");
        assertEquals(client.getBuffer(), "signup success");
        verify(userService, times(1)).createUser("hello", "111");
    }

    @Test
    void testWrongCommand() throws InterruptedException {
        // client 1 send wrong command
        client.send("abc hello 111");
        assertEquals(client.getBuffer(), "abc unknown command");
    }
}
