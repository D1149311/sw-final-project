package network;

import game.*;
import game.piece.KingPiece;
import game.piece.PawnPiece;
import org.checkerframework.checker.units.qual.C;
import storage.IUserService;
import storage.User;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyServer {
    private static ChessWebSocket myServer = null;
    private static IUserService userService;
    private static IGameService gameService;
    private static IChessService chessService;
    public static final int port = 1115;

    private MyServer() {

    }

    public static ChessWebSocket getServer() {
        if (myServer == null) {
            // mock database
            userService = mock(IUserService.class);
            when(userService.findUserById("duke")).thenReturn(new User("duke", "123", "0"));
            when(userService.findUserById("john")).thenReturn(new User("john", "123", "0"));
            when(userService.findUserById("ruby")).thenReturn(new User("ruby", "111", "0"));
            when(userService.findTop10ByOrderByPointDesc()).thenReturn(List.of(new User("duke", "123", "100"), new User("john", "123", "90"), new User("ruby", "111", "0")));

            // mock a chess game
            ChessPiece[][] chessPieces = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
            chessPieces[0][4] = new KingPiece(PieceColor.BLACK);
            chessPieces[1][3] = new PawnPiece(PieceColor.WHITE);

            chessService = mock(IChessService.class);
            when(chessService.getTurn()).thenReturn(PieceColor.WHITE);
            when(chessService.getBoard()).thenReturn(chessPieces);
            when(chessService.getPossibleMoves(4, 0)).thenReturn(List.of(new Position[]{}));  // not your turn
            when(chessService.getPossibleMoves(2, 1)).thenReturn(List.of(new Position[]{}));  // no chess on it
            when(chessService.getPossibleMoves(3, 1)).thenReturn(List.of(new Position[]{new Position(4, 0, true), new Position(3, 0, false)}));
            when(chessService.move(3, 1, 4, 0)).thenReturn(true);
            when(chessService.move(3, 1, 2, 3)).thenReturn(false);

            // mock game factory
            gameService = mock(IGameService.class);
            when(gameService.createGame()).thenReturn(chessService);

            myServer = new ChessWebSocket(port, userService, gameService);
            myServer.start();
        }

        return myServer;
    }

    public static IUserService getUserService() {
        return MyServer.userService;
    }

    public static IGameService getGameService() {
        return MyServer.gameService;
    }

    public static IChessService getChessService() {
        return MyServer.chessService;
    }
}
