package game;

import java.util.List;

public abstract class ChessPiece {
    public final PieceType type;
    public final PieceColor color;

    public ChessPiece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public static List<Position> getPossibleMoves(int x, int y, ChessPiece[][] board) throws Exception {
        throw new Exception("not implemented");
    }
}
