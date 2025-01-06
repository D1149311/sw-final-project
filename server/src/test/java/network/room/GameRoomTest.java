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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GameRoomTest {
    private static ChessWebSocket server;
    private MyClient client1, client2;

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

        // start playing
        client1.send("play online");
        assertEquals("play success", client1.getBuffer());

        client2.send("play online");
        assertEquals("play success", client2.getBuffer());

        // notify to start the game
        assertEquals("start john duke", client1.getBuffer());
        assertEquals("start john duke", client2.getBuffer());

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());
    }

    @AfterEach
    void closeClient() throws InterruptedException {
        if (client1 != null) client1.closeBlocking();
        if (client2 != null) client2.closeBlocking();
    }

    @AfterAll
    static void tearDown() throws IOException, InterruptedException {
//        if (server != null) server.stop();
    }

    @Test
    void testPossibleMove() throws InterruptedException {
        // not your turn
        client1.send("possible 3 1");
        assertEquals("possible ", client1.getBuffer());  // not your turn

        // get possible moves
        client2.send("possible 3 1");
        assertEquals("possible 4 0 true 3 0 false ", client2.getBuffer());

        // no piece on it
        client2.send("possible 2 1");
        assertEquals("possible ", client2.getBuffer());
    }

    @Test
    void testMove() throws InterruptedException {
        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        // cannot move
        client2.send("move 3 1 2 3");
        assertEquals("move invalid", client2.getBuffer());

        // not your turn
        client1.send("move 3 1 2 3");
        assertEquals("move invalid", client1.getBuffer());
    }

    @Test
    void testCheckmate() throws InterruptedException {
        when(chessService.isCheckmate()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("end WHITE is checkmated!", client1.getBuffer());
        assertEquals("end WHITE is checkmated!", client2.getBuffer());

        // reset
        when(chessService.isCheckmate()).thenReturn(false);
    }

    @Test
    void testDraw() throws InterruptedException {
        when(chessService.isDraw()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("end The game is a draw!", client1.getBuffer());
        assertEquals("end The game is a draw!", client2.getBuffer());

        // reset
        when(chessService.isDraw()).thenReturn(false);
    }

    @Test
    void testKingThreat() throws InterruptedException {
        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("threat BLACK", client1.getBuffer());
        assertEquals("threat BLACK", client2.getBuffer());

        // reset
        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(false);
        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("threat WHITE", client1.getBuffer());
        assertEquals("threat WHITE", client2.getBuffer());

        // reset
        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(false);
    }

    @Test
    void testPromote() throws InterruptedException {
        when(chessService.requestingPromote()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("promote", client2.getBuffer());

        // not your turn
        client1.send("promote QUEEN");
        assertEquals("promote unknown command", client1.getBuffer());

        // your turn
        when(chessService.getTurn()).thenReturn(PieceColor.BLACK);
        client1.send("promote QUEEN");

        // not your turn
        when(chessService.getTurn()).thenReturn(PieceColor.BLACK);
        client2.send("promote QUEEN");
        assertEquals("promote unknown command", client2.getBuffer());

        // your turn
        Thread.sleep(100);
        when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
        client2.send("promote ABC");
        client2.send("promote QUEEN");
        client2.send("promote BISHOP");
        client2.send("promote KNIGHT");

        Thread.sleep(100);
        when(chessService.requestingPromote()).thenReturn(false);
        client2.send("promote ROOK");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());
    }

    @Test
    void testPromoteWithCheckmate() throws InterruptedException {
        when(chessService.requestingPromote()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("promote", client2.getBuffer());
        when(chessService.isCheckmate()).thenReturn(true);

        // not your turn
        when(chessService.getTurn()).thenReturn(PieceColor.BLACK);
        client2.send("promote QUEEN");
        assertEquals("promote unknown command", client2.getBuffer());

        // your turn
        Thread.sleep(100);
        when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
        when(chessService.requestingPromote()).thenReturn(false);
        client2.send("promote ROOK");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("end WHITE is checkmated!", client1.getBuffer());
        assertEquals("end WHITE is checkmated!", client2.getBuffer());

        when(chessService.isCheckmate()).thenReturn(false);
    }

    @Test
    void testPromoteWithDraw() throws InterruptedException {
        when(chessService.requestingPromote()).thenReturn(true);
        when(chessService.getTurn()).thenReturn(PieceColor.BLACK);

        // can move
        client1.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("promote", client1.getBuffer());
        when(chessService.isDraw()).thenReturn(true);

        // not your turn
        client2.send("promote BISHOP");
        assertEquals("promote unknown command", client2.getBuffer());

        // your turn
        when(chessService.requestingPromote()).thenReturn(false);
        client1.send("promote BISHOP");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("end The game is a draw!", client1.getBuffer());
        assertEquals("end The game is a draw!", client2.getBuffer());

        when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
        when(chessService.isDraw()).thenReturn(false);
    }

    @Test
    void testPromoteWithWhiteThreat() throws InterruptedException {
        when(chessService.requestingPromote()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("promote", client2.getBuffer());
        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(true);

        // your turn
        when(chessService.requestingPromote()).thenReturn(false);
        client2.send("promote BISHOP");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("threat WHITE", client1.getBuffer());
        assertEquals("threat WHITE", client2.getBuffer());

        when(chessService.isKingThreatened(PieceColor.WHITE)).thenReturn(false);
    }

    @Test
    void testPromoteWithBlackThreat() throws InterruptedException {
        when(chessService.requestingPromote()).thenReturn(true);

        // can move
        client2.send("move 3 1 4 0");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("promote", client2.getBuffer());
        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(true);

        // your turn
        when(chessService.requestingPromote()).thenReturn(false);
        client2.send("promote BISHOP");

        // get the board
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client1.getBuffer());
        assertEquals("move null null null null null null null null BLACK KING null null null null null null null null null null null null WHITE PAWN null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null null ", client2.getBuffer());

        assertEquals("threat BLACK", client1.getBuffer());
        assertEquals("threat BLACK", client2.getBuffer());

        when(chessService.isKingThreatened(PieceColor.BLACK)).thenReturn(false);
    }

    @Test
    void testClient1Leave() throws InterruptedException {
        when(userService.findUserById("duke")).thenReturn(new User("duke", "123", "100"));
        when(userService.findUserById("john")).thenReturn(new User("john", "123", "90"));
        when(userService.findUserById("ruby")).thenReturn(new User("ruby", "111", "0"));
        when(userService.findTop10ByOrderByPointDesc()).thenReturn(List.of(new User("duke", "123", "100"), new User("john", "123", "90"), new User("ruby", "111", "0")));

        client1.closeBlocking();

        assertEquals("end duke leave the room", client2.getBuffer());

        // add 100 point
        assertEquals("190", userService.findUserById("john").point);
    }

    @Test
    void testClient2Leave() throws InterruptedException {
        client2.closeBlocking();

        assertEquals("end john leave the room", client1.getBuffer());
    }

    @Test
    void testClientLeaveWhenMatching() throws InterruptedException, URISyntaxException {
        client1.closeBlocking();
        client1 = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client1.connectBlocking();
        client1.send("login duke 123");
        client1.getBuffer();

        client1.send("play create");
        client1.getBuffer();
        client1.getBuffer();
        client1.closeBlocking();

        Thread.sleep(100);
        assertEquals(2, server.rooms.size());
    }

    @Test
    void testSendCmdBeforeGameStart() throws InterruptedException, URISyntaxException {
        client1.closeBlocking();
        client1 = new MyClient(new URI("ws://localhost:" + MyServer.port));
        client1.connectBlocking();
        client1.send("login duke 123");
        client1.getBuffer();

        client1.send("play create");
        client1.getBuffer();
        client1.getBuffer();

        client1.send("possible 2 1");
        assertEquals("possible unknown command", client1.getBuffer());
    }

    @Test
    void testUnknownCommand() throws InterruptedException {
        // not your turn
        client1.send("hello world");
        assertEquals("hello unknown command", client1.getBuffer());

        // your turn
        when(chessService.getTurn()).thenReturn(PieceColor.BLACK);
        client1.send("hello world");
        assertEquals("hello unknown command", client1.getBuffer());

        // not your turn
        client2.send("hello world");
        assertEquals("hello unknown command", client2.getBuffer());

        // your turn
        when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
        client2.send("hello world");
        assertEquals("hello unknown command", client2.getBuffer());
    }
}
