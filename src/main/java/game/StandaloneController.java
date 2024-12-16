package game;

public class StandaloneController implements IChessController {
    private ChessGame game;
    private Callback callback;

    public StandaloneController() {
        this.game = new ChessGame();
    }

    @Override
    public void start(Callback callback) {
        Piece[][] board = this.game.getBoard();

        for (int row = 0; row < ChessGame.BOARD_SIZE; row++) {
            for (int col = 0; col < ChessGame.BOARD_SIZE; col++) {
                callback.setChess(col, row, board[row][col]);
            }
        }
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean getMovableList(int x, int y) {
        return false;
    }

    @Override
    public String checkGameStatus() {
        return "";
    }
}
