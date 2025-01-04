package network;

import org.java_websocket.WebSocket;

public class Client {
    public WebSocket conn;
    public String roomId;
    public String id;

    public Client(WebSocket conn) {
        this.conn = conn;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
