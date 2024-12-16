package game;

public class ChessGame {
    public static final int BOARD_SIZE = 8;

    private PieceColor turn; // white: 0, black: 1
    private Piece[][] board;

    public ChessGame() {
        turn = PieceColor.WHITE;
        board = new Piece[BOARD_SIZE][BOARD_SIZE];

        board[0][0] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        board[0][1] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        board[0][2] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        board[0][3] = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        board[0][4] = new Piece(PieceType.KING, PieceColor.BLACK);
        board[0][5] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        board[0][6] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        board[0][7] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        board[1][0] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][1] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][2] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][3] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][4] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][5] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][6] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[1][7] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        board[6][0] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][1] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][2] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][3] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][4] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][5] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][6] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[6][7] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        board[7][0] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        board[7][1] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        board[7][2] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        board[7][3] = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        board[7][4] = new Piece(PieceType.KING, PieceColor.WHITE);
        board[7][5] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        board[7][6] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        board[7][7] = new Piece(PieceType.ROOK, PieceColor.WHITE);
    }

    public Piece[][] getBoard() {
        Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.board[i][j] == null) {
                    board[i][j] = null;
                } else {
                    board[i][j] = new Piece(this.board[i][j].getType(), this.board[i][j].getColor());
                }
            }
        }

        return board;
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
