package network.room;

import network.ChessWebSocket;
import network.Client;
import network.Room;
import storage.User;

import java.util.List;
import java.util.Objects;

public class LobbyRoom extends Room {
    private Client waiting;

    public LobbyRoom(ChessWebSocket controller) {
        super("lobby", controller);
    }

    @Override
    public void processMessage(Client client, String message) {
        super.processMessage(client, message);

        String[] msg = message.split(" ");
        if (msg[0].equals("play")) {
            if (client == waiting) {
                client.conn.send("play unknown command");
                return;
            }

            if (Objects.equals(msg[1], "online")) {
                client.conn.send("play success");

                if (waiting == null) {
                    waiting = client;
                } else {
                    String id = controller.createRoom();
                    controller.joinRoom(waiting, id);
                    controller.joinRoom(client, id);
                    ((GameRoom) controller.rooms.get(id)).point = 100;
                    waiting = null;
                }
            } else if (Objects.equals(msg[1], "create")) {
                client.conn.send("play success");

                String id = controller.createRoom();
                client.conn.send("id " + id);
                controller.joinRoom(client, id);
            } else if (Objects.equals(msg[1], "friend")) {
                GameRoom room = (GameRoom) controller.rooms.get(msg[2]);
                if (room != null && room.point == 0 && room.clients.size() < 2) {
                    System.out.println();
                    client.conn.send("play success");
                    controller.joinRoom(client, msg[2]);
                } else {
                    client.conn.send("play cannot find the room");
                }
            } else {
                client.conn.send("play unknown command");
            }
        } else if (msg[0].equals("top10")) {
            List<User> top10 = controller.userService.findTop10ByOrderByPointDesc();
            StringBuilder result = new StringBuilder("top10 ");

            for (User user : top10) {
                result.append(user.id).append(" ").append(user.point).append(" ");
            }
            client.conn.send(result.toString());
        } else if (msg[0].equals("logout")) {
            client.conn.send("logout success");
            controller.joinRoom(client, "auth");
        } else if (msg[0].equals("info")) {
            User user = controller.userService.findUserById(client.id);
            client.conn.send("info " + user.id + " " + user.point);
        } else {
            client.conn.send(msg[0] + " unknown command");
        }
    }

    @Override
    public void removeClient(Client client) {
        super.removeClient(client);

        if (client == waiting) {
            waiting = null;
        }
    }
}
