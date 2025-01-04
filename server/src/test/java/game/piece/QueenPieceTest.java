package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class QueenPieceTest {
    @Test
    void testEmptyBoard() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();

        // Add all possible moves in 8 directions
        for(int i = 1; i < ChessUtils.BOARD_SIZE/2; i++){
            expected.add(new Position(4+i,4,false));
            expected.add(new Position(4-i,4,false));
            expected.add(new Position(4,4+i,false));
            expected.add(new Position(4,4-i,false));
            expected.add(new Position(4+i,4+i,false));
            expected.add(new Position(4-i,4-i,false));
            expected.add(new Position(4-i,4+i,false));
            expected.add(new Position(4+i,4-i,false));
        }
        expected.add(new Position(0,4,false));
        expected.add(new Position(4,0,false));
        expected.add(new Position(0,0,false));
        board[4][4] = new QueenPiece(PieceColor.WHITE);
        List<Position> result = QueenPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCaptureOpponentPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();

        // Setup the board
        board[4][4] = new QueenPiece(PieceColor.WHITE);
        board[6][6] = new PawnPiece(PieceColor.BLACK);      // Opponent piece to capture
        board[5][5] = new RookPiece(PieceColor.WHITE);      // Friendly piece blocking
        board[3][3] = new RookPiece(PieceColor.WHITE);
        board[3][4] = new PawnPiece(PieceColor.BLACK);
        board[5][4] = new PawnPiece(PieceColor.BLACK);
        board[4][3] = new PawnPiece(PieceColor.BLACK);
        board[4][5] = new PawnPiece(PieceColor.BLACK);
        board[5][3] = new PawnPiece(PieceColor.BLACK);
        board[3][5] = new PawnPiece(PieceColor.BLACK);
        // Add valid moves until blocked
        expected.add(new Position(4, 3, true)); // Capture opponent piece
        expected.add(new Position(3, 4, true)); // Capture opponent piece
        expected.add(new Position(3, 5, true)); // Capture opponent piece
        expected.add(new Position(5, 3, true)); // Capture opponent piece
        expected.add(new Position(4, 5, true)); // Capture opponent piece
        expected.add(new Position(5, 4, true)); // Capture opponent piece


        List<Position> result = QueenPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBlockedBySameColor() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();

        // Setup pieces
        board[4][4] = new QueenPiece(PieceColor.WHITE);
        board[6][4] = new PawnPiece(PieceColor.WHITE);    // Block vertical
        board[4][6] = new RookPiece(PieceColor.WHITE);    // Block horizontal
        board[6][6] = new BishopPiece(PieceColor.WHITE);  // Block diagonal
        board[4][3] = new PawnPiece(PieceColor.WHITE);
        board[3][3] = new PawnPiece(PieceColor.WHITE);
        board[3][4] = new PawnPiece(PieceColor.WHITE);
        board[5][3] = new PawnPiece(PieceColor.WHITE);
        board[3][5] = new PawnPiece(PieceColor.WHITE);
        // Add only moves before blocked positions
        for (int i = 1; i < 2; i++) {
            expected.add(new Position(4 + i, 4, false));     // Vertical down
            expected.add(new Position(4, 4 + i, false));     // Horizontal right
            expected.add(new Position(4 + i, 4 + i, false)); // Diagonal down-right
        }

        List<Position> result = QueenPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCornerPosition() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();

        // Setup board
        board[0][0] = new QueenPiece(PieceColor.WHITE);
        board[1][1] = new PawnPiece(PieceColor.WHITE);    // Block diagonal

        // Add all possible moves from corner
        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
            expected.add(new Position(i, 0, false));     // Vertical down
            expected.add(new Position(0, i, false));     // Horizontal right
        }

        List<Position> result = QueenPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

//    @Test
//    void testEdgePosition() {
//        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expected = new ArrayList<>();
//
//        // Setup board
//        board[0][4] = new QueenPiece(PieceColor.WHITE);
//
//        // Add moves from edge position
//        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
//            expected.add(new Position(i, 4, false));         // Vertical down
//            expected.add(new Position(0, 4 + i, false));     // Horizontal right
//            expected.add(new Position(0, 4 - i, false));     // Horizontal left
//            expected.add(new Position(i, 4 + i, false));     // Diagonal down-right
//            expected.add(new Position(i, 4 - i, false));     // Diagonal down-left
//        }
//
//        List<Position> result = QueenPiece.getPossibleMoves(4, 0, board);
//        PositionTest.assertPosition(expected, result);
//    }

    @Test
    void testSurroundedByOpponents() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();

        // Setup board with queen surrounded by opponents
        board[4][4] = new QueenPiece(PieceColor.WHITE);
        board[3][3] = new PawnPiece(PieceColor.BLACK);
        board[3][4] = new PawnPiece(PieceColor.BLACK);
        board[3][5] = new PawnPiece(PieceColor.BLACK);
        board[4][3] = new PawnPiece(PieceColor.BLACK);
        board[4][5] = new PawnPiece(PieceColor.BLACK);
        board[5][3] = new PawnPiece(PieceColor.BLACK);
        board[5][4] = new PawnPiece(PieceColor.BLACK);
        board[5][5] = new PawnPiece(PieceColor.BLACK);

        // Add all capture moves
        expected.add(new Position(3, 3, true));
        expected.add(new Position(3, 4, true));
        expected.add(new Position(3, 5, true));
        expected.add(new Position(4, 3, true));
        expected.add(new Position(4, 5, true));
        expected.add(new Position(5, 3, true));
        expected.add(new Position(5, 4, true));
        expected.add(new Position(5, 5, true));

        List<Position> result = QueenPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }
}