package game.piece;

import game.*;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
/**
 * 判定、設定棋子 國王
 **/
public class KingPiece extends ChessPiece {
    private boolean moved; // Track if the king has moved
    /**
     * 設定棋子種類、顏色
     **/
    public KingPiece(final PieceColor color) {
        super(PieceType.KING, color);
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
     * 取得國王攻擊範圍
     **/
    static public List<Position> getAttackRange(final int col, final int row, final ChessPiece[][] board) {
        final List<Position> attackRange = new ArrayList<>();

        // King 可以攻擊周圍的 8 個方向
        for (final int[] direction : getDirections()) {
            final int newX = col + direction[0];
            final int newY = row + direction[1];

            // 檢查是否是有效的位置
            if (ChessUtils.isValidPos(newX, newY)) {
                final ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null || targetPiece.color != board[row][col].color) {
                    attackRange.add(new Position(newX, newY, targetPiece != null)); // 如果有敵方棋子則攻擊
                }
            }
        }

        return attackRange;
    }

    /**
     * 取得合法的移動範圍
     **/
    static public List<Position> getPossibleMoves(final int col, final int row, final ChessPiece[][] board) {
        final List<Position> result = new ArrayList<>();

        // 取得King的可行走範圍
        for (final int[] direction : getDirections()) {
            final int newX = col + direction[0];
            final int newY = row + direction[1];

            // 檢查是否是有效的位置
            if (ChessUtils.isValidPos(newX, newY)) {
                final ChessPiece targetPiece = board[newY][newX];
                if (targetPiece == null || targetPiece.color != board[row][col].color) {
                    result.add(new Position(newX, newY, targetPiece != null)); // 如果是敵方棋子，那麼是一次攻擊
                }
            }
        }

        List<Position> attackRange = new ArrayList<>();
        // 遍歷整個棋盤，對於每個敵方棋子，根據其類型獲取攻擊範圍
        for (int i = 0; i < ChessUtils.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessUtils.BOARD_SIZE; j++) {
                final ChessPiece targetPiece = board[i][j];

                // 如果是敵方棋子
                if (targetPiece != null && targetPiece.color != board[row][col].color) {

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
                            attackRange = PawnPiece.getAttackRange(j, i, board);
                            break;
                        case KING:
                            attackRange = KingPiece.getAttackRange(j, i, board);
                            break;
                    }

                    // 使用Iterator遍歷result，並從中移除與攻擊範圍重疊的部分
                    final Iterator<Position> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        final Position kingMove = iterator.next();
                        if (attackRange.contains(kingMove)) {
                            iterator.remove();  // 如果King的可行走位置在攻擊範圍內，移除它
                        }
                    }
                }
            }
        }

        return result;
    }

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
