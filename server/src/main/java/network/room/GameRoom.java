package network.room;

import game.*;
import network.ChessWebSocket;
import network.Client;
import network.Room;
import storage.User;

import java.util.List;

/**
 * Chess game room
 */
public class GameRoom extends Room {
    private Client black, white;
    private IChessService chessService;
    private boolean gameOver;
    public int point;

    private static final String POSSIBLE_COMMAND = "possible";
    private static final String MOVE_COMMAND = "move";
    private static final String PROMOTE_COMMAND = "promote";

    private static final String QUEEN = "QUEEN";
    private static final String BISHOP = "BISHOP";
    private static final String KNIGHT = "KNIGHT";
    private static final String ROOK = "ROOK";

    private static final int MAX_CLIENTS = 2;

    /**
     * Initialize the game room
     */
    public GameRoom(final String roomId, final ChessWebSocket controller) {
        super(roomId, controller);
    }

    @Override
    public void addClient(final Client client) {
        super.addClient(client);

        if (this.clients.size() == MAX_CLIENTS) {
            white = this.clients.get(1);
            black = this.clients.get(0);

            white.conn.send("start " + white.userId + " " + black.userId);
            black.conn.send("start " + white.userId + " " + black.userId);

            chessService = controller.gameService.createGame();

            final StringBuilder result = new StringBuilder("move ");

            final ChessPiece[][] board = chessService.getBoard();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] != null) {
                        result.append(board[row][col].color).append(' ').append(board[row][col].type).append(' ');
                    } else {
                        result.append("null null ");
                    }
                }
            }

            white.conn.send(String.valueOf(result));
            black.conn.send(String.valueOf(result));
        }
    }

    @Override
    public void removeClient(final Client client) {
        super.removeClient(client);

        if (!gameOver) {
            gameOver = true;
            controller.destroyRoom(roomId);
            final Client winner = (client.equals(white) ? black : white);
            if (winner != null) {
                final User user = controller.userService.findUserById(winner.userId);
                user.point = String.valueOf(Integer.parseInt(user.point) + point);
                controller.userService.save();
                winner.conn.send("end " + client.userId + " leave the room");
            }
        }
    }

    @Override
    public void processMessage(final Client client, final String message) {
        super.processMessage(client, message);

        final String[] msg = message.split(" ");

        if (clients.size() < MAX_CLIENTS) {
            client.conn.send(msg[0] + " unknown command");
        } else {

            if (POSSIBLE_COMMAND.equals(msg[0])) {
                final StringBuilder result = new StringBuilder("possible ");

                final int col = Integer.parseInt(msg[1]);
                final int row = Integer.parseInt(msg[2]);

                if (chessService.getTurn() == (client.equals(white) ? PieceColor.WHITE : PieceColor.BLACK)) {
                    final List<Position> positionList = chessService.getPossibleMoves(col, row);
                    positionList.forEach((value) -> {
                        result.append(value.col).append(' ').append(value.row).append(' ').append(value.eatable).append(' ');
                    });
                }
                client.conn.send(String.valueOf(result));
            } else if (MOVE_COMMAND.equals(msg[0])) {
                if (chessService.getTurn() == (client.equals(white) ? PieceColor.WHITE : PieceColor.BLACK)) {
                    final int fromX = Integer.parseInt(msg[1]);
                    final int fromY = Integer.parseInt(msg[2]);
                    final int toX = Integer.parseInt(msg[3]);
                    final int toY = Integer.parseInt(msg[4]);
                    final boolean valid = chessService.move(fromX, fromY, toX, toY);
                    if (valid) {
                        final StringBuilder result = new StringBuilder("move ");

                        final ChessPiece[][] board = chessService.getBoard();
                        for (int row = 0; row < 8; row++) {
                            for (int col = 0; col < 8; col++) {
                                if (board[row][col] != null) {
                                    result.append(board[row][col].color).append(' ').append(board[row][col].type).append(' ');
                                } else {
                                    result.append("null null ");
                                }
                            }
                        }

                        white.conn.send(String.valueOf(result));
                        black.conn.send(String.valueOf(result));

                        if (chessService.requestingPromote()) {
                            client.conn.send("promote");
                        } else if (chessService.isCheckmate()) {
                            gameOver = true;
                            final User user = controller.userService.findUserById(client.userId);
                            user.point = String.valueOf(Integer.parseInt(user.point) + point);
                            controller.userService.save();
                            controller.destroyRoom(roomId);
                            white.conn.send("end " + chessService.getTurn() + " is checkmated!");
                            black.conn.send("end " + chessService.getTurn() + " is checkmated!");
                        } else if (chessService.isDraw()) {
                            gameOver = true;
                            controller.destroyRoom(roomId);
                            white.conn.send("end The game is a draw!");
                            black.conn.send("end The game is a draw!");
                        } else {
                            if (chessService.isKingThreatened(PieceColor.BLACK)) {
                                white.conn.send("threat BLACK");
                                black.conn.send("threat BLACK");
                            }
                            if (chessService.isKingThreatened(PieceColor.WHITE)) {
                                white.conn.send("threat WHITE");
                                black.conn.send("threat WHITE");
                            }
                        }
                    } else {
                        client.conn.send("move invalid");
                    }
                } else {
                    client.conn.send("move invalid");
                }
            } else if ((chessService.getTurn() == (client.equals(white) ? PieceColor.WHITE : PieceColor.BLACK)) && PROMOTE_COMMAND.equals(msg[0])) {
                // promote pawn
                if (QUEEN.equals(msg[1])) {
                    chessService.promotePawn(PieceType.QUEEN);
                } else if (BISHOP.equals(msg[1])) {
                    chessService.promotePawn(PieceType.BISHOP);
                } else if (KNIGHT.equals(msg[1])) {
                    chessService.promotePawn(PieceType.KNIGHT);
                } else if (ROOK.equals(msg[1])) {
                    chessService.promotePawn(PieceType.ROOK);
                }

                if (chessService.requestingPromote()) {
                    return;
                }

                // update board
                final StringBuilder result = new StringBuilder("move ");

                final ChessPiece[][] board = chessService.getBoard();
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] != null) {
                            result.append(board[row][col].color).append(' ').append(board[row][col].type).append(' ');
                        } else {
                            result.append("null null ");
                        }
                    }
                }

                white.conn.send(String.valueOf(result));
                black.conn.send(String.valueOf(result));

                // check whether the game is over
                if (chessService.isCheckmate()) {
                    gameOver = true;
                    final User user = controller.userService.findUserById(client.userId);
                    user.point = String.valueOf(Integer.parseInt(user.point) + point);
                    controller.userService.save();
                    controller.destroyRoom(roomId);
                    white.conn.send("end " + chessService.getTurn() + " is checkmated!");
                    black.conn.send("end " + chessService.getTurn() + " is checkmated!");
                } else if (chessService.isDraw()) {
                    gameOver = true;
                    controller.destroyRoom(roomId);
                    white.conn.send("end The game is a draw!");
                    black.conn.send("end The game is a draw!");
                } else {
                    if (chessService.isKingThreatened(PieceColor.BLACK)) {
                        white.conn.send("threat BLACK");
                        black.conn.send("threat BLACK");
                    }
                    if (chessService.isKingThreatened(PieceColor.WHITE)) {
                        white.conn.send("threat WHITE");
                        black.conn.send("threat WHITE");
                    }
                }
            } else {
                client.conn.send(msg[0] + " unknown command");
            }
        }
    }
}
