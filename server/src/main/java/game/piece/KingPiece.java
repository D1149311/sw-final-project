package game.piece;

import game.ChessPiece;
import game.PieceColor;
import game.PieceType;

public class KingPiece extends ChessPiece {

    public KingPiece(PieceColor color) {
        super(PieceType.KING, color);
    }
    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) {
        int[][][] deltaList = {
                {{ 1, 0 },{1,1}},
                {{ -1, 0 },{-1,1}},
                {{ 0, 1 },{1,-1}},
                {{ 0, -1 },{-1,-1}},
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
