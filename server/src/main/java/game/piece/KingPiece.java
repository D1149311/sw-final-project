package game.piece;

import game.ChessPiece;
import game.PieceColor;
import game.PieceType;

public class KingPiece extends ChessPiece {
    public KingPiece(PieceColor color) {
        super(PieceType.KING, color);
    }
}
