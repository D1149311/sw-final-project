package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;
/**
 * 判定、設定棋子 騎士
 **/
public class KnightPiece extends ChessPiece {
    /**
     * 設定棋子種類、顏色
     **/
    public KnightPiece(final PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    /**
     * 取得合法的移動範圍
     **/
    public static List<Position> getPossibleMoves(final int col, final int row, final ChessPiece[][] board) {
        final int[][] deltas = getMoveDeltas();

        final List<Position> result = new ArrayList<>();
        for (final int[] delta : deltas) {
            final int newX = col + delta[0];
            final int newY = row + delta[1];

            if (ChessUtils.isValidPos(newX, newY)) {
                final ChessPiece target = board[newY][newX];
                if (target == null || target.color != board[row][col].color) {
                    result.add(new Position(newX, newY, target != null)); // true if capturing
                }
            }
        }

        return result;
    }

    // 提供所有可能的移動方向（L 形移動）
    private static int[][] getMoveDeltas() {
        return new int[][]{
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };
    }
}
