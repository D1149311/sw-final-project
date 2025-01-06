package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class QueenPieceTest {
    @Test
    void Test() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int i = 1; i < ChessUtils.BOARD_SIZE/2; i++){
            expect.add(new Position(4+i,4,false));
            expect.add(new Position(4-i,4,false));
            expect.add(new Position(4,4+i,false));
            expect.add(new Position(4,4-i,false));
            expect.add(new Position(4+i,4+i,false));
            expect.add(new Position(4-i,4-i,false));
            expect.add(new Position(4-i,4+i,false));
            expect.add(new Position(4+i,4-i,false));
        }
        expect.add(new Position(0,4,false));
        expect.add(new Position(4,0,false));
        expect.add(new Position(0,0,false));
        empty_board[4][4] = new QueenPiece(PieceColor.WHITE);
        List<Position> result = QueenPiece.getPossibleMoves(4,4,empty_board);
        PositionTest.assertPosition(expect,result);
    }
}