package game;

public class ChessUtils {
    public static final int BOARD_SIZE = 8;

    public static boolean isValidPos(int x, int y) {
        return x >= 0 && x < ChessUtils.BOARD_SIZE && y >= 0 && y < ChessUtils.BOARD_SIZE;
    }

    // Check if the position (x, y) is under attack by any enemy piece
    public static boolean isUnderAttack(int x, int y, ChessPiece[][] board, PieceColor kingColor) {
        // Check for attacks from all piece types
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPiece piece = board[i][j];

                // Skip if there's no piece at this position or if it's the same color as the king
                if (piece == null || piece.color == kingColor) {
                    continue;
                }

                // Determine what type of piece this is and check if it can attack (x, y)
                boolean canAttack = false;
                switch (piece.type) {
                    case ROOK:
                        canAttack = canRookAttack(j, i, x, y, board);
                        break;
                    case BISHOP:
                        canAttack = canBishopAttack(j, i, x, y, board);
                        break;
                    case QUEEN:
                        canAttack = canQueenAttack(j, i, x, y, board);
                        break;
                    case KNIGHT:
                        canAttack = canKnightAttack(j, i, x, y);
                        break;
                    case PAWN:
                        canAttack = canPawnAttack(j, i, x, y, piece.color);
                        break;
                    case KING:
                        canAttack = canKingAttack(j, i, x, y);
                        break;
                }

                if (canAttack) {
                    return true; // The position (x, y) is under attack
                }
            }
        }

        return false; // No piece can attack the target position
    }

    // Check if a pawn can attack the given position (diagonal attack)
    private static boolean canPawnAttack(int x, int y, int targetX, int targetY, PieceColor color) {
        if (color == PieceColor.WHITE) {
            return (x - 1 == targetX && y + 1 == targetY) || (x + 1 == targetX && y + 1 == targetY);
        } else {
            return (x - 1 == targetX && y - 1 == targetY) || (x + 1 == targetX && y - 1 == targetY);
        }
    }

    // Check if a rook can attack the given position (straight lines)
    private static boolean canRookAttack(int x, int y, int targetX, int targetY, ChessPiece[][] board) {
        if (x != targetX && y != targetY) {
            return false; // Rooks can only move along straight lines
        }

        int dx = Integer.signum(targetX - x);
        int dy = Integer.signum(targetY - y);

        // Check if there are no pieces between the rook and the target position
        int i = x + dx, j = y + dy;
        while (i != targetX || j != targetY) {
            if (board[j][i] != null) {
                return false; // There's a piece in the way
            }
            i += dx;
            j += dy;
        }

        return true;
    }

    // Check if a bishop can attack the given position (diagonal lines)
    private static boolean canBishopAttack(int x, int y, int targetX, int targetY, ChessPiece[][] board) {
        if (Math.abs(x - targetX) != Math.abs(y - targetY)) {
            return false; // Bishops can only move along diagonals
        }

        int dx = Integer.signum(targetX - x);
        int dy = Integer.signum(targetY - y);

        // Check if there are no pieces between the bishop and the target position
        int i = x + dx, j = y + dy;
        while (i != targetX || j != targetY) {
            if (board[j][i] != null) {
                return false; // There's a piece in the way
            }
            i += dx;
            j += dy;
        }

        return true;
    }

    // Check if a queen can attack the given position (combination of rook and bishop moves)
    private static boolean canQueenAttack(int x, int y, int targetX, int targetY, ChessPiece[][] board) {
        return canRookAttack(x, y, targetX, targetY, board) || canBishopAttack(x, y, targetX, targetY, board);
    }

    // Check if a knight can attack the given position (L-shaped move)
    private static boolean canKnightAttack(int x, int y, int targetX, int targetY) {
        int dx = Math.abs(x - targetX);
        int dy = Math.abs(y - targetY);
        return dx == 2 && dy == 1 || dx == 1 && dy == 2;
    }

    // Check if a king can attack the given position (one square in any direction)
    private static boolean canKingAttack(int x, int y, int targetX, int targetY) {
        return Math.abs(x - targetX) <= 1 && Math.abs(y - targetY) <= 1;
    }
}
