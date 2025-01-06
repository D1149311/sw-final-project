package game;

/**
 * Chess game factory
 */
public interface IGameService {
    /**
     * create a chess game
     */
    IChessService createGame();
}
