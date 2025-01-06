package network.room;

import network.ChessWebSocket;
import network.Client;
import network.Room;
import storage.User;

import java.util.List;

/**
 * allow user to create a game room
 */
public class LobbyRoom extends Room {
    private Client waiting;

    private static final String COMMAND_PLAY = "play";
    private static final String COMMAND_TOP10 = "top10";
    private static final String COMMAND_LOGOUT = "logout";
    private static final String COMMAND_INFO = "info";
    private static final String MODE_ONLINE = "online";
    private static final String MODE_CREATE = "create";
    private static final String MODE_FRIEND = "friend";

    /**
     * Initialize the room
     */
    public LobbyRoom(final ChessWebSocket controller) {
        super("lobby", controller);
    }

    @Override
    public void processMessage(final Client client, final String message) {
        super.processMessage(client, message);

        final String[] msg = message.split(" ");
        if (COMMAND_PLAY.equals(msg[0])) {
            if (client.equals(waiting)) {
                client.conn.send("play unknown command");
                return;
            }

            if (MODE_ONLINE.equals(msg[1])) {
                client.conn.send("play success");

                if (waiting == null) {
                    waiting = client;
                } else {
                    final String roomId = controller.createRoom();
                    controller.joinRoom(waiting, roomId);
                    controller.joinRoom(client, roomId);
                    ((GameRoom) controller.rooms.get(roomId)).point = 100;
                    waiting = null;
                }
            } else if (MODE_CREATE.equals(msg[1])) {
                client.conn.send("play success");

                final String roomId = controller.createRoom();
                client.conn.send("id " + roomId);
                controller.joinRoom(client, roomId);
            } else if (MODE_FRIEND.equals(msg[1])) {
                final GameRoom room = (GameRoom) controller.rooms.get(msg[2]);
                if (room != null && room.point == 0 && room.clients.size() < 2) {
                    client.conn.send("play success");
                    controller.joinRoom(client, msg[2]);
                } else {
                    client.conn.send("play cannot find the room");
                }
            } else {
                client.conn.send("play unknown command");
            }
        } else if (COMMAND_TOP10.equals(msg[0])) {
            final List<User> top10 = controller.userService.findTop10ByOrderByPointDesc();
            final StringBuilder result = new StringBuilder("top10 ");

            for (final User user : top10) {
                result.append(user.userId).append(' ').append(user.point).append(' ');
            }
            client.conn.send(result.toString());
        } else if (COMMAND_LOGOUT.equals(msg[0])) {
            controller.joinRoom(client, "auth");
            client.conn.send("logout success");
        } else if (COMMAND_INFO.equals(msg[0])) {
            final User user = controller.userService.findUserById(client.userId);
            client.conn.send("info " + user.userId + " " + user.point);
        } else {
            client.conn.send(msg[0] + " unknown command");
        }
    }

    @Override
    public void removeClient(final Client client) {
        super.removeClient(client);

        if (client.equals(waiting)) {
            waiting = null;
        }
    }
}
