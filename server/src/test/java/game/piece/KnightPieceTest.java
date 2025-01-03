package game.piece;

import game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class KnightPieceTest {
    @Test
    void Test() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        expect.add(new Position(5,6,false));
        expect.add(new Position(3,6,false));
        expect.add(new Position(5,2,false));
        expect.add(new Position(3,2,false));
        expect.add(new Position(6,5,false));
        expect.add(new Position(6,3,false));
        expect.add(new Position(2,5,false));
        expect.add(new Position(2,3,false));
        empty_board[4][4] = new KnightPiece(PieceColor.BLACK);
        List<Position> result = KnightPiece.getPossibleMoves(4,4,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestEat() {
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[4][4] = new KnightPiece(PieceColor.WHITE);
        empty_board[2][5] = new KnightPiece(PieceColor.BLACK);
        expect.add(new Position(5,6,false));
        expect.add(new Position(3,6,false));
        expect.add(new Position(5,2,false));
        expect.add(new Position(3,2,false));
        expect.add(new Position(6,5,false));
        expect.add(new Position(6,3,false));
        expect.add(new Position(2,5,true));
        expect.add(new Position(2,3,false));
        List<Position> result = KnightPiece.getPossibleMoves(4,4,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestBlock(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[4][4] = new KnightPiece(PieceColor.WHITE);
        empty_board[4+2][4+1] = new KnightPiece(PieceColor.WHITE);
        empty_board[4+1][4+2] = new KnightPiece(PieceColor.WHITE);
        empty_board[4-1][4+2] = new KnightPiece(PieceColor.WHITE);
        empty_board[4-2][4+1] = new KnightPiece(PieceColor.WHITE);
        empty_board[4+1][4-2] = new KnightPiece(PieceColor.WHITE);
        empty_board[4+2][4-1] = new KnightPiece(PieceColor.WHITE);
        empty_board[4-1][4-2] = new KnightPiece(PieceColor.WHITE);
        empty_board[4-2][4-1] = new KnightPiece(PieceColor.WHITE);
        List<Position> result = KnightPiece.getPossibleMoves(4,4,empty_board);
        PositionTest.assertPosition(expect,result);
    }
    @Test
    void TestCorner(){
        ChessPiece[][] empty_board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];
        List<Position> expect = new ArrayList<>();
        empty_board[2][1] = new KnightPiece(PieceColor.WHITE);
        empty_board[1][2] = new KnightPiece(PieceColor.WHITE);
        empty_board[0][0] = new KnightPiece(PieceColor.WHITE);
        List<Position> result = KnightPiece.getPossibleMoves(0,0,empty_board);
        PositionTest.assertPosition(expect,result);
    }
}