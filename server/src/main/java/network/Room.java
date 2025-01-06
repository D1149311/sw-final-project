package network;

import java.util.ArrayList;
import java.util.List;

/**
 * The room should extend from it
 */
public class Room {
    public String roomId;
    public List<Client> clients;
    public ChessWebSocket controller;

    /**
     * Initialize the room
     */
    public Room(final String roomId, final ChessWebSocket controller) {
        this.roomId = roomId;
        this.clients = new ArrayList<>();
        this.controller = controller;
    }

    /**
     * add client to the room
     */
    public void addClient(final Client client) {
        clients.add(client);
    }

    /**
     * remove client from the room
     */
    public void removeClient(final Client client) {
        clients.remove(client);
    }

    /**
     * process the message from websocket
     */
    public void processMessage(final Client client, final String message) {
        // It should complete by room
    }
}
