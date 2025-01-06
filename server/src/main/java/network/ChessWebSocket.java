package network;

import game.IGameService;
import network.room.AuthRoom;
import network.room.GameRoom;
import network.room.LobbyRoom;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import storage.IUserService;
import storage.UserService;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// https://github.com/pusher/java-websocket/blob/
// master/src/main/example/ChatServer.java

/**
 * Create the websocket server
 */
public class ChessWebSocket extends WebSocketServer {
    private static final Logger LOGGER = Logger.getLogger(ChessWebSocket.class.getName());
    public Map<String, Room> rooms;
    public Map<WebSocket, Client> clients;
    public IUserService userService;
    public IGameService gameService;

    /**
     * Initialize websocket server
     */
    public ChessWebSocket(final int port, final IUserService userService, final IGameService gameService) {
        super(new InetSocketAddress(port));

        this.rooms = new HashMap<>();
        this.clients = new HashMap<>();
        this.userService = userService;
        this.gameService = gameService;

        this.createRoom("auth", new AuthRoom(this));
        this.createRoom("lobby", new LobbyRoom(this));
    }

    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        LOGGER.info("new connection: " + handshake.getResourceDescriptor());
        final Client client = new Client(conn);
        clients.put(conn, client);
        joinRoom(client, "auth");
    }

    @Override
    public void onClose(final WebSocket conn, final int closeCode, final String reason, boolean wasClean) {
        LOGGER.info(conn + " has left the room!");
        leaveRoom(clients.get(conn));
    }

    @Override
    public void onMessage(final WebSocket conn, final String message) {
//        System.out.println(message);
        final Client client = clients.get(conn);
        rooms.get(client.roomId).processMessage(client, message);
    }

    @Override
    public void onError(final WebSocket conn, final Exception exception) {
        LOGGER.warning(exception.getMessage());
//
//        if(conn != null) {
//            // some errors like port binding failed may
//            // not be assignable to a specific websocket
//        }
    }

    /**
     * create a game room
     */
    public String createRoom() {
        String roomId = "lobby";

        while(rooms.get(roomId) != null) {
            roomId = String.valueOf((int) (Math.random() * 10));
        }

        final GameRoom room = new GameRoom(roomId, this);
        rooms.put(roomId, room);

        return roomId;
    }

    /**
     * create a room with room id
     */
    public void createRoom(final String roomId, final Room room) {
        rooms.put(roomId, room);
    }

    /**
     * destroy the room
     */
    public void destroyRoom(final String roomId) {
        for (int client = rooms.get(roomId).clients.size(); client > 0; client--) {
            joinRoom(rooms.get(roomId).clients.get(0), "lobby");
        }
        rooms.remove(roomId);
    }

    /**
     * join the user to the room
     */
    public void joinRoom(final Client conn, final String roomId) {
//        if (roomId == null || !rooms.containsKey(roomId)) {
//            return;
//        }

        leaveRoom(conn); // Remove client from previous room
        final Room room = rooms.get(roomId);
        conn.setRoomId(roomId);
        room.addClient(conn);
    }

    private void leaveRoom(final Client conn) {
        final Room room = rooms.get(conn.roomId);
        if (room != null) {
            room.removeClient(conn);
        }
    }
}
