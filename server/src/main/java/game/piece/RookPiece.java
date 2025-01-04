package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;
/**
 * 判定、設定棋子 城堡
 **/
public class RookPiece extends ChessPiece {
    private boolean moved; // Track if the rook has moved

    /**
     * 設定棋子種類、顏色
     **/
    public RookPiece(final PieceColor color) {
        super(PieceType.ROOK, color);
    }

    /**
     * 設定棋子已被移動
     **/
    public void setHasMoved() {
        this.moved = true;
    }

    /**
     * 查看棋子是否已被移動
     **/
    public boolean hasMoved() {
        return this.moved;
    }

    /**
     * 取得合法的移動範圍
     **/
    public static List<Position> getPossibleMoves(final int col, final int row, final ChessPiece[][] board) {
        final List<Position> result = new ArrayList<>();

        for (final int[] direction : getDirections()) {
            int newX = col, newY = row;

            while (true) {
                newX += direction[0];
                newY += direction[1];

                if (!ChessUtils.isValidPos(newX, newY)) {
                    break;
                }

                final ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null) {
                    result.add(new Position(newX, newY, false)); // Empty square
                } else {
                    if (targetPiece.color != board[row][col].color) {
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
