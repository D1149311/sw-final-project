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
    /*PiTest*/
    @Test
    void testInvalidPosition() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][0] = new KingPiece(PieceColor.WHITE);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> KingPiece.getPossibleMoves(-1, 0, board));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> KingPiece.getPossibleMoves(0, -1, board));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> KingPiece.getPossibleMoves(8, 8, board));
    }
    @Test
    void testFullBoardScenario() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        for (int i = 0; i < ChessUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessUtils.BOARD_SIZE; j++) {
                board[i][j] = new PawnPiece(PieceColor.WHITE); // 填滿棋盤
            }
        }
        board[4][4] = new KingPiece(PieceColor.WHITE); // 放置國王
        List<Position> expected = new ArrayList<>(); // 國王無法移動
        List<Position> result = KingPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }
    @Test
    void testSurroundedByEnemies() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new KingPiece(PieceColor.WHITE);
        board[3][3] = new PawnPiece(PieceColor.BLACK);
        board[3][4] = new RookPiece(PieceColor.BLACK);
        board[3][5] = new KnightPiece(PieceColor.BLACK);
        board[4][3] = new BishopPiece(PieceColor.BLACK);
        board[4][5] = new QueenPiece(PieceColor.BLACK);
        board[5][3] = new PawnPiece(PieceColor.BLACK);
        board[5][4] = new RookPiece(PieceColor.BLACK);
        board[5][5] = new KnightPiece(PieceColor.BLACK);

        List<Position> expected = List.of(
                new Position(3, 3, true), new Position(3, 4, true), new Position(3, 5, true),
                new Position(4, 3, true),                        /* King at (4, 4) */
                new Position(4, 5, true), new Position(5, 3, true),
                new Position(5, 4, true), new Position(5, 5, true)
        );
        List<Position> result = KingPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }
    @Test
    void testKingNearBoundary() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[7][7] = new KingPiece(PieceColor.WHITE);
        List<Position> expected = List.of(
                new Position(6, 7, false), // 左
                new Position(6, 6, false), // 左下
                new Position(7, 6, false)  // 下
        );
        List<Position> result = KingPiece.getPossibleMoves(7, 7, board);
        PositionTest.assertPosition(expected, result);
    }


}
