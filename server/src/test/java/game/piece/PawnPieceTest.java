package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

//Test
class PawnPieceTest {
    @Test
    void TestBlack() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        expect.add(new Position(0,2,false));
        expect.add(new Position(0,3,false));
        empty_board[1][0] = new PawnPiece(PieceColor.BLACK);
        List<Position> result = PawnPiece.getPossibleMoves(0,1,empty_board, null);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestWhite() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        expect.add(new Position(0,5,false));
        expect.add(new Position(0,4,false));
        empty_board[6][0] = new PawnPiece(PieceColor.WHITE);
        List<Position> result = PawnPiece.getPossibleMoves(0,6,empty_board, null);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[2][1] = new PawnPiece(PieceColor.WHITE);
        empty_board[1][0] = new PawnPiece(PieceColor.BLACK);
        expect.add(new Position(1,2,true));
        expect.add(new Position(0,2,true));
        expect.add(new Position(0,3,true));
        List<Position> result = PawnPiece.getPossibleMoves(0,1,empty_board,null);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestBlock(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[2][0] = new PawnPiece(PieceColor.BLACK);
        empty_board[1][0] = new PawnPiece(PieceColor.BLACK);
        List<Position> result = PawnPiece.getPossibleMoves(0,1,empty_board,null);
        PositionTest.assertPosition(expect,result);
    }
}