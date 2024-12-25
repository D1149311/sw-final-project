package game;

import java.util.List;

public abstract class ChessPiece implements Cloneable {
    public final PieceType type;
    public final PieceColor color;

    public ChessPiece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
