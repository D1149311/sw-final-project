package network;

import org.java_websocket.WebSocket;

/**
 * Connection of websocket
 */
public class Client {
    public WebSocket conn;
    public String roomId;
    public String userId;

    /**
     * Initialize the client
     */
    public Client(final WebSocket conn) {
        this.conn = conn;
    }

    /**
     * set the room id of the client
     */
    public void setRoomId(final String roomId) {
        this.roomId = roomId;
    }
}
