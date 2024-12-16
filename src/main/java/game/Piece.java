package game;

public class Piece {
    private PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public void changeType(PieceType type) {
        this.type = type;
    }

    public PieceType getType() {
        return this.type;
    }

    public PieceColor getColor() {
        return this.color;
    }
}
