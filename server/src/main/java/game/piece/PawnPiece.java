package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;

public class PawnPiece extends ChessPiece {
    private boolean hasMoved;

    public PawnPiece(PieceColor color) {
        super(PieceType.PAWN, color);
        this.hasMoved = false;
    }

    // 返回是否已經移動過
    public boolean hasMoved() {
        return hasMoved;
    }

    // 設置兵是否已經移動過
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    // 取得合法的移動範圍
    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board, Position lastMove) {
        List<Position> result = new ArrayList<>();
        PawnPiece pawn = (PawnPiece) board[y][x];

        int direction = (pawn.color == PieceColor.WHITE) ? -1 : 1;

        // 向前一格移動
        if (ChessUtils.isValidPos(x, y + direction) && board[y + direction][x] == null) {
            result.add(new Position(x, y + direction, false));
        }

        // 首次移動可以選擇向前兩格
        if (!pawn.hasMoved() && ChessUtils.isValidPos(x, y + 2 * direction) && board[y + 2 * direction][x] == null && board[y + direction][x] == null) {
            result.add(new Position(x, y + 2 * direction, false));
        }

        // 斜前方吃棋子
        addCaptureMoves(x, y, direction, board, result);

        // 處理過路兵吃法
        addEnPassantMoves(x, y, direction, board, result, lastMove);

        return result;
    }

    // 添加斜前方的攻擊移動
    private static void addCaptureMoves(int x, int y, int direction, ChessPiece[][] board, List<Position> result) {
        if (ChessUtils.isValidPos(x - 1, y + direction) && board[y + direction][x - 1] != null &&
                board[y + direction][x - 1].color != board[y][x].color) {
            result.add(new Position(x - 1, y + direction, true));
        }
        if (ChessUtils.isValidPos(x + 1, y + direction) && board[y + direction][x + 1] != null &&
                board[y + direction][x + 1].color != board[y][x].color) {
            result.add(new Position(x + 1, y + direction, true));
        }
    }

    // 添加過路兵的攻擊移動
    private static void addEnPassantMoves(int x, int y, int direction, ChessPiece[][] board, List<Position> result, Position lastMove) {
        if (lastMove == null) return;

        // 左側過路兵
        if (lastMove.x == x - 1 && lastMove.y == y) {
            ChessPiece leftPawn = board[y][x - 1];
            if (leftPawn != null && leftPawn.type == PieceType.PAWN && leftPawn.color != board[y][x].color) {
                result.add(new Position(x - 1, y + direction, true));
            }
        }

        // 右側過路兵
        if (lastMove.x == x + 1 && lastMove.y == y) {
            ChessPiece rightPawn = board[y][x + 1];
            if (rightPawn != null && rightPawn.type == PieceType.PAWN && rightPawn.color != board[y][x].color) {
                result.add(new Position(x + 1, y + direction, true));
            }
        }
    }

    // 處理過路兵吃法
    public static boolean enPassantCapture(int fromX, int fromY, int toX, int toY, ChessPiece[][] board, Position lastMove) {
        PawnPiece pawn = (PawnPiece) board[fromY][fromX];

        // 確保是斜移一格，並且目標格子空
        if (Math.abs(toX - fromX) == 1 && toY - fromY == (pawn.color == PieceColor.WHITE ? -1 : 1) && board[toY][toX] == null) {
            // 檢查 lastMove 是否為敵方兵的雙步移動
            if (lastMove != null && lastMove.y == fromY && lastMove.x == toX) {
                ChessPiece opponentPawn = board[fromY][lastMove.x];
                if (opponentPawn != null && opponentPawn.type == PieceType.PAWN && opponentPawn.color != pawn.color) {
                    // 移動己方兵並移除敵方兵
                    board[toY][toX] = pawn;
                    board[fromY][fromX] = null;
                    board[fromY][lastMove.x] = null;
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Position> getAttackRange(int x, int y, ChessPiece[][] board) {
        List<Position> attackRange = new ArrayList<>();
        PawnPiece pawn = (PawnPiece) board[y][x];

        int direction = (pawn.color == PieceColor.WHITE) ? -1 : 1;

        // 攻擊範圍僅為斜前兩格
        if (ChessUtils.isValidPos(x - 1, y + direction)) {
            attackRange.add(new Position(x - 1, y + direction, false));
        }
        if (ChessUtils.isValidPos(x + 1, y + direction)) {
            attackRange.add(new Position(x + 1, y + direction, false));
        }

        return attackRange;
    }

}
