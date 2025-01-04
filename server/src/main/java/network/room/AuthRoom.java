package network.room;

import network.ChessWebSocket;
import network.Client;
import network.Room;
import storage.User;

import java.lang.reflect.InvocationTargetException;

public class AuthRoom extends Room {
    public AuthRoom(ChessWebSocket controller) {
        super("auth", controller);
    }

    @Override
    public void processMessage(Client client, String message) {
        super.processMessage(client, message);

        String[] msg = message.split(" ");
        if (msg[0].equals("login")) {
            User user = controller.userService.findUserById(msg[1]);
            if (user != null && user.password.equals(msg[2])) {
                client.conn.send("login success");
                client.id = msg[1];
                controller.joinRoom(client, "lobby");
            } else {
                client.conn.send("login wrong username or password");
            }
        } else if (msg[0].equals("signup")) {
            User user = controller.userService.findUserById(msg[1]);
            if (user == null) {
                client.conn.send("signup success");
                controller.userService.createUser(msg[1], msg[2]);
            } else {
                client.conn.send("signup username has already token");
            }
        } else {
            client.conn.send(msg[0] + " unknown command");
        }
    }
}
