package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class BishopPieceTest {
    @Test
    void Test() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int x = 1; x < 8; x++){
            int y = x;
            expect.add(new Position(x,y,false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];        List<Position> expect = new ArrayList<>();
        empty_board[3][3] = new BishopPiece(PieceColor.WHITE);
        empty_board[0][0] = new BishopPiece(PieceColor.BLACK);
        for(int x = 1; x < 4; x++){
            int y = x;
            if(x==3){
                expect.add(new Position(x,y,true));
            }else {
                expect.add(new Position(x,y, false));
            }
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestBlock(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[3][3] = new BishopPiece(PieceColor.WHITE);
        empty_board[0][0] = new BishopPiece(PieceColor.WHITE);
        for(int x = 1; x < 3; x++){
            int y = x;
            expect.add(new Position(x,y,false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
}