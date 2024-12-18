package game;

public class ChessUtils {
    public static final int BOARD_SIZE = 8;

    public static boolean isValidPos(int x, int y) {
        return x >= 0 && x < ChessUtils.BOARD_SIZE && y >= 0 && y < ChessUtils.BOARD_SIZE;
    }
}
