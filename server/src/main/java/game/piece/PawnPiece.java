package game.piece;

import game.*;

import java.util.ArrayList;
import java.util.List;
/**
 * 判定、設定棋子 士兵
 **/
public class PawnPiece extends ChessPiece {
    private boolean moved;

    /**
     * 設定棋子種類、顏色
     **/
    public PawnPiece(final PieceColor color) {
        super(PieceType.PAWN, color);
        this.moved = false;
    }

    /**
     * 設定棋子已被移動
     **/
    public boolean hasMoved() {
        return moved;
    }

    /**
     * 查看棋子是否已被移動
     **/
    public void setMoved(final boolean moved) {
        this.moved = moved;
    }

    /**
     * 取得合法的移動範圍
     **/
    public static List<Position> getPossibleMoves(final int col, final int row, final ChessPiece[][] board, final Position lastMove) {
        final List<Position> result = new ArrayList<>();
        final PawnPiece pawn = (PawnPiece) board[row][col];

        final int direction = (pawn.color == PieceColor.WHITE) ? -1 : 1;

        // 向前一格移動
        if (ChessUtils.isValidPos(col, row + direction) && board[row + direction][col] == null) {
            result.add(new Position(col, row + direction, false));
        }

        // 首次移動可以選擇向前兩格
        if (!pawn.hasMoved() && ChessUtils.isValidPos(col, row + 2 * direction) && board[row + 2 * direction][col] == null && board[row + direction][col] == null) {
            result.add(new Position(col, row + 2 * direction, false));
        }

        // 斜前方吃棋子
        addCaptureMoves(col, row, direction, board, result);

        // 處理過路兵吃法
        addEnPassantMoves(col, row, direction, board, result, lastMove);

        return result;
    }

    // 添加斜前方的攻擊移動
    private static void addCaptureMoves(final int col, final int row, final int direction, final ChessPiece[][] board, final List<Position> result) {
        if (ChessUtils.isValidPos(col - 1, row + direction) && board[row + direction][col - 1] != null &&
                board[row + direction][col - 1].color != board[row][col].color) {
            result.add(new Position(col - 1, row + direction, true));
        }
        if (ChessUtils.isValidPos(col + 1, row + direction) && board[row + direction][col + 1] != null &&
                board[row + direction][col + 1].color != board[row][col].color) {
            result.add(new Position(col + 1, row + direction, true));
        }
    }

    // 添加過路兵的攻擊移動
    private static void addEnPassantMoves(final int col, final int row, final int direction, final ChessPiece[][] board, final List<Position> result, final Position lastMove) {
        if (lastMove != null) {
            // 左側過路兵
            if (lastMove.col == col - 1 && lastMove.row == row) {
                final ChessPiece leftPawn = board[row][col - 1];
                if (leftPawn != null && leftPawn.type == PieceType.PAWN && leftPawn.color != board[row][col].color) {
                    result.add(new Position(col - 1, row + direction, true));
                }
            }

            // 右側過路兵
            if (lastMove.col == col + 1 && lastMove.row == row) {
                final ChessPiece rightPawn = board[row][col + 1];
                if (rightPawn != null && rightPawn.type == PieceType.PAWN && rightPawn.color != board[row][col].color) {
                    result.add(new Position(col + 1, row + direction, true));
                }
            }
        }
    }

    /**
     * 處理過路兵吃法
     **/
    public static boolean enPassantCapture(final int fromX, final int fromY, final int toX, final int toY, final ChessPiece[][] board, final Position lastMove) {
        final PawnPiece pawn = (PawnPiece) board[fromY][fromX];
        boolean result = false;
        // 確保是斜移一格，並且目標格子空
        if (Math.abs(toX - fromX) == 1 && toY - fromY == (pawn.color == PieceColor.WHITE ? -1 : 1) && board[toY][toX] == null && lastMove != null && lastMove.row == fromY && lastMove.col == toX) {
            // 檢查 lastMove 是否為敵方兵的雙步移動
            final ChessPiece opponentPawn = board[fromY][lastMove.col];
            if (opponentPawn != null && opponentPawn.type == PieceType.PAWN && opponentPawn.color != pawn.color) {
                // 移動己方兵並移除敵方兵
                board[toY][toX] = pawn;
                board[fromY][fromX] = null;
                board[fromY][lastMove.col] = null;
                result = true;
            }
        }
        return result;
    }

    /**
     * 取得士兵攻擊範圍
     **/
    public static List<Position> getAttackRange(final int col, final int row, final ChessPiece[][] board) {
        final List<Position> attackRange = new ArrayList<>();
        final PawnPiece pawn = (PawnPiece) board[row][col];

        final int direction = (pawn.color == PieceColor.WHITE) ? -1 : 1;

        // 攻擊範圍僅為斜前兩格
        if (ChessUtils.isValidPos(col - 1, row + direction)) {
            attackRange.add(new Position(col - 1, row + direction, false));
        }
        if (ChessUtils.isValidPos(col + 1, row + direction)) {
            attackRange.add(new Position(col + 1, row + direction, false));
        }

        return attackRange;
    }

}
