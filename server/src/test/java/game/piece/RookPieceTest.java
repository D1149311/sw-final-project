package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RookPieceTest {
    @Test
    void Test() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int x = 0; x < 8; x++){
            if(x==3)x++;
            int y = x;
            expect.add(new Position(x,3,false));
            expect.add(new Position(3,y,false));
        }
        empty_board[3][3] = new RookPiece(PieceColor.WHITE);
        List<Position> result = RookPiece.getPossibleMoves(3,3,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        expect.add(new Position(3,4,false));
        expect.add(new Position(4,3,false));
        expect.add(new Position(3,2,false));
        expect.add(new Position(2,3,false));
        empty_board[3][3] = new RookPiece(PieceColor.WHITE);
        empty_board[4][3] = new RookPiece(PieceColor.BLACK);
        empty_board[3][4] = new RookPiece(PieceColor.BLACK);
        empty_board[2][3] = new RookPiece(PieceColor.BLACK);
        empty_board[3][2] = new RookPiece(PieceColor.BLACK);
        List<Position> result = RookPiece.getPossibleMoves(3,3,empty_board);
        PositionTest.assertPosition(expect,result);
    }
}