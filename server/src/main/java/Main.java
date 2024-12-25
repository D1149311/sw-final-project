import network.ChessWebSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static int PORT = 8081;

    public static void main(String[] args) throws InterruptedException, IOException {
        // 指定主機和端口
        ChessWebSocket server = new ChessWebSocket(PORT);

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
