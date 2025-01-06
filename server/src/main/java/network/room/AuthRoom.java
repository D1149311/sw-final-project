package network.room;

import network.ChessWebSocket;
import network.Client;
import network.Room;
import storage.User;

/**
 * Allow user login or register
 */
public class AuthRoom extends Room {
    private static final String LOGIN_COMMAND = "login";
    private static final String SIGNUP_COMMAND = "signup";

    /**
     * Initialize the room
     */
    public AuthRoom(final ChessWebSocket controller) {
        super("auth", controller);
    }

    @Override
    public void processMessage(final Client client, final String message) {
        super.processMessage(client, message);

        final String[] msg = message.split(" ");
        if (LOGIN_COMMAND.equals(msg[0])) {
            final User user = controller.userService.findUserById(msg[1]);
            if (user != null && user.password.equals(msg[2])) {
                client.conn.send("login success");
                client.userId = msg[1];
                controller.joinRoom(client, "lobby");
            } else {
                client.conn.send("login wrong username or password");
            }
        } else if (SIGNUP_COMMAND.equals(msg[0])) {
            final User user = controller.userService.findUserById(msg[1]);
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
