package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

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

//    @Test
//    void testBoardEdges() {
//        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expected = new ArrayList<>();
//        board[0][7] = new BishopPiece(PieceColor.WHITE);
//        for (int i = 1; i < ChessUtils.BOARD_SIZE; i++) {
//            if (0 + i < ChessUtils.BOARD_SIZE && 7 - i >= 0) {
//                expected.add(new Position(0 + i, 7 - i, false));
//            }
//        }
//        List<Position> result = BishopPiece.getPossibleMoves(0, 7, board);
//        PositionTest.assertPosition(expected, result);
//    }
}
