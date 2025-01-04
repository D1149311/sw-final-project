package network;

import game.*;
import game.piece.KingPiece;
import game.piece.PawnPiece;
import game.piece.RookPiece;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.*;
import storage.IUserService;
import storage.User;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyClient extends WebSocketClient {
    private final List<String> buffer = new ArrayList<>();

    public MyClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        this.buffer.add(s);
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public String getBuffer() throws InterruptedException {
        while (buffer.isEmpty()) {
            Thread.sleep(1000);
        }

        return buffer.removeFirst();
    }
}

class ChessWebSocketTest {
    private static ChessWebSocket server;
    private MyClient client1, client2;
    private static IUserService userService;
    private static IGameService gameService;
    private static IChessService chessService;

    @BeforeAll
    static void setUp() throws Exception {
        // mock database
        userService = mock(IUserService.class);
        when(userService.findUserById("duke")).thenReturn(new User("duke", "123", "0"));
        when(userService.findUserById("john")).thenReturn(new User("john", "123", "0"));
        when(userService.findTop10ByOrderByPointDesc()).thenReturn(List.of(new User("duke", "123", "100"), new User("john", "123", "90")));

        // mock a chess game
        ChessPiece[][] chessPieces = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        chessPieces[0][4] = new KingPiece(PieceColor.BLACK);
        chessPieces[1][3] = new PawnPiece(PieceColor.WHITE);

        chessService = mock(IChessService.class);
        when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
        when(chessService.getBoard()).thenReturn(chessPieces);
        when(chessService.getPossibleMoves(4, 0)).thenReturn(List.of(new Position[]{}));
        when(chessService.getPossibleMoves(2, 1)).thenReturn(List.of(new Position[]{}));
        when(chessService.getPossibleMoves(3, 1)).thenReturn(List.of(new Position[]{new Position(4, 0, true), new Position(3, 0, false)}));
        when(chessService.isCheckmate()).thenReturn(false);
        when(chessService.isDraw()).thenReturn(false);
        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(false);
        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(false);
        when(chessService.move(1, 1, 3, 3)).thenReturn(false);
        when(chessService.move(1, 1, 2, 3)).thenReturn(true);

        // mock game factory
        gameService = mock(IGameService.class);
        when(gameService.createGame()).thenReturn(chessService);

        // start server
        server = new ChessWebSocket(1111, userService, gameService);
        server.start();
    }

    @BeforeEach
    void setUpClient() throws InterruptedException, URISyntaxException {
        // create 2 client and connect to the server
        client1 = new MyClient(new URI("ws://localhost:1111"));
        client1.connectBlocking();

        client2 = new MyClient(new URI("ws://localhost:1111"));
        client2.connectBlocking();
    }

    @AfterEach
    void tearDownClient() throws Exception {
        // close connections
        if (client1 != null) client1.closeBlocking();
        if (client2 != null) client2.closeBlocking();
    }

    @AfterAll
    static void tearDown() throws IOException, InterruptedException {
        // stop server
        if (server != null) server.stop();
    }

    @Test
    void testLogin() throws InterruptedException {
        // 2 client connected to the server, so the auth room should have 2 clients
        assertEquals(2, server.rooms.get("auth").clients.size());

        // client 1 login with wrong username
        client1.send("login hello 1");
        assertEquals(client1.getBuffer(), "login wrong username or password");

        // client 1 login with wrong password
        client1.send("login duke 111");
        assertEquals(client1.getBuffer(), "login wrong username or password");

        // client 1 login with true username and password
        client1.send("login duke 123");
        assertEquals(client1.getBuffer(), "login success");
        assertEquals(1, server.rooms.get("lobby").clients.size());

        // client 1 logout
        client1.send("logout");
        assertEquals(client1.getBuffer(), "logout success");
        assertEquals(0, server.rooms.get("lobby").clients.size());
    }

    @Test
    void testRegister() throws InterruptedException {
        assertEquals(2, server.rooms.get("auth").clients.size());

        // client 1 signup with the username that has already exists
        client1.send("signup duke 123");
        assertEquals(client1.getBuffer(), "signup username has already token");

        // client 1 signup with the username that has not exists
        client1.send("signup hello 111");
        assertEquals(client1.getBuffer(), "signup success");
        verify(userService, times(1)).createUser("hello", "111");

        // client 1 send wrong command
        client1.send("abc hello 111");
        assertEquals(client1.getBuffer(), "abc unknown command");
    }

    @Test
    void testTop10List() throws InterruptedException {
        // login and enter the lobby
        client1.send("login duke 123");
        client1.getBuffer();
        assertEquals(1, server.rooms.get("lobby").clients.size());

        // get the top ten list, data is setup with mockito
        client1.send("top10");
        assertEquals("top10 duke 100 john 90 ", client1.getBuffer());
    }

