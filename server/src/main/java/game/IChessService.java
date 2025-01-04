package game;

import java.util.List;

public interface IChessService {
    PieceColor getTurn();
    ChessPiece[][] getBoard();
    boolean isCheckmate();
    boolean isDraw();
    boolean isKingThreatened(PieceColor currentColor);
    boolean move(int fromX, int fromY, int toX, int toY);
    List<Position> getPossibleMoves(int fromX, int fromY);
}
