package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;

public class QueenPiece extends ChessPiece {
    public QueenPiece(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    // 取得合法的移動範圍
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
                    result.add(new Position(newX, newY, false)); // 空格
                } else {
                    if (targetPiece.color != board[y][x].color) {
                        result.add(new Position(newX, newY, true)); // 攻擊
                    }
                    break; // 遇到棋子時停止掃描
                }
            }
        }

        return result;
    }

    // 提供方向資訊（八個方位）
    private static int[][] getDirections() {
        return new int[][]{
                {1, 0},   // 向右
                {-1, 0},  // 向左
                {0, 1},   // 向下
                {0, -1},  // 向上
                {1, 1},   // 右下
                {1, -1},  // 右上
                {-1, 1},  // 左下
                {-1, -1}  // 左上
        };
    }
}