    @Test
    void testPlayOnline() throws InterruptedException {
        // client 1 login
        client1.send("login duke 123");
        client1.getBuffer();
        assertEquals(1, server.rooms.get("lobby").clients.size());

        // client 1 start playing
        client1.send("play online");
        assertEquals("play success", client1.getBuffer());

        // client 1 start playing again, then get error
        client1.send("play friend");
        assertEquals("play unknown command", client1.getBuffer());

        // client 2 login
        client2.send("login john 123");
        client2.getBuffer();
        assertEquals(2, server.rooms.get("lobby").clients.size());

        // client 2 send wrong command
        client2.send("hello online");
        assertEquals("hello unknown command", client2.getBuffer());

        // client 2 send wrong command
        client2.send("play hello");
        assertEquals("play unknown command", client2.getBuffer());

        // client 2 start playing
        client2.send("play online");
        assertEquals("play success", client2.getBuffer());

        // client 1 and client 2 will start the chess game
        assertEquals("start", client1.getBuffer().split(" ")[0]);
        assertEquals("start", client2.getBuffer().split(" ")[0]);
        assertEquals(0, server.rooms.get("lobby").clients.size());

        // client 2 is white, and client 1 is black, so client 2 first
        // get the possible position with no chess on the grid
        client2.send("possible 2 1");
        assertEquals("possible ", client2.getBuffer());

        // get the possible position with pawn on it
        client2.send("possible 3 1");
        assertEquals("possible 4 0 true 3 0 false ", client2.getBuffer());

        // get the possible with black chess on it
        client2.send("possible 4 0");
        assertEquals("possible ", client2.getBuffer());

        // not client1's turn
        client1.send("possible 2 1");
        assertEquals("possible ", client1.getBuffer());

        // not client1's turn
        client1.send("move 1 1 2 3");
        assertEquals("move invalid", client1.getBuffer());

        // invalid move
        client2.send("move 1 1 3 3");
        assertEquals("move invalid", client2.getBuffer());

        // invalid command
        client2.send("mv 1 1 3 3");
        assertEquals("mv unknown command", client2.getBuffer());

        // valid move
        client2.send("move 1 1 2 3");
        assertEquals("move 1 1 2 3", client1.getBuffer());
        assertEquals("move 1 1 2 3", client2.getBuffer());

        // threat
        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(true);
        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(true);
        client2.send("move 1 1 2 3");
        assertEquals("move 1 1 2 3", client1.getBuffer());
        assertEquals("move 1 1 2 3", client2.getBuffer());
        assertEquals("threat BLACK", client1.getBuffer());
        assertEquals("threat BLACK", client2.getBuffer());
        assertEquals("threat WHITE", client1.getBuffer());
        assertEquals("threat WHITE", client2.getBuffer());
        when(chessService.isCheckmate()).thenReturn(false);

        // draw
        when(chessService.isDraw()).thenReturn(true);
        client2.send("move 1 1 2 3");
        assertEquals("move 1 1 2 3", client1.getBuffer());
        assertEquals("move 1 1 2 3", client2.getBuffer());
        assertEquals("end The game is a draw!", client1.getBuffer());
        assertEquals("end The game is a draw!", client2.getBuffer());
        when(chessService.isDraw()).thenReturn(false);
    }

    @Test
    void testPlayOnlineCheckmate() throws InterruptedException {
        // client 1 login
        client1.send("login duke 123");
        client1.getBuffer();
        assertEquals(1, server.rooms.get("lobby").clients.size());

        // client 1 start playing
        client1.send("play online");
        assertEquals("play success", client1.getBuffer());

        // client 2 login
        client2.send("login john 123");
        client2.getBuffer();
        assertEquals(2, server.rooms.get("lobby").clients.size());

        // client 2 start playing
        client2.send("play online");
        assertEquals("play success", client2.getBuffer());

        // client 1 and client 2 will start the chess game
        assertEquals("start", client1.getBuffer().split(" ")[0]);
        assertEquals("start", client2.getBuffer().split(" ")[0]);
        assertEquals(0, server.rooms.get("lobby").clients.size());

        // checkmate
        when(chessService.isCheckmate()).thenReturn(true);
        client2.send("move 1 1 2 3");
        assertEquals("move 1 1 2 3", client1.getBuffer());
        assertEquals("move 1 1 2 3", client2.getBuffer());
        assertEquals("end WHITE is checkmated!", client1.getBuffer());
        assertEquals("end WHITE is checkmated!", client2.getBuffer());
        when(chessService.isCheckmate()).thenReturn(false);
    }

    @Test
    void testErrorMessage() throws InterruptedException {
        // client 2 send wrong message and it can keep running
        client2.send("login john");

        client2.send("login");

        client2.send("login ");

        client2.send("login john 123");
        client2.getBuffer();
        assertEquals(1, server.rooms.get("lobby").clients.size());
    }
}