import network.Server;

public class Main {
    public static final int PORT = 6666;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.startServer();
    }
}
