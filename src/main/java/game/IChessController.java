package game;

public interface IChessController {
    public void start(Callback callback);
    public boolean move(int fromX, int fromY, int toX, int toY);
    public boolean getMovableList(int x, int y);
    public String checkGameStatus();
}
