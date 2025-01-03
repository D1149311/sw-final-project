package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

//Cannot read field "color" because "pawn" is null
class PawnPieceTest {
    @Test
    void Test() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        for(int x = 1; x < 8; x++){
            int y = x;
            expect.add(new Position(x,y,false));
        }
        empty_board[0][1] = new PawnPiece(PieceColor.WHITE);
        List<Position> result = PawnPiece.getPossibleMoves(0,1,empty_board, null);
        PositionTest.assertPosition(expect,result);
    }
//    @Test
//    void TestEat() {
//        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expect = new ArrayList<>();
//        empty_board[1][1] = new PawnPiece(PieceColor.WHITE);
//        empty_board[0][0] = new PawnPiece(PieceColor.BLACK);
//        List<Position> result = PawnPiece.getPossibleMoves(0,0,empty_board,null);
//        PositionTest.assertPosition(expect,result);
//    }
//    @Test
//    void TestBlock(){
//        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expect = new ArrayList<>();
//        empty_board[3][3] = new PawnPiece(PieceColor.WHITE);
//        empty_board[0][0] = new PawnPiece(PieceColor.WHITE);
//        for(int x = 1; x < 3; x++){
//            int y = x;
//            expect.add(new Position(x,y,false));
//        }
//        List<Position> result = PawnPiece.getPossibleMoves(0,0,empty_board,null);
//        PositionTest.assertPosition(expect,result);
//    }
//    @Test
//    void TestCorner(){
//        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
//        List<Position> expect = new ArrayList<>();
//        empty_board[1][1] = new PawnPiece(PieceColor.WHITE);
//        empty_board[0][0] = new PawnPiece(PieceColor.WHITE);
//        List<Position> result = PawnPiece.getPossibleMoves(0,0,empty_board,null);
//        PositionTest.assertPosition(expect,result);
//    }
}