package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class KnightPieceTest {
    @Test
    void testEmptyBoard() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = List.of(
                new Position(5, 6, false),
                new Position(3, 6, false),
                new Position(5, 2, false),
                new Position(3, 2, false),
                new Position(6, 5, false),
                new Position(6, 3, false),
                new Position(2, 5, false),
                new Position(2, 3, false)
        );
        board[4][4] = new KnightPiece(PieceColor.BLACK);
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCaptureOpponentPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expected = List.of(
                new Position(5, 6, false),
                new Position(3, 6, false),
                new Position(5, 2, false),
                new Position(3, 2, false),
                new Position(6, 5, false),
                new Position(6, 3, false),
                new Position(2, 5, true),
                new Position(2, 3, false)
        );
        board[4][4] = new KnightPiece(PieceColor.WHITE);
        board[2][5] = new KnightPiece(PieceColor.BLACK);
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBlockedBySameColorPiece() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[4][4] = new KnightPiece(PieceColor.WHITE);
        board[6][5] = new KnightPiece(PieceColor.WHITE);
        board[6][3] = new KnightPiece(PieceColor.WHITE);
        board[5][6] = new KnightPiece(PieceColor.WHITE);
        board[5][2] = new KnightPiece(PieceColor.WHITE);
        board[3][6] = new KnightPiece(PieceColor.WHITE);
        board[3][2] = new KnightPiece(PieceColor.WHITE);
        board[2][5] = new KnightPiece(PieceColor.WHITE);
        board[2][3] = new KnightPiece(PieceColor.WHITE);

        List<Position> expected = new ArrayList<>();
        List<Position> result = KnightPiece.getPossibleMoves(4, 4, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testCornerPosition() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][0] = new KnightPiece(PieceColor.WHITE);

        List<Position> expected = List.of(
                new Position(2, 1, false),
                new Position(1, 2, false)
        );
        List<Position> result = KnightPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expected, result);
    }

    @Test
    void testBoundaryEdges() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        board[0][7] = new KnightPiece(PieceColor.BLACK);

        List<Position> expected = List.of(
                new Position(2, 6, false),
                new Position(1, 5, false)
        );
        List<Position> result = KnightPiece.getPossibleMoves(0, 7, board);
        PositionTest.assertPosition(expected, result);
    }
}
