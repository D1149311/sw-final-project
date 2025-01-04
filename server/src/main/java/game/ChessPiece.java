package game;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 設定棋子的資訊
 **/
public abstract class ChessPiece implements Cloneable {
    public final PieceType type;
    public final PieceColor color;

    /**
     * 設定棋子的類型、顏色
     **/
    public ChessPiece(final PieceType type,final PieceColor color) {
        this.type = type;
        this.color = color;
    }

    /**
     * 針對棋子做完整的複製
     **/
    @Override
    public ChessPiece clone() {
        ChessPiece clonedPiece = null;
        try {
            clonedPiece = (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            Logger.getLogger(ChessPiece.class.getName()).log(Level.SEVERE, "Clone not supported", e);
        }
        return clonedPiece;
    }
}
