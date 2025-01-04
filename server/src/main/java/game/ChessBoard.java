package game;

import game.piece.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * 判定棋局的移動、勝負
 **/
public class ChessBoard implements IChessService {
    public ChessPiece[][] board;
    public PieceColor turn;
    private Position lastMove;
    private boolean requestPromote = false;
    private ChessPiece promotePawn;
    private int promoteX, promoteY;

    /**
     * 初始化棋盤
     **/
    public ChessBoard() {
        initializeBoard();
        initializeGame();
    }

    public PieceColor getTurn() {
        return this.turn;
    }

    public ChessPiece[][] getBoard() {
        return this.board;
    }

    private void initializeBoard() {
        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];

        // 初始化棋盤上的其他棋子
        board[0][0] = new RookPiece(PieceColor.BLACK);
        board[0][7] = new RookPiece(PieceColor.BLACK);
        board[7][0] = new RookPiece(PieceColor.WHITE);
        board[7][7] = new RookPiece(PieceColor.WHITE);

        board[0][2] = new BishopPiece(PieceColor.BLACK);
        board[0][5] = new BishopPiece(PieceColor.BLACK);
        board[7][2] = new BishopPiece(PieceColor.WHITE);
        board[7][5] = new BishopPiece(PieceColor.WHITE);

        board[0][1] = new KnightPiece(PieceColor.BLACK);
        board[0][6] = new KnightPiece(PieceColor.BLACK);
        board[7][1] = new KnightPiece(PieceColor.WHITE);
        board[7][6] = new KnightPiece(PieceColor.WHITE);

        board[0][3] = new QueenPiece(PieceColor.BLACK);
        board[7][3] = new QueenPiece(PieceColor.WHITE);

