package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class KingPieceTest {
    @Test
    void testEmptyBoard() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1)) {
                    expected.add(new Position(x, y, false));
                }
            }
        }
        board[1][1] = new KingPiece(PieceColor.WHITE);
        List<Position> result = KingPiece.getPossibleMoves(1, 1, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCaptureOpponentPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new KingPiece(PieceColor.BLACK);
        board[1][1] = new KnightPiece(PieceColor.WHITE);
        expected.add(new Position(0, 1, false));
        expected.add(new Position(1, 0, false));
        expected.add(new Position(1, 1, true));
        List<Position> result = KingPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBlockedBySameColorPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new KingPiece(PieceColor.WHITE);
        board[1][1] = new KingPiece(PieceColor.WHITE);
        expected.add(new Position(0, 1, false));
        expected.add(new Position(1, 0, false));
        List<Position> result = KingPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCornerPosition() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new KingPiece(PieceColor.WHITE);
        board[0][1] = new KingPiece(PieceColor.WHITE);
        board[1][0] = new KingPiece(PieceColor.WHITE);
        board[1][1] = new KingPiece(PieceColor.WHITE);
        List<Position> result = KingPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

//    @Test
//    void testRookKingInteraction() {
//        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expected = new ArrayList<>();
//        board[0][4] = new KingPiece(PieceColor.BLACK);
//        board[0][7] = new RookPiece(PieceColor.BLACK);
//        board[1][4] = new PawnPiece(PieceColor.BLACK);
//        board[0][3] = new QueenPiece(PieceColor.BLACK);
//        expected.add(new Position(0, 5, false));
//        expected.add(new Position(0, 3, false));
//        expected.add(new Position(1, 3, false));
//        expected.add(new Position(1, 4, false));
//        expected.add(new Position(1, 5, false));
//        List<Position> result = KingPiece.getPossibleMoves(0, 4, board);
//        PositionTest.assertPosition(expected, result);
//    }

    @Test
    void testBoundaryEdges() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = new ArrayList<>();
        board[0][0] = new KingPiece(PieceColor.WHITE);
        expected.add(new Position(0, 1, false));
        expected.add(new Position(1, 0, false));
        expected.add(new Position(1, 1, false));
        List<Position> result = KingPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testNoPossibleMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][0] = new KingPiece(PieceColor.WHITE);
        board[0][1] = new KingPiece(PieceColor.WHITE);
        board[1][0] = new KingPiece(PieceColor.WHITE);
        board[1][1] = new KingPiece(PieceColor.WHITE);
        List<Position> expected = new ArrayList<>();
        List<Position> result = KingPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }
}
