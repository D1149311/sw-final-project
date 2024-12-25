package network;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

// from https://github.com/pusher/java-websocket/blob/master/src/main/example/ChatServer.java
public class ChessWebSocket extends WebSocketServer {
    public ChessWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ChessWebSocket(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection: " + handshake.getResourceDescriptor());
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        System.out.println(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        this.sendToAll(message);
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();

        if(conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            for(WebSocket c : con) {
                c.send(text);
            }
        }
    }
}
