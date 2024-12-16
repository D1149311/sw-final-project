package game;

public class OnlineController implements IChessController {
    @Override
    public void start(Callback callback) {

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
