package network;

import game.IGameService;
import network.room.AuthRoom;
import network.room.GameRoom;
import network.room.LobbyRoom;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import storage.IUserService;

import java.net.InetSocketAddress;
import java.util.HashMap;

// from https://github.com/pusher/java-websocket/blob/master/src/main/example/ChatServer.java
public class ChessWebSocket extends WebSocketServer {
    public HashMap<String, Room> rooms;
    public HashMap<WebSocket, Client> clients;
    public IUserService userService;
    public IGameService gameService;

    public ChessWebSocket(int port, IUserService userService, IGameService gameService) {
        super(new InetSocketAddress(port));
        this.rooms = new HashMap<>();
        this.clients = new HashMap<>();
        this.userService = userService;
        this.gameService = gameService;
        this.createStaticRooms();
    }

    private void createStaticRooms() {
        this.createRoom("auth", new AuthRoom(this));
        this.createRoom("lobby", new LobbyRoom(this));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection: " + handshake.getResourceDescriptor());
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
        Client client = new Client(conn);
        clients.put(conn, client);
        joinRoom(client, "auth");
    }

    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        System.out.println(conn + " has left the room!");
        leaveRoom(clients.get(conn));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        Client client = clients.get(conn);
        try {
            rooms.get(client.roomId).processMessage(client, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();
//
//        if(conn != null) {
//            // some errors like port binding failed may not be assignable to a specific websocket
//        }
    }

    public String createRoom() {
        String roomId = "lobby";

        while(rooms.get(roomId) != null) {
            roomId = String.valueOf((int) (Math.random() * 10));
        }

        GameRoom room = new GameRoom(roomId, this);
        rooms.put(roomId, room);

        return roomId;
    }

    public void createRoom(String roomId, Room room) {
        rooms.put(roomId, room);
    }

    public void destroyRoom(String roomId) {
        for (int i = rooms.get(roomId).clients.size(); i > 0; i--) {
            joinRoom(rooms.get(roomId).clients.getFirst(), "lobby");
        }
        rooms.remove(roomId);
    }

    public void joinRoom(Client conn, String roomId) {
//        if (roomId == null || !rooms.containsKey(roomId)) {
//            return;
//        }

        leaveRoom(conn); // Remove client from previous room
        Room room = rooms.get(roomId);
        conn.setRoomId(roomId);
        room.addClient(conn);
    }

    private void leaveRoom(Client conn) {
        Room room = rooms.get(conn.roomId);
        if (room != null) {
            room.removeClient(conn);
        }
    }

//    public void sendToAll(String text) {
//        Collection<WebSocket> con = connections();
//        synchronized (con) {
//            for(WebSocket c : con) {
//                c.send(text);
//            }
//        }
//    }
}
