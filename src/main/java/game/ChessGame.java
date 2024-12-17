package game;

import java.util.ArrayList;
import java.util.List;

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

    public List<Position> getMovableList(int x, int y) {
        if (!isValidPos(x, y) && this.board[y][x] == null) {
            return null;
        }

        if (this.board[y][x].getType() == PieceType.PAWN) {
            if (this.board[y][x].getColor() == PieceColor.WHITE) {
                int[][] deltaList = {{1, -1}, {-1, -1}};
                return calculateMovable(x, y, deltaList);
            } else if (this.board[y][x].getColor() == PieceColor.WHITE) {
                int[][] deltaList = {{1, -1}, {1, 1}};
                return calculateMovable(x, y, deltaList);
            }
        } else if (this.board[y][x].getType() == PieceType.KNIGHT) {
            int[][] deltaList = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
            return calculateMovable(x, y, deltaList);
        } else if (this.board[y][x].getType() == PieceType.BISHOP) {
            int[][] deltaList = {
                {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
                {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7},
                {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
                {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}
            };
            return calculateMovable(x, y, deltaList);
        } else if (this.board[y][x].getType() == PieceType.ROOK) {
            int[][] deltaList = {
                {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0},
                {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0},
                {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
                {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7}
            };
            return calculateMovable(x, y, deltaList);
        } else if (this.board[y][x].getType() == PieceType.QUEEN) {
            int[][] deltaList = {
                {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0},
                {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0},
                {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7},
                {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7},
                {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7},
                {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7},
                {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7},
                {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}
            };
            return calculateMovable(x, y, deltaList);
        } else if (this.board[y][x].getType() == PieceType.KING) {
            int[][] deltaList = {
                {1, -1}, {1, 0}, {1, 1}, {0, 1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
            };

            List<Position> result = calculateMovable(x, y, deltaList);
            List<Position> dangerList = new ArrayList<>();

            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (this.board[i][j] != null && this.board[i][j].getColor() != this.board[y][x].getColor() && this.board[i][j].getType() != PieceType.KING) {
                        dangerList.addAll(getMovableList(j, i));
                    }
                }
            }

            result.removeAll(dangerList);

            return result;
        }

        return null;
    }

    public boolean isValidPos(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    private List<Position> calculateMovable(int x, int y, int[][] deltaList) {
        List<Position> result = new ArrayList<>();

        for (int[] delta : deltaList) {
            if (isValidPos(x + delta[0], y + delta[1])) {
                if (this.board[y][x] == null) {
                    result.add(new Position(x + delta[0], y + delta[1], false));
                } else if (this.board[y + delta[1]][x + delta[0]].getColor() != this.board[y][x].getColor()) {
                    result.add(new Position(x + delta[0], y + delta[1], true));
                }
            }
        }

        return result;
    }

//    public void move(int fromX, int fromY, int toX, int toY) {
//
//    }

    public PieceColor getTurn() {
        return this.turn;
    }
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
