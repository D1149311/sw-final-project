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
//怪怪的，前面兩個應該可以吃 後面兩個應該不能吃
    @Test
    void testPawnCapture() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[4][4] = new PawnPiece(PieceColor.WHITE);
        board[3][3] = new PawnPiece(PieceColor.BLACK); // Diagonal left
        board[3][5] = new PawnPiece(PieceColor.BLACK); // Diagonal right
        expected.add(new Position(5, 3, true)); // Capture diagonal right
        expected.add(new Position(3, 3, true)); // Capture diagonal left
        expected.add(new Position(4, 3, false)); // Single forward move
        expected.add(new Position(4, 2, false)); // Double forward move

        List<Position> result = PawnPiece.getPossibleMoves(4, 4, board, null);
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

    @Test
    void testSetPawnMoved() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new PawnPiece(PieceColor.WHITE);

        // initial state
        assertFalse(((PawnPiece) board[4][4]).hasMoved());

        // update moved state
        ((PawnPiece)board[4][4]).setMoved(true);
        assertTrue(((PawnPiece) board[4][4]).hasMoved());

        // update moved state, won't be happened in real game
        ((PawnPiece)board[4][4]).setMoved(false);
        assertFalse(((PawnPiece) board[4][4]).hasMoved());
    }

    @Test
    void testGetPossibleMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][0] = new PawnPiece(PieceColor.WHITE);

        // move forward one grid, and out of board, won't be happened in real game because the pawn will change to another piece
        List<Position> expected = new ArrayList<>();
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(0, 0, board, new Position(1, 1, false)));

        // has moved before
        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[3][0] = new PawnPiece(PieceColor.WHITE);
        board[1][0] = new PawnPiece(PieceColor.WHITE);
        expected = new ArrayList<>();
        expected.add(new Position(0, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(0, 3, board, null));
    }

    @Test
    void testPassantMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[3][4] = new PawnPiece(PieceColor.WHITE);
        ((PawnPiece)board[3][4]).setMoved(true);
        board[3][3] = new PawnPiece(PieceColor.BLACK);
        board[3][5] = new PawnPiece(PieceColor.BLACK);

        // 左側過路兵
        List<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        expected.add(new Position(3, 2, true));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(3, 3, true)));
        assertTrue(PawnPiece.enPassantCapture(4, 3, 3, 2, board, new Position(3, 3, true)));

        // reset board
        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[3][4] = new PawnPiece(PieceColor.WHITE);
        ((PawnPiece)board[3][4]).setMoved(true);
        board[3][3] = new PawnPiece(PieceColor.BLACK);
        board[3][5] = new PawnPiece(PieceColor.BLACK);

        // 左側過路兵，但上一動不合法
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(2, 3, true)));

        // 左側過路兵，但上一動不合法
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(3, 2, true)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 2, board, null));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 2, board, new Position(4, 3, false)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 2, board, new Position(5, 2, false)));

        // 左側過路兵，顏色不對
        board[3][3] = new PawnPiece(PieceColor.WHITE);
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(3, 3, true)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 3, 2, board, new Position(3, 3, true)));
        board[3][3] = new PawnPiece(PieceColor.BLACK);

        // 左側過路兵，棋子不對
        board[3][3] = new KingPiece(PieceColor.WHITE);
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(3, 3, true)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 3, 2, board, new Position(3, 3, true)));
        board[3][3] = new PawnPiece(PieceColor.BLACK);

        // 左側過路兵，沒有棋子
        board[3][3] = null;
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(3, 3, true)));
        board[3][3] = new PawnPiece(PieceColor.BLACK);

        // 右側過路兵
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        expected.add(new Position(5, 2, true));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 3, true)));

        // 右側過路兵，但上一動不合法
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(2, 3, true)));

        // 右側過路兵，但上一動不合法
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 2, true)));

        // 右側過路兵，顏色不對
        board[3][5] = new PawnPiece(PieceColor.WHITE);
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 3, true)));
        board[3][5] = new PawnPiece(PieceColor.BLACK);

        // 右側過路兵，棋子不對
        board[3][5] = new KingPiece(PieceColor.WHITE);
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 3, true)));
        board[3][5] = new PawnPiece(PieceColor.BLACK);

        // 右側過路兵，沒有棋子
        board[3][5] = null;
        expected = new ArrayList<>();
        expected.add(new Position(4, 2, false));
        PositionTest.assertPosition(expected, PawnPiece.getPossibleMoves(4, 3, board, new Position(5, 3, true)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 2, board, new Position(5, 3, true)));
        board[3][5] = new PawnPiece(PieceColor.BLACK);

        // 往前一格
        assertFalse(PawnPiece.enPassantCapture(4, 3, 4, 2, board, new Position(5, 3, true)));
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 1, board, new Position(5, 3, true)));
        assertFalse(PawnPiece.enPassantCapture(5, 3, 6, 5, board, new Position(5, 3, true)));

        // 左側過路兵，有棋子擋住
        board[2][5] = new KingPiece(PieceColor.WHITE);
        assertFalse(PawnPiece.enPassantCapture(4, 3, 5, 2, board, new Position(3, 3, true)));
    }

    @Test
    void testGetAttackRange() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[2][0] = new PawnPiece(PieceColor.WHITE);
        board[2][7] = new PawnPiece(PieceColor.WHITE);

        List<Position> expected = new ArrayList<>();
        expected.add(new Position(1, 1, false));
        PositionTest.assertPosition(expected, PawnPiece.getAttackRange(0, 2, board));

        expected = new ArrayList<>();
        expected.add(new Position(6, 1, false));
        PositionTest.assertPosition(expected, PawnPiece.getAttackRange(7, 2, board));

        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[2][0] = new PawnPiece(PieceColor.BLACK);
        board[2][7] = new PawnPiece(PieceColor.BLACK);

        expected = new ArrayList<>();
        expected.add(new Position(1, 3, false));
        PositionTest.assertPosition(expected, PawnPiece.getAttackRange(0, 2, board));
    }
}