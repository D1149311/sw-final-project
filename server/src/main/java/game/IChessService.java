package game;

import java.util.List;

/**
 * A chess should have follow methods
 */
public interface IChessService {
    /**
     * get the turn of the game
     */
    PieceColor getTurn();

    /**
     * get the board of the game
     */
    ChessPiece[][] getBoard();

    /**
     * is the game checkmate
     */
    boolean isCheckmate();

    /**
     * is the game is draw
     */
    boolean isDraw();

    /**
     * is the king is threatened
     */
    boolean isKingThreatened(PieceColor currentColor);

    /**
     * move the piece
     */
    boolean move(int fromX, int fromY, int toX, int toY);

    /**
     * is requesting promote
     */
    boolean requestingPromote();

    /**
     * if is requesting promote then promote the pawn to the choice
     */
    void promotePawn(PieceType choice);

    /**
     * get the possible moves of the piece
     */
    List<Position> getPossibleMoves(int fromX, int fromY);
}
