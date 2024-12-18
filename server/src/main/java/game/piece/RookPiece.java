package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;

public class RookPiece extends ChessPiece {
    public RookPiece(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) {
        int[][][] deltaList = {
            { { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 }, { 7, 0 } },
            { { -1, 0 }, { -2, 0 }, { -3, 0 }, { -4, 0 }, { -5, 0 }, { -6, 0 }, { -7, 0 } },
            { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 0, 6 }, { 0, 7 } },
            { { 0, -1 }, { 0, -2 }, { 0, -3 }, { 0, -4 }, { 0, -5 }, { 0, -6 }, { 0, -7 } }
        };

        List<Position> result = new ArrayList<>();

        for (int[][] deltaLine : deltaList) {
            for (int[] delta : deltaLine) {
                if (ChessUtils.isValidPos(x + delta[0], y + delta[1])) {
                    if (board[y][x] == null) {
                        result.add(new Position(x + delta[0], y + delta[1], false));
                    } else if (board[y + delta[1]][x + delta[0]].color != board[y][x].color) {
                        result.add(new Position(x + delta[0], y + delta[1], true));
                        break;
                    } else if (board[y + delta[1]][x + delta[0]].color == board[y][x].color) {
                        break;
                    }
                }
            }
        }

        return result;
    }
}
