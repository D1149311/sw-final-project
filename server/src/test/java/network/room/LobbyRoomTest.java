package network.room;

import game.*;
import game.piece.KingPiece;
import game.piece.PawnPiece;
import network.ChessWebSocket;
import network.MyClient;
import network.MyServer;
import org.junit.jupiter.api.*;
import storage.IUserService;
import storage.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LobbyRoomTest {
    private static ChessWebSocket server;
    private MyClient client1, client2, client3;

    private static IUserService userService;
    private static IGameService gameService;
    private static IChessService chessService;

    @BeforeAll
    static void setup() {
        server = MyServer.getServer();
        userService = MyServer.getUserService();
        gameService = MyServer.getGameService();
        chessService = MyServer.getChessService();
    }

    @BeforeEach
    void connectClient() throws URISyntaxException, InterruptedException {
        client1 = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client1.connectBlocking();
        client1.send("login duke 123");
        client1.getBuffer();

        client2 = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client2.connectBlocking();
        client2.send("login john 123");
        client2.getBuffer();

        client3 = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client3.connectBlocking();
        client3.send("login ruby 111");
        client3.getBuffer();
    }

    @AfterEach
    void closeClient() throws InterruptedException {
        if (client1 != null) client1.closeBlocking();
        if (client2 != null) client2.closeBlocking();
        if (client3 != null) client3.closeBlocking();
    }

    @AfterAll
    static void tearDown() throws IOException, InterruptedException {
//        if (server != null) server.stop();
    }

    @Test
    void testTop10() throws InterruptedException {
        client1.send("top10");
        assertEquals("top10 duke 100 john 90 ruby 0 ", client1.getBuffer());
    }

    @Test
    void testGetInfo() throws InterruptedException {
        // reset point
        when(userService.findUserById("duke")).thenReturn(new User("duke", "123", "100"));
        when(userService.findUserById("john")).thenReturn(new User("john", "123", "90"));
        when(userService.findUserById("ruby")).thenReturn(new User("ruby", "111", "0"));
        when(userService.findTop10ByOrderByPointDesc()).thenReturn(List.of(new User("duke", "123", "100"), new User("john", "123", "90"), new User("ruby", "111", "0")));

        client1.send("info");
        assertEquals("info duke 100", client1.getBuffer());

        client2.send("info");
        assertEquals("info john 90", client2.getBuffer());

        client3.send("info");
        assertEquals("info ruby 0", client3.getBuffer());
    }

    @Test
    void testPlayOnline() throws InterruptedException {
        // client 1 start play, and waiting for new player
        client1.send("play online");
        assertEquals("play success", client1.getBuffer());

        // client 1 start play again, the server should return wrong message
        client1.send("play online");
        assertEquals("play unknown command", client1.getBuffer());

        client2.send("play online");
        assertEquals("play success", client2.getBuffer());

        assertEquals("start john duke", client1.getBuffer());
        assertEquals("start john duke", client2.getBuffer());

        // client 1 and client 2 join the new room
        assertEquals(3, server.rooms.size());
        assertEquals(1, server.rooms.get("lobby").clients.size());

        // online point
        Set<String> roomKeys = new HashSet<>(server.rooms.keySet());
        roomKeys.remove("lobby");
        roomKeys.remove("auth");
        String onlineRoomId = (String) roomKeys.toArray()[0];
        assertEquals(100, ((GameRoom)server.rooms.get(onlineRoomId)).point);
    }

    @Test
    void testPlayFriend() throws InterruptedException {
        client1.send("play create");
        assertEquals("play success", client1.getBuffer());

        String[] msg = client1.getBuffer().split(" ");
        assertEquals("id", msg[0]);

        client2.send("play friend " + msg[1]);
        assertEquals("play success", client2.getBuffer());

        assertEquals("start john duke", client1.getBuffer());
        assertEquals("start john duke", client2.getBuffer());

        // friend point
        Set<String> roomKeys = new HashSet<>(server.rooms.keySet());
        roomKeys.remove("lobby");
        roomKeys.remove("auth");
        String onlineRoomId = (String) roomKeys.toArray()[0];
        assertEquals(0, ((GameRoom)server.rooms.get(onlineRoomId)).point);
    }

    @Test
    void testPlayFriendWithWithThreeClient() throws InterruptedException {
        client1.send("play create");
        assertEquals("play success", client1.getBuffer());

        String[] msg = client1.getBuffer().split(" ");
        assertEquals("id", msg[0]);

        client2.send("play friend " + msg[1]);
        assertEquals("play success", client2.getBuffer());

        assertEquals("start john duke", client1.getBuffer());
        assertEquals("start john duke", client2.getBuffer());

        client3.send("play friend " + msg[1]);
        assertEquals("play cannot find the room", client3.getBuffer());
    }

    @Test
    void testPlayFriendWithWithWrongNumber() throws InterruptedException {
        client1.send("play create");
        assertEquals("play success", client1.getBuffer());

        String[] msg = client1.getBuffer().split(" ");
        assertEquals("id", msg[0]);

        client2.send("play friend " + (Integer.parseInt(msg[1]) + 1));
        assertEquals("play cannot find the room", client2.getBuffer());
    }

    @Test
    void testPlayFriendButWithOnlineNumber() throws InterruptedException {
        client1.send("play online");
        assertEquals("play success", client1.getBuffer());

        client2.send("play online");
        assertEquals("play success", client2.getBuffer());

        assertEquals("start john duke", client1.getBuffer());
        assertEquals("start john duke", client2.getBuffer());

        Set<String> roomKeys = new HashSet<>(server.rooms.keySet());
        roomKeys.remove("lobby");
        roomKeys.remove("auth");

        String onlineRoomId = (String) roomKeys.toArray()[0];
        client3.send("play friend " + onlineRoomId);
        assertEquals("play cannot find the room", client3.getBuffer());
    }

    @Test
    void testPlayButWrongCmd() throws InterruptedException {
        client1.send("play hello");
        assertEquals("play unknown command", client1.getBuffer());
    }

    @Test
    void testLogout() throws InterruptedException {
        client1.send("logout");
        assertEquals(client1.getBuffer(), "logout success");

        // client 1 logout, so the auth room have one client, and the lobby room should have 2 client.
        assertEquals(1, server.rooms.get("auth").clients.size());
        assertEquals(2, server.rooms.get("lobby").clients.size());
    }

    @Test
    void testWrongCommand() throws InterruptedException {
        // client 1 send wrong command
        client1.send("login hello 111");
        assertEquals(client1.getBuffer(), "login unknown command");
    }
}
