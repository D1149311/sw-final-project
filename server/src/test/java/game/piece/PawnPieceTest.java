package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PawnPieceTest {
    @Test
    void testBlackPawnInitialMove() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        expected.add(new Position(0, 2, false)); // Single forward move
        expected.add(new Position(0, 3, false)); // Double forward move
        board[1][0] = new PawnPiece(PieceColor.BLACK);
        List<Position> result = PawnPiece.getPossibleMoves(0, 1, board, null);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testWhitePawnInitialMove() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        expected.add(new Position(0, 5, false)); // Single forward move
        expected.add(new Position(0, 4, false)); // Double forward move
        board[6][0] = new PawnPiece(PieceColor.WHITE);
        List<Position> result = PawnPiece.getPossibleMoves(0, 6, board, null);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testPawnCapture() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[2][1] = new PawnPiece(PieceColor.WHITE);
        board[1][0] = new PawnPiece(PieceColor.BLACK);
        expected.add(new Position(0, 1, true)); // Capture left diagonal
        expected.add(new Position(1, 1, false)); // Capture right diagonal
        expected.add(new Position(1, 0, false)); // Single forward move
        List<Position> result = PawnPiece.getPossibleMoves(1, 2, board, null);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBlockedPawn() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[1][0] = new PawnPiece(PieceColor.BLACK); // Pawn to test
        board[2][0] = new PawnPiece(PieceColor.BLACK); // Blocking pawn
        List<Position> result = PawnPiece.getPossibleMoves(0, 1, board, null);
        PositionTest.assertPosition(expected, result);
    }

//    @Test
//    void testEnPassant() {
//        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expected = new ArrayList<>();
//        board[3][4] = new PawnPiece(PieceColor.WHITE);
//        board[3][5] = new PawnPiece(PieceColor.BLACK);
//        expected.add(new Position(4, 4, false)); // Regular forward move
//        expected.add(new Position(4, 5, true));  // En passant capture
//        List<Position> result = PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 3, false));
//        PositionTest.assertPosition(expected, result);
//    }

    @Test
    void testPawnPromotion() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[6][0] = new PawnPiece(PieceColor.BLACK);
        board[7][1] = new PawnPiece(PieceColor.WHITE);
        expected.add(new Position(0, 7, false)); // Forward move leading to promotion
        expected.add(new Position(1, 7, true));  // Capture leading to promotion
        List<Position> result = PawnPiece.getPossibleMoves(0, 6, board, null);
        PositionTest.assertPosition(expected, result);
    }
}
