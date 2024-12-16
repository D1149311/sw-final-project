package game;

public class StandaloneController implements IChessController {
    private ChessGame game;

    public StandaloneController() {
        this.game = new ChessGame();
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
