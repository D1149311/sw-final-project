package network.room;

import game.*;
import network.ChessWebSocket;
import network.Client;
import network.Room;

import java.util.List;

public class GameRoom extends Room {
    private Client black, white;
    private IChessService chessService;

    public GameRoom(String id, ChessWebSocket controller) {
        super(id, controller);
    }

    @Override
    public void addClient(Client client) {
        super.addClient(client);

        if (this.clients.size() == 2) {
            white = this.clients.get(1);
            black = this.clients.get(0);

            white.conn.send("start " + white.id + " " + black.id);
            black.conn.send("start " + white.id + " " + black.id);

            chessService = controller.gameService.createGame();

            StringBuilder result = new StringBuilder("move ");

            ChessPiece[][] board = chessService.getBoard();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] != null) {
                        result.append(board[row][col].color + " " + board[row][col].type + " ");
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
    public void processMessage(Client client, String message) {
        super.processMessage(client, message);

        String[] msg = message.split(" ");
        if (msg[0].equals("possible")) {
            StringBuilder result = new StringBuilder("possible ");

            int x = Integer.parseInt(msg[1]);
            int y = Integer.parseInt(msg[2]);

            if (chessService.getTurn() == (client == white ? PieceColor.WHITE : PieceColor.BLACK)) {
                List<Position> positionList = chessService.getPossibleMoves(x, y);
                positionList.forEach((value) -> {
                    result.append(value.col);
                    result.append(" ");
                    result.append(value.row);
                    result.append(" ");
                    result.append(value.eatable);
                    result.append(" ");
                });
            }
            client.conn.send(String.valueOf(result));
        } else if (msg[0].equals("move")) {
            if (chessService.getTurn() == (client == white ? PieceColor.WHITE : PieceColor.BLACK)) {
                int fromX = Integer.parseInt(msg[1]);
                int fromY = Integer.parseInt(msg[2]);
                int toX = Integer.parseInt(msg[3]);
                int toY = Integer.parseInt(msg[4]);
                boolean valid = chessService.move(fromX, fromY, toX, toY);
                if (valid) {
                    StringBuilder result = new StringBuilder("move ");

                    ChessPiece[][] board = chessService.getBoard();
                    for (int row = 0; row < 8; row++) {
                        for (int col = 0; col < 8; col++) {
                            if (board[row][col] != null) {
                                result.append(board[row][col].color + " " + board[row][col].type + " ");
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
                        white.conn.send("end " + chessService.getTurn() + " is checkmated!");
                        black.conn.send("end " + chessService.getTurn() + " is checkmated!");
                        controller.destroyRoom(id);
                    } else if (chessService.isDraw()) {
                        white.conn.send("end " + "The game is a draw!");
                        black.conn.send("end " + "The game is a draw!");
                        controller.destroyRoom(id);
                    } else {
                        if (chessService.isKingThreatened(PieceColor.BLACK)) {
                            white.conn.send("threat " + "BLACK");
                            black.conn.send("threat " + "BLACK");
                        }
                        if (chessService.isKingThreatened(PieceColor.WHITE)) {
                            white.conn.send("threat " + "WHITE");
                            black.conn.send("threat " + "WHITE");
                        }
                    }

                    return;
                }
            }
            client.conn.send("move invalid");
        } else if (msg[0].equals("promote")) {
            // promote pawn
            if (msg[1].equals("QUEEN")) {
                chessService.promotePawn(PieceType.QUEEN);
            } else if (msg[1].equals("BISHOP")) {
                chessService.promotePawn(PieceType.BISHOP);
            } else if (msg[1].equals("KNIGHT")) {
                chessService.promotePawn(PieceType.KNIGHT);
            } else if (msg[1].equals("ROOK")) {
                chessService.promotePawn(PieceType.ROOK);
            }

            if (chessService.requestingPromote()) {
                return;
            }

            // update board
            StringBuilder result = new StringBuilder("move ");

            ChessPiece[][] board = chessService.getBoard();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] != null) {
                        result.append(board[row][col].color + " " + board[row][col].type + " ");
                    } else {
                        result.append("null null ");
                    }
                }
            }

            white.conn.send(String.valueOf(result));
            black.conn.send(String.valueOf(result));

            // check whether the game is over
            if (chessService.isCheckmate()) {
                white.conn.send("end " + chessService.getTurn() + " is checkmated!");
                black.conn.send("end " + chessService.getTurn() + " is checkmated!");
                controller.destroyRoom(id);
            } else if (chessService.isDraw()) {
                white.conn.send("end " + "The game is a draw!");
                black.conn.send("end " + "The game is a draw!");
                controller.destroyRoom(id);
            } else {
                if (chessService.isKingThreatened(PieceColor.BLACK)) {
                    white.conn.send("threat " + "BLACK");
                    black.conn.send("threat " + "BLACK");
                }
                if (chessService.isKingThreatened(PieceColor.WHITE)) {
                    white.conn.send("threat " + "WHITE");
                    black.conn.send("threat " + "WHITE");
                }
            }
        } else {
            client.conn.send(msg[0] + " unknown command");
        }
    }
}
