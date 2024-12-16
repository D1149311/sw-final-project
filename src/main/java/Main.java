import game.IChessController;
import game.StandaloneController;
import gui.ChessBoard;

public class Main {
    public static void main(String[] args) {
        IChessController controller = new StandaloneController();
        new ChessBoard(controller);
    }
}
