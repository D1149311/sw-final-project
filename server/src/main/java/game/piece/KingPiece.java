package game.piece;

import game.*;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class KingPiece extends ChessPiece {
    private boolean hasMoved = false; // Track if the king has moved
    private boolean isInCheck = false; // Track if the king is in check

    public KingPiece(PieceColor color) {
        super(PieceType.KING, color);
    }

    // Mark the king as moved
    public void setHasMoved() {
        this.hasMoved = true;
    }

    // Check if the king has moved
    public boolean hasMoved() {
        return this.hasMoved;
    }

    // Mark the king as being in check
    public void setInCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    // Check if the king is in check
    public boolean isInCheck() {
        return this.isInCheck;
    }

    // 假設 KingPiece 類別中新增了這個方法
    static public List<Position> getAttackRange(int x, int y, ChessPiece[][] board) {
        List<Position> attackRange = new ArrayList<>();

        // King 可以攻擊周圍的 8 個方向
        for (int[] direction : getDirections()) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            // 檢查是否是有效的位置
            if (ChessUtils.isValidPos(newX, newY)) {
                ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null || targetPiece.color != board[y][x].color) {
                    attackRange.add(new Position(newX, newY, targetPiece != null)); // 如果有敵方棋子則攻擊
                }
            }
        }

        return attackRange;
    }

    // Get possible moves for the king (one square in any direction)
    static public List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) {
        List<Position> result = new ArrayList<>();

        // 取得King的可行走範圍
        for (int[] direction : getDirections()) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            // 檢查是否是有效的位置
            if (ChessUtils.isValidPos(newX, newY)) {
                ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null || targetPiece.color != board[y][x].color) {
                    result.add(new Position(newX, newY, targetPiece != null)); // 如果是敵方棋子，那麼是一次攻擊
                }
            }
        }

        // 遍歷整個棋盤，對於每個敵方棋子，根據其類型獲取攻擊範圍
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                ChessPiece targetPiece = board[i][j];

                // 如果是敵方棋子
                if (targetPiece != null && targetPiece.color != board[y][x].color) {
                    List<Position> attackRange = new ArrayList<>();

                    // 根據對方棋子的類型調用對應的getPossibleMoves方法來獲取攻擊範圍
                    switch (targetPiece.type) {
                        case ROOK:
                            attackRange = RookPiece.getPossibleMoves(j, i, board);
                            break;
                        case BISHOP:
                            attackRange = BishopPiece.getPossibleMoves(j, i, board);
                            break;
                        case QUEEN:
                            attackRange = QueenPiece.getPossibleMoves(j, i, board);
                            break;
                        case KNIGHT:
                            attackRange = KnightPiece.getPossibleMoves(j, i, board);
                            break;
                        case PAWN:
                            attackRange = PawnPiece.getAttackRange(j, i, board); // 這裡需要適當處理lastMove
                            break;
                        case KING:
                            attackRange = KingPiece.getAttackRange(j, i, board);
                            break;
                    }

                    // 使用Iterator遍歷result，並從中移除與攻擊範圍重疊的部分
                    Iterator<Position> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        Position kingMove = iterator.next();
                        if (attackRange.contains(kingMove)) {
                            iterator.remove();  // 如果King的可行走位置在攻擊範圍內，移除它
                        }
                    }
                }
            }
        }

        return result;
    }

    // Check if castling is possible (both for short and long castling)
//    public boolean canCastle(int x, int y, ChessPiece[][] board, boolean isShortCastle) {
//        if (hasMoved() || isInCheck) {
//            return false; // King has already moved or is in check
//        }
//
//        RookPiece rook = null;
//        int rookX = -1, rookY = -1;
//
//        if (isShortCastle) {
//            rookX = x + 3;
//            rookY = y;
//        } else {
//            rookX = x - 4;
//            rookY = y;
//        }
//
//        // Check if the rook is in the right position and hasn't moved
//        if (ChessUtils.isValidPos(rookX, rookY)) {
//            ChessPiece piece = board[rookY][rookX];
//            if (piece instanceof RookPiece) {
//                rook = (RookPiece) piece;
//                if (rook.hasMoved()) {
//                    return false; // Rook has already moved
//                }
//            }
//        }
//
//        if (rook == null) {
//            return false; // No rook found in the right position
//        }
//
//        // Check if there are pieces between the king and the rook
//        int step = isShortCastle ? 1 : -1;
//        for (int i = x + step; i != rookX; i += step) {
//            if (board[y][i] != null) {
//                return false; // There's a piece in the way
//            }
//        }
//
//        // Check if the squares the king passes through are under attack
//        int kingPathXStart = isShortCastle ? x + 1 : x - 1;
//        int kingPathXEnd = isShortCastle ? x + 2 : x - 2;
//
//        for (int i = kingPathXStart; i != kingPathXEnd; i += step) {
//            if (ChessUtils.isUnderAttack(i, y, board, this.color)) {
//                return false; // One of the squares is under attack
//            }
//        }
//
//        return true; // All conditions met, castling is possible
//    }

    // Get direction info (king can move one square in any direction)
    private static int[][] getDirections() {
        return new int[][]{
                {1, 0},  // Right
                {-1, 0}, // Left
                {0, 1},  // Down
                {0, -1}, // Up
                {1, 1},  // Down-Right
                {-1, 1}, // Down-Left
                {1, -1}, // Up-Right
                {-1, -1}  // Up-Left
        };
    }
}
