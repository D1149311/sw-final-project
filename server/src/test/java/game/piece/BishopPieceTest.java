package game.piece;

import game.ChessBoard;
import game.PieceColor;
import game.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class BishopPieceTest {
    @Test
    void Test() {
        ChessBoard empty_board = new ChessBoard();
        List<Position> expect = new ArrayList<>();
        for(int x = 1; x < 8; x++){
            int y = x;
            expect.add(new Position(x,y,false));
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board.board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessBoard empty_board = new ChessBoard();
        List<Position> expect = new ArrayList<>();
        empty_board.board[3][3] = new BishopPiece(PieceColor.WHITE);
        empty_board.board[0][0] = new BishopPiece(PieceColor.BLACK);
        for(int x = 1; x < 4; x++){
            int y = x;
            if(x==3){
                expect.add(new Position(x,y,true));
            }else {
                expect.add(new Position(x,y, false));
            }
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board.board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestBlock(){
        ChessBoard empty_board = new ChessBoard();
        List<Position> expect = new ArrayList<>();
        empty_board.board[3][3] = new BishopPiece(PieceColor.WHITE);
        empty_board.board[0][0] = new BishopPiece(PieceColor.WHITE);
        for(int x = 1; x < 4; x++){
            int y = x;
            if(x==3){
                expect.add(new Position(x,y,true));
            }else {
                expect.add(new Position(x,y, false));
            }
        }
        List<Position> result = BishopPiece.getPossibleMoves(0,0,empty_board.board);
        PositionTest.assertPosition(expect,result);
    }
}