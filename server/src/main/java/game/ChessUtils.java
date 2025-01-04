package game;
/**
 * 設定、判定棋盤範圍
 **/
public final class ChessUtils {
    public static final int BOARD_SIZE = 8;
    private ChessUtils() {}
    /**
     * 判定是否位於棋盤範圍
     **/
    public static boolean isValidPos(final int col, final int row) {
        return col >= 0 && col < ChessUtils.BOARD_SIZE && row >= 0 && row < ChessUtils.BOARD_SIZE;
    }
}
