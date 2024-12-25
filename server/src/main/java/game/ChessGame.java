package game;

import java.util.Scanner;

public class ChessGame {
    public static void main(String[] args) {
        ChessBoard chessBoard = new ChessBoard();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Chess game started!");
        chessBoard.printBoard();

        while (true) {
            // 在每回合開始時檢查是否將死或和局
            if (chessBoard.isCheckmate()) {
                System.out.println(chessBoard.turn + " is checkmated! Game over.");
                break;
            }
            if (chessBoard.isDraw()) {
                System.out.println("The game is a draw!");
                break;
            }

            System.out.println(chessBoard.turn + "'s turn.");

            // 在每回合開始時檢查國王是否被威脅
            if (chessBoard.isKingThreatened(chessBoard.turn)) {
                System.out.println("Your king is under threat! You must make a move to remove the threat.");
            }

            System.out.print("Enter move (e.g., A1 A4) or 'exit' to quit: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Game over. Thanks for playing!");
                break;
            }

            String[] positions = input.split(" ");
            if (positions.length != 2) {
                System.out.println("Invalid input. Please enter moves in the format 'A1 A4'.");
                continue;
            }

            Position from = parsePosition(positions[0]);
            Position to = parsePosition(positions[1]);

            if (from == null || to == null) {
                System.out.println("Invalid position. Use coordinates like A1, B2, etc.");
                continue;
            }

            // 嘗試執行移動並檢查是否成功
            boolean success = chessBoard.move(from.x, from.y, to.x, to.y);

            // 如果移動成功，打印棋盤
            if (success) {
                chessBoard.printBoard();
            } else {
                System.out.println("Move failed. Try again.");
            }
        }

        scanner.close();
    }

    private static Position parsePosition(String input) {
        if (input.length() != 2) {
            return null;
        }

        char file = input.charAt(0);
        char rank = input.charAt(1);

        int x = file - 'A';
        int y = '8' - rank;

        if (x < 0 || x >= ChessUtils.BOARD_SIZE || y < 0 || y >= ChessUtils.BOARD_SIZE) {
            return null;
        }

        return new Position(x, y, false);
    }
}
