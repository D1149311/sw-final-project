package game;

import game.piece.RookPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessPieceTest {
    @Test
    void testClone() {
        RookPiece r1 = new RookPiece(PieceColor.BLACK);
        RookPiece r2 = (RookPiece) r1.clone();

        assertFalse(r1 == r2);
        assertEquals(r1.hasMoved(), r2.hasMoved());
        assertEquals(r1.type, r2.type);
        assertEquals(r1.color, r2.color);
    }
}