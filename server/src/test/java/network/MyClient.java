package network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyClient extends WebSocketClient {
    private final List<String> buffer = new ArrayList<>();

    public MyClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        this.buffer.add(s);
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public String getBuffer() throws InterruptedException {
        while (buffer.isEmpty()) {
            Thread.sleep(50);
        }

        return buffer.remove(0);
    }
}
