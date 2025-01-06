import game.ChessGame;
import network.ChessWebSocket;
import storage.UserService;

import java.io.IOException;

/**
 * Chess websocket server entry class
 */
public final class Main {
    public static final int PORT = 8081;

    private Main() {}

    /**
     * Chess websocket server entry
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        final ChessGame gameService = new ChessGame();
        final UserService userService = new UserService("user.txt");
        final ChessWebSocket server = new ChessWebSocket(PORT, userService, gameService);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:" + PORT + "/");
            System.out.println("Press CTRL+C to quit");

            while (true) {
                // 阻止任何輸入，實際上等待用戶按下鍵，但不處理輸入
                System.in.read();
            }
        } finally {
            server.stop();
        }
    }
}
