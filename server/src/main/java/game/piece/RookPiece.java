package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;

public class RookPiece extends ChessPiece {
    private boolean hasMoved = false; // Track if the rook has moved

    public RookPiece(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    // Mark the rook as moved
    public void setHasMoved() {
        this.hasMoved = true;
    }

    // Check if the rook has moved
    public boolean hasMoved() {
        return this.hasMoved;
    }

    // Get possible moves for the rook
    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) {
        List<Position> result = new ArrayList<>();

        for (int[] direction : getDirections()) {
            int newX = x, newY = y;

            while (true) {
                newX += direction[0];
                newY += direction[1];

                if (!ChessUtils.isValidPos(newX, newY)) {
                    break;
                }

                ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null) {
                    result.add(new Position(newX, newY, false)); // Empty square
                } else {
                    if (targetPiece.color != board[y][x].color) {
                        result.add(new Position(newX, newY, true)); // Attack
                    }
                    break; // Stop scanning if there's a piece in the way
                }
            }
        }

        return result;
    }

    // Get direction info (left, right, up, down)
    private static int[][] getDirections() {
        return new int[][]{
                {1, 0},  // Right
                {-1, 0}, // Left
                {0, 1},  // Down
                {0, -1}  // Up
        };
    }
}
