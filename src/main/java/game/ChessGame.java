package game;

public class ChessGame {
    private static final int BOARD_SIZE = 8;

    private PieceColor turn; // white: 0, black: 1
    private Piece[][] board;

    public ChessGame() {
        turn = PieceColor.WHITE;
        board = new Piece[BOARD_SIZE][BOARD_SIZE];

        board[0][0] = new Piece(PieceType.ROOK, PieceColor.WHITE);
    }

//    public void isValidPos(int x, int y) {
//
//    }
//
//    public void move(int fromX, int fromY, int toX, int toY) {
//
//    }
//
//    public void getTurn() {
//
//    }
//
//    public String checkGameStatus() {
//
//    }
//
//    public boolean isCheckmate() {
//
//    }
//
//    public boolean isKingInCheck() {
//
//    }
//
//    public int[] findKing() {
//
//    }
}
