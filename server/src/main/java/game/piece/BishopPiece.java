package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;
/**
 * 判定、設定棋子 主教
 **/
public class BishopPiece extends ChessPiece {
    /**
     * 設定棋子種類、顏色
     **/
    public BishopPiece(final PieceColor color) {
        super(PieceType.BISHOP, color);
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
                    result.add(new Position(newX, newY, false)); // 空格
                } else {
                    if (targetPiece.color != board[row][col].color) {
                        result.add(new Position(newX, newY, true)); // 攻擊
                    }
                    break; // 遇到棋子時停止掃描
                }
            }
        }

        return result;
    }

    // 提供方向資訊（四個對角方向）
    private static int[][] getDirections() {
        return new int[][]{
                {1, 1},   // 右下
                {1, -1},  // 右上
                {-1, 1},  // 左下
                {-1, -1}  // 左上
        };
    }
}
