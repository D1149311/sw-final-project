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
//
//    @Test
//    void testPawnCapture() {
//        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expected = new ArrayList<>();
//        board[4][4] = new PawnPiece(PieceColor.WHITE);
//        board[3][3] = new PawnPiece(PieceColor.BLACK); // Diagonal left
//        board[3][5] = new PawnPiece(PieceColor.BLACK); // Diagonal right
//        expected.add(new Position(5, 3, true)); // Capture diagonal left
//        expected.add(new Position(3, 3, true)); // Capture diagonal right
//        expected.add(new Position(4, 3, false)); // Single forward move
//        expected.add(new Position(4, 2, false)); // Double forward move
//
//        List<Position> result = PawnPiece.getPossibleMoves(4, 4, board, null);
//        PositionTest.assertPosition(expected, result);
//    }

    @Test
    void testBlockedPawn() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[1][0] = new PawnPiece(PieceColor.BLACK); // Pawn to test
        board[2][0] = new PawnPiece(PieceColor.BLACK); // Blocking pawn
        List<Position> result = PawnPiece.getPossibleMoves(0, 1, board, null);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testPawnAtEdge() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[1][7] = new PawnPiece(PieceColor.BLACK); // Pawn at the right edge
        expected.add(new Position(7, 2, false)); // Single forward move
        expected.add(new Position(7, 3, false)); // Double forward move
        List<Position> result = PawnPiece.getPossibleMoves(7, 1, board, null);
        PositionTest.assertPosition(expected, result);
    }
    @Test
    void testPawnSurrounded() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new PawnPiece(PieceColor.WHITE);
        board[3][4] = new PawnPiece(PieceColor.WHITE);
        board[3][5] = new PawnPiece(PieceColor.WHITE);
        board[3][3] = new PawnPiece(PieceColor.WHITE);

        List<Position> expected = new ArrayList<>(); // Pawn has no valid moves
        List<Position> result = PawnPiece.getPossibleMoves(4, 4, board, null);
        PositionTest.assertPosition(expected, result);
    }
}