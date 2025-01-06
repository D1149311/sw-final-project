package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class KnightPieceTest {

    @Test
    void testSurroundedByEnemies() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new KnightPiece(PieceColor.WHITE);
        board[6][5] = new PawnPiece(PieceColor.BLACK);
        board[6][3] = new PawnPiece(PieceColor.BLACK);
        board[5][6] = new PawnPiece(PieceColor.BLACK);
        board[5][2] = new PawnPiece(PieceColor.BLACK);
        board[3][6] = new PawnPiece(PieceColor.BLACK);
        board[3][2] = new PawnPiece(PieceColor.BLACK);
        board[2][5] = new PawnPiece(PieceColor.BLACK);
        board[2][3] = new PawnPiece(PieceColor.BLACK);

        List<Position> expected = List.of(
                new Position(6, 5, true),
                new Position(6, 3, true),
                new Position(5, 6, true),
                new Position(5, 2, true),
                new Position(3, 6, true),
                new Position(3, 2, true),
                new Position(2, 5, true),
                new Position(2, 3, true)
        );
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
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
        board[4][4] = new KnightPiece(PieceColor.WHITE); // 放置騎士
        List<Position> expected = new ArrayList<>(); // 騎士無法移動
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testKnightOnEdgeWithMixedPieces() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][7] = new KnightPiece(PieceColor.WHITE);
        board[2][6] = new PawnPiece(PieceColor.WHITE); // 阻擋
        board[1][5] = new PawnPiece(PieceColor.BLACK); // 可吃

        List<Position> expected = List.of(
                new Position(5, 1, true) // 吃子
        );
        List<Position> result = KnightPiece.getPossibleMoves(7, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testNoPossibleMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new KnightPiece(PieceColor.WHITE);
        board[6][5] = new PawnPiece(PieceColor.WHITE);
        board[6][3] = new PawnPiece(PieceColor.WHITE);
        board[5][6] = new PawnPiece(PieceColor.WHITE);
        board[5][2] = new PawnPiece(PieceColor.WHITE);
        board[3][6] = new PawnPiece(PieceColor.WHITE);
        board[3][2] = new PawnPiece(PieceColor.WHITE);
        board[2][5] = new PawnPiece(PieceColor.WHITE);
        board[2][3] = new PawnPiece(PieceColor.WHITE);

        List<Position> expected = new ArrayList<>(); // 騎士無法移動
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testKnightOnBoundaryCenter() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][4] = new KnightPiece(PieceColor.BLACK);

        List<Position> expected = List.of(
                new Position(2, 1, false),
                new Position(3, 2, false),
                new Position(6, 1, false),
                new Position(5, 2, false)
        );
        List<Position> result = KnightPiece.getPossibleMoves(4, 0, board);
        PositionTest.assertPosition(expected, result);
    }
}
