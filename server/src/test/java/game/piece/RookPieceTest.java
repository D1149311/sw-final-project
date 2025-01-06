package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class RookPieceTest {

    @Test
    void testEmptyBoard() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int x = 0; x < 8; x++){
            if(x==3) continue;
            // Add horizontal moves
            expect.add(new Position(x, 3, false));
            // Add vertical moves
            expect.add(new Position(3, x, false));
        }
        empty_board[3][3] = new RookPiece(PieceColor.WHITE);
        List<Position> result = RookPiece.getPossibleMoves(3, 3, empty_board);
        PositionTest.assertPosition(expect, result);
    }

    @Test
    void testCornerPosition() {
        // Test Rook at corner (0,0)
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();

        // Add all horizontal moves from corner
        for(int x = 1; x < 8; x++) {
            expect.add(new Position(x, 0, false));
        }
        // Add all vertical moves from corner
        for(int y = 1; y < 8; y++) {
            expect.add(new Position(0, y, false));
        }

        board[0][0] = new RookPiece(PieceColor.WHITE);
        List<Position> result = RookPiece.getPossibleMoves(0, 0, board);
        PositionTest.assertPosition(expect, result);
    }

    @Test
    void testCaptureMoves() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();

        // Setup captures in all four directions
        expect.add(new Position(3, 4, true)); // North
        expect.add(new Position(4, 3, true)); // East
        expect.add(new Position(3, 2, true)); // South
        expect.add(new Position(2, 3, true)); // West

        // Place white rook
        board[3][3] = new RookPiece(PieceColor.WHITE);
        // Place black pieces to capture
        board[3][4] = new RookPiece(PieceColor.BLACK);
        board[4][3] = new RookPiece(PieceColor.BLACK);
        board[3][2] = new RookPiece(PieceColor.BLACK);
        board[2][3] = new RookPiece(PieceColor.BLACK);

        List<Position> result = RookPiece.getPossibleMoves(3, 3, board);
        PositionTest.assertPosition(expect, result);
    }

    @Test
    void testBlockedByFriendlyPieces() {
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();

        // Add moves only up to friendly pieces
        for(int x = 1; x < 3; x++) {
            expect.add(new Position(x, 3, false));
        }
        for(int y = 4; y < 6; y++) {
            expect.add(new Position(3, y, false));
        }

        // Place white rook
        board[3][3] = new RookPiece(PieceColor.WHITE);
        // Place friendly pieces as blockers
        board[2][3] = new RookPiece(PieceColor.WHITE); // South blocker
        board[6][3] = new RookPiece(PieceColor.WHITE); // North blocker
        board[3][0] = new RookPiece(PieceColor.WHITE); // West blocker
        board[3][4] = new RookPiece(PieceColor.WHITE); // East blocker

        List<Position> result = RookPiece.getPossibleMoves(3, 3, board);
        PositionTest.assertPosition(expect, result);
    }

    @Test
    void testEdgePosition() {
        // Test Rook at edge (0,3)
        ChessPiece[][] board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();

        // Add horizontal moves
        for(int x = 1; x < 8; x++) {
            expect.add(new Position(x, 3, false));
        }
        // Add vertical moves
        for(int y = 0; y < 8; y++) {
            if(y != 3) {
                expect.add(new Position(0, y, false));
            }
        }

        board[0][3] = new RookPiece(PieceColor.WHITE);
        List<Position> result = RookPiece.getPossibleMoves(0, 3, board);
        PositionTest.assertPosition(expect, result);
    }
}