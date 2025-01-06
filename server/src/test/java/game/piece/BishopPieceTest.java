package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BishopPieceTest {

    @Test
    void testEmptyBoard() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
            expected.add(new Position(i, i, false));
        }
        board[0][0] = new BishopPiece(PieceColor.WHITE);
        List<Position> result = BishopPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCaptureOpponentPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new BishopPiece(PieceColor.WHITE);
        board[3][3] = new BishopPiece(PieceColor.BLACK);
        for (int i = 1; i < 4; i++) {
            expected.add(new Position(i, i, i == 3));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBlockedBySameColorPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new BishopPiece(PieceColor.WHITE);
        board[3][3] = new BishopPiece(PieceColor.WHITE);
        for (int i = 1; i < 3; i++) {
            expected.add(new Position(i, i, false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCornerPosition() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[7][7] = new BishopPiece(PieceColor.WHITE);
        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
            expected.add(new Position(7 - i, 7 - i, false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(7, 7, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testNoPossibleMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][0] = new BishopPiece(PieceColor.WHITE);
        board[1][1] = new BishopPiece(PieceColor.WHITE);
        List<Position> expected = new ArrayList<>();
        List<Position> result = BishopPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testFullBoardScenario() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        for (int i = 0; i < ChessUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessUtils.BOARD_SIZE; j++) {
                board[i][j] = new PawnPiece(PieceColor.WHITE); // 填滿棋盤
            }
        }
        board[4][4] = new BishopPiece(PieceColor.WHITE); // 放置主教
        List<Position> expected = new ArrayList<>(); // 主教無法移動
        List<Position> result = BishopPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testSurroundedByEnemies() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new BishopPiece(PieceColor.WHITE);
        board[5][5] = new PawnPiece(PieceColor.BLACK);
        board[3][3] = new RookPiece(PieceColor.BLACK);
        board[5][3] = new KnightPiece(PieceColor.BLACK);
        board[3][5] = new QueenPiece(PieceColor.BLACK);

        List<Position> expected = List.of(
                new Position(5, 5, true), // Down-right
                new Position(3, 3, true), // Up-left
                new Position(5, 3, true), // Down-left
                new Position(3, 5, true)  // Up-right
        );
        List<Position> result = BishopPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testEdgeBoundary() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[7][0] = new BishopPiece(PieceColor.WHITE);
        List<Position> expected = new ArrayList<>();
        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
            expected.add(new Position(i, 7-i, false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0, 7, board);
        PositionTest.assertPosition(expected, result);
    }
}
