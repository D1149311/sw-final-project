import game.ChessGame;
import network.ChessWebSocket;
import storage.UserService;

import java.io.IOException;

public class Main {
    public static int PORT = 8081;

    public static void main(String[] args) throws InterruptedException, IOException {
        ChessGame gameService = new ChessGame();
        UserService userService = new UserService();
        ChessWebSocket server = new ChessWebSocket(PORT, userService, gameService);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:" + PORT + "/");
            System.out.println("Press CTRL+C to quit");

            while (true) {
                // 阻止任何輸入，實際上等待用戶按下鍵，但不處理輸入
                System.in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
