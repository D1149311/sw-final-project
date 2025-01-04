package network;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {
    public String id;
    public List<Client> clients;
    public ChessWebSocket controller;

    public Room(String id, ChessWebSocket controller) {
        this.id = id;
        this.clients = new ArrayList<>();
        this.controller = controller;
    }

    public void addClient(Client client) {
        clients.add(client);
        System.out.println("add client to the room");
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void processMessage(Client client, String message) { }
}
