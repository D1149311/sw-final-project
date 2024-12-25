package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;

public class KnightPiece extends ChessPiece {
    public KnightPiece(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    // 取得合法的移動範圍
    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) {
        int[][] deltas = getMoveDeltas();

        List<Position> result = new ArrayList<>();
        for (int[] delta : deltas) {
            int newX = x + delta[0];
            int newY = y + delta[1];

            if (ChessUtils.isValidPos(newX, newY)) {
                ChessPiece target = board[newY][newX];
                if (target == null || target.color != board[y][x].color) {
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