        for (int i = 0; i < ChessUtils.BOARD_SIZE; i++) {
            board[1][i] = new PawnPiece(PieceColor.BLACK);
            board[6][i] = new PawnPiece(PieceColor.WHITE);
        }
        board[0][4] = new KingPiece(PieceColor.BLACK);
        board[7][4] = new KingPiece(PieceColor.WHITE);
    }

    private void initializeGame() {
        turn = PieceColor.WHITE;
    }
    /**
     * 檢查己方的國王是否受到威脅
     **/
    public boolean isKingThreatened(final PieceColor currentColor) {
        boolean result = false;
        // 獲取對手的棋子
        final PieceColor opponentColor = currentColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        // 查找對方棋子攻擊範圍
        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                final ChessPiece piece = board[y][x];
                if (piece != null && piece.color == opponentColor) {
                    final List<Position> attackRange = getAttackRangeForPiece(piece, x, y);
                    for (final Position pos : attackRange) {
                        // 檢查是否對方的攻擊範圍包含自己的國王
                        if (board[pos.row][pos.col] != null && board[pos.row][pos.col].type == PieceType.KING &&
                                board[pos.row][pos.col].color == currentColor) {
                            result = true; // 王被威脅
                        }
                    }
                }
            }
        }
        return result; // 王沒被威脅
    }

    private List<Position> getAttackRangeForPiece(final ChessPiece piece, final int col, final int row) {
        List<Position> attackRange = new ArrayList<>();
        switch (piece.type) {
            case ROOK:
                attackRange = RookPiece.getPossibleMoves(col, row, board);
                break;
            case BISHOP:
                attackRange = BishopPiece.getPossibleMoves(col, row, board);
                break;
            case QUEEN:
                attackRange = QueenPiece.getPossibleMoves(col, row, board);
                break;
            case KNIGHT:
                attackRange = KnightPiece.getPossibleMoves(col, row, board);
                break;
            case PAWN:
                attackRange = PawnPiece.getPossibleMoves(col, row, board, lastMove);
                break;
            case KING:
                attackRange = KingPiece.getPossibleMoves(col, row, board);
                break;
        }
        return attackRange;
    }
    /**
     * 檢查移動後己方的國王是否移除威脅
     **/
    public boolean canRemoveThreat(final int fromX, final int fromY, final int toX, final int toY, final ChessPiece[][] board) {
        final ChessPiece piece = board[fromY][fromX];
        List<Position> possibleMoves = new ArrayList<>();
        boolean canMove = false;
        boolean result = false;

        // 获取该棋子所有的可能移动
        switch (piece.type) {
            case ROOK:
                possibleMoves = RookPiece.getPossibleMoves(fromX, fromY, board);
                break;
            case BISHOP:
                possibleMoves = BishopPiece.getPossibleMoves(fromX, fromY, board);
                break;
            case QUEEN:
                possibleMoves = QueenPiece.getPossibleMoves(fromX, fromY, board);
                break;
            case KNIGHT:
                possibleMoves = KnightPiece.getPossibleMoves(fromX, fromY, board);
                break;
            case PAWN:
                possibleMoves = PawnPiece.getPossibleMoves(fromX, fromY, board, lastMove);
                break;
            case KING:
                possibleMoves = KingPiece.getPossibleMoves(fromX, fromY, board);
                break;
        }

        // 检查移动是否合法
        for (final Position pos : possibleMoves) {
            if (pos.col == toX && pos.row == toY) {
                canMove = true;
                break;
            }
        }

        // 如果能够合法移动
        if (canMove) {
            // 模拟棋盘的移动并判断是否能消除威胁
            final ChessBoard tempBoard = new ChessBoard();
            tempBoard.board = deepCopyBoard(board);  // 使用深拷贝的方法
            tempBoard.turn = this.turn; // 傳遞當前玩家的回合
            tempBoard.lastMove = this.lastMove; // 確保最後移動位置也同步

            // 模擬移動
            tempBoard.board[toY][toX] = tempBoard.board[fromY][fromX];
            tempBoard.board[fromY][fromX] = null;

            // 如果移动后王没有被威胁，返回true
            result = !tempBoard.isKingThreatened(tempBoard.turn);
        }

        return result;
    }

    // 深拷贝棋盘的方法
    private ChessPiece[][] deepCopyBoard(final ChessPiece[][] originalBoard) {
        final ChessPiece[][] copy = new ChessPiece[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            for (int j = 0; j < originalBoard[i].length; j++) {
                if (originalBoard[i][j] != null) {
                    copy[i][j] = originalBoard[i][j].clone();  // 假设棋子类实现了Cloneable接口
                }
            }
        }
        return copy;
    }

    public List<Position> getPossibleMoves(int fromX, int fromY) {
        List<Position> possibleMoves = new ArrayList<>();
        List<Position> availableMoves = new ArrayList<>();

        ChessPiece piece = board[fromY][fromX];
        if (piece != null && piece.color == turn) {
            // 判斷是否是可以移動的棋子
            switch (piece.type) {
                case ROOK:
                    possibleMoves = RookPiece.getPossibleMoves(fromX, fromY, board);
                    break;
                case BISHOP:
                    possibleMoves = BishopPiece.getPossibleMoves(fromX, fromY, board);
                    break;
                case QUEEN:
                    possibleMoves = QueenPiece.getPossibleMoves(fromX, fromY, board);
                    break;
                case KNIGHT:
                    possibleMoves = KnightPiece.getPossibleMoves(fromX, fromY, board);
                    break;
                case PAWN:
                    possibleMoves = PawnPiece.getPossibleMoves(fromX, fromY, board, lastMove);
                    break;
                case KING:
                    possibleMoves = KingPiece.getPossibleMoves(fromX, fromY, board);
                    break;
            }

            for (Position pos : possibleMoves) {
                if (canRemoveThreat(fromX, fromY, pos.col, pos.row, board)) {
                    availableMoves.add(pos);
                }
            }
        }

        return availableMoves;
    }

    /**
     * 旗子的移動邏輯
     **/
    public boolean move(final int fromX, final int fromY, final int toX, final int toY) {
        boolean result = false;  // Variable to store the result

        // Perform all checks and move handling before returning
        if (ChessUtils.isValidPos(fromX, fromY) && ChessUtils.isValidPos(toX, toY) &&
                board[fromY][fromX] != null && board[fromY][fromX].color == turn) {

            if (isKingThreatened(turn) && !canRemoveThreat(fromX, fromY, toX, toY, board)) {
                System.out.println("You cannot make this move because your king is still under threat.");
            } else {
                final ChessPiece piece = board[fromY][fromX];
                List<Position> possibleMoves = getPossibleMoves(fromX, fromY);
                boolean canMove = false;

                // Check if the move is valid
                for (final Position pos : possibleMoves) {
                    if (pos.col == toX && pos.row == toY) {
                        canMove = true;
                        break;
                    }
                }

                // King-side and Queen-side castling checks
                if (piece.type == PieceType.KING) {
                    final KingPiece king = (KingPiece) piece;

                    if ((fromX == 4 && toX == 6 && fromY == toY && !king.hasMoved()) ||
                            (fromX == 4 && toX == 2 && fromY == toY && !king.hasMoved())) {

                        final ChessPiece rook = (fromX == 4 && toX == 6) ? board[toY][7] : board[toY][0];  // Select the correct rook based on castling direction
                        final int rookDestX = (fromX == 4 && toX == 6) ? 5 : 3;
                        final int kingDestX = (fromX == 4 && toX == 6) ? 6 : 2;

                        if (rook == null || rook.type != PieceType.ROOK || rook.color != turn || ((RookPiece) rook).hasMoved()) {
                            System.out.println("Error: Rook is either missing, has already moved, or is not the correct color.");
                        } else {
                            // Perform castling
                            board[toY][rookDestX] = rook;
                            board[toY][kingDestX] = king;
                            board[fromY][4] = null;
                            board[toY][(fromX == 4 && toX == 6) ? 7 : 0] = null;
                            ((RookPiece) board[toY][rookDestX]).setHasMoved();
                            ((KingPiece) board[toY][kingDestX]).setHasMoved();
                            result = true;  // Set result to true as castling is valid
                        }
                    }
                }

                if (canMove) {
                    // Handle pawn promotion
                    if (piece.type == PieceType.PAWN) {
                        final PawnPiece pawn = (PawnPiece) piece;
                        if (shouldPromotePawn(pawn, toY)) {
                            requestPromote = true;
                            promotePawn = pawn;
                            promoteX = toX;
                            promoteY = toY;
                            board[toY][toX] = board[fromY][fromX];
                            board[fromY][fromX] = null;
                            result = true;
                        } else {
                            // Handle en passant
                            if (pawn.enPassantCapture(fromX, fromY, toX, toY, board, lastMove)) {
                                System.out.println("En Passant captured!");
                                board[fromY][toX] = null;  // Remove captured pawn
                            } else {
                                board[toY][toX] = board[fromY][fromX];
                            }
                            board[fromY][fromX] = null;
                            pawn.setMoved(true);
                            result = true;
                        }
                    } else {
                        if (canRemoveThreat(fromX, fromY, toX, toY, board)) {
                            // Handle piece-specific movement
                            if (piece.type == PieceType.ROOK) {
                                ((RookPiece) piece).setHasMoved();
                            } else if (piece.type == PieceType.KING) {
                                ((KingPiece) piece).setHasMoved();
                            }

                            // Perform the move
                            board[toY][toX] = board[fromY][fromX];
                            board[fromY][fromX] = null;
                            result = true;  // Mark the move as successful
                        } else {
                            System.out.println("You cannot make this move because your king is still under threat.");
                        }

                    }

                    lastMove = new Position(toX, toY, false);
                    if (!requestPromote) {
                        turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE; // Switch turn
                    }
                    printBoard();
                }
            }
        } else {
            System.out.println("Invalid position or no piece to move.");
        }

        // Return the result after all processing
        return result;
    }


    /**
     * 判斷是否需要升變。
     */
    private boolean shouldPromotePawn(final PawnPiece pawn, final int toY) {
        return (pawn.color == PieceColor.WHITE && toY == 0)
                || (pawn.color == PieceColor.BLACK && toY == ChessUtils.BOARD_SIZE - 1);
    }

    public boolean requestingPromote() {
        return requestPromote;
    }

    /**
     * 處理兵的升變邏輯。
     */
    public void promotePawn(PieceType choice) {
        if (!this.requestPromote) return;

        ChessPiece promotedPiece = null;
        switch (choice) {
            case PieceType.QUEEN:
                promotedPiece = new QueenPiece(promotePawn.color);
                break;
            case PieceType.ROOK:
                promotedPiece = new RookPiece(promotePawn.color);
                break;
            case PieceType.BISHOP:
                promotedPiece = new BishopPiece(promotePawn.color);
                break;
            case PieceType.KNIGHT:
                promotedPiece = new KnightPiece(promotePawn.color);
                break;
        }

        if (promotedPiece != null) {
            // Replace the pawn with the new promoted piece
            board[promoteY][promoteX] = promotedPiece;
            turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE; // Switch turn
            this.requestPromote = false;
            System.out.println("Pawn promoted to " + promotedPiece.getClass().getSimpleName());
        }
    }


    private boolean hasLegalMoves(final PieceColor currentColor) {
        boolean result = false;
        final ChessBoard tempBoard = new ChessBoard();
        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                final ChessPiece piece = board[y][x];
                if (piece != null && piece.color == currentColor) {
                    final List<Position> possibleMoves = getAttackRangeForPiece(piece, x, y);
                    for (final Position move : possibleMoves) {
                        tempBoard.board = deepCopyBoard(board);
                        tempBoard.turn = currentColor;
                        tempBoard.lastMove = lastMove;

                        tempBoard.board[y][x] = tempBoard.board[move.row][move.col];
                        tempBoard.board[move.row][move.col] = null;

                        if (!tempBoard.isKingThreatened(currentColor)) {
                            result = true; // 找到一個合法移動
                        }
                    }
                }
            }
        }
        return result; // 無任何合法移動
    }

    private boolean islowDraw() {
        int whitePieces = 0, blackPieces = 0;
        boolean whiteHBK = false, blackHBK = false;

        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                final ChessPiece piece = board[y][x];
                if (piece != null) {
                    if (piece.color == PieceColor.WHITE) {
                        whitePieces++;
                        if (piece.type == PieceType.BISHOP || piece.type == PieceType.KNIGHT) {
                            whiteHBK = true;
                        }
                    } else {
                        blackPieces++;
                        if (piece.type == PieceType.BISHOP || piece.type == PieceType.KNIGHT) {
                            blackHBK = true;
                        }
                    }
                }
            }
        }

        // 檢查是否僅剩國王，或國王加輕子
        return (whitePieces == 1 || (whitePieces == 2 && whiteHBK)) &&
                (blackPieces == 1 || (blackPieces == 2 && blackHBK));
    }
    /**
     * 檢查棋局是否和局
     **/
    public boolean isDraw() {
        boolean result = false;
        // 1. 判斷是否無法合法移動（悶死局面）
        if (!hasLegalMoves(turn) && !isKingThreatened(turn)) {
            System.out.println("Stalemate! The game is a draw.");
            result = true;
        }

        // 2. 檢查是否為物理和棋（僅剩國王或國王與輕子）
        if (islowDraw()) {
            System.out.println("Insufficient material! The game is a draw.");
            result = true;
        }

        // 3. 其他情況（如三次重複局面或 50 步規則）可根據需要實現
        return result;
    }
    /**
     * 檢查棋局是否將死
     **/
    public boolean isCheckmate() {
        boolean result = false;
        if (isKingThreatened(turn) && !hasLegalMoves(turn)) {
            System.out.println(turn + " is checkmated! The game is over.");
            result = true;
        }
        return result;
    }
    /**
     * 劃出棋盤
     **/
    public void printBoard() {
        System.out.print("   "); // 留空間給行號
        for (char c = 'A'; c < 'A' + ChessUtils.BOARD_SIZE; c++) {
            System.out.print(c + "   ");
        }
        System.out.println();

        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            System.out.print((ChessUtils.BOARD_SIZE - y) + "  "); // 行號
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                if (board[y][x] == null) {
                    System.out.print(".   "); // 空格用 "." 表示
                } else {
                    final String pieceSymbol = board[y][x].color.toString().charAt(0) + "" + board[y][x].type.toString().charAt(0);
                    System.out.print(pieceSymbol + " "); // 棋子符號
                    if (pieceSymbol.length() == 2) {
                        System.out.print(" "); // 添加額外空格保持對齊
                    }
                }
            }
            System.out.println((ChessUtils.BOARD_SIZE - y)); // 行號
        }

        System.out.print("   "); // 留空間給行號
        for (char c = 'A'; c < 'A' + ChessUtils.BOARD_SIZE; c++) {
            System.out.print(c + "   ");
        }
        System.out.println();
    }
}