package game.piece;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
class KingPieceTest {
    @Test
    void Test() {//國王預計有8個可動，實際有九個
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                if(x == 1 && y == 1){
                    y=2;
                }
                expect.add(new Position(x,y,false));
            }
        }
        empty_board[1][1] = new KingPiece(PieceColor.WHITE);
        List<Position> result = KingPiece.getPossibleMoves(1,1,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[1][1] = new KnightPiece(PieceColor.WHITE);
        empty_board[0][0] = new KingPiece(PieceColor.BLACK);
        expect.add(new Position(0,1, false));
        expect.add(new Position(1,0, false));
        expect.add(new Position(1,1, true));
        List<Position> result = KingPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
        //assertEquals(empty_board[0][0].isInCheck(),true);
    }
    @Test
    void TestBlock(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[1][1] = new KingPiece(PieceColor.WHITE);
        empty_board[0][0] = new KingPiece(PieceColor.WHITE);
        expect.add(new Position(0,1, false));
        expect.add(new Position(1,0, false));
        List<Position> result = KingPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestCorner(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[1][1] = new KingPiece(PieceColor.WHITE);
        empty_board[0][1] = new KingPiece(PieceColor.WHITE);
        empty_board[1][0] = new KingPiece(PieceColor.WHITE);
        empty_board[0][0] = new KingPiece(PieceColor.WHITE);
        List<Position> result = KingPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
}