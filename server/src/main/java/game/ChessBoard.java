package game;

import game.piece.RookPiece;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    public ChessPiece[][] board;
    public PieceColor turn;

    public ChessBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];

        board[0][0] = new RookPiece(PieceColor.BLACK);
        board[0][7] = new RookPiece(PieceColor.BLACK);
        board[7][0] = new RookPiece(PieceColor.WHITE);
        board[7][7] = new RookPiece(PieceColor.WHITE);
    }

    private void initializeGame() {
        turn = PieceColor.WHITE;
    }

    public boolean move(int fromX, int fromY, int toX, int toY) {
        if (board[fromY][fromX] == null) {
            return false;
        }

        if (board[fromY][fromX].color != turn) {
            return false;
        }

        List<Position> result = new ArrayList<>();
        if (board[fromY][fromX].type == PieceType.ROOK) {
            result = RookPiece.getPossibleMoves(fromX, fromY, board);
        }

        boolean canMove = false;
        for (Position pos : result) {
            if (pos.x == toX && pos.y == toY) {
                canMove = true;
            }
        }

        if (canMove) {
            board[toY][toX] = board[fromY][fromX];
            board[fromY][fromX] = null;

            turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        }

        return canMove;
    }
}
