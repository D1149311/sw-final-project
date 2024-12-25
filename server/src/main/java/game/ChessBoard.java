package game;

import game.piece.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    public ChessPiece[][] board;
    public PieceColor turn;
    private Position lastMove;
    private boolean isSimulating = false;

    public ChessBoard() {
        initializeBoard();
        initializeGame();
    }

    private void initializeBoard() {
        board = new ChessPiece[ChessUtils.BOARD_SIZE][ChessUtils.BOARD_SIZE];

        // 初始化棋盤上的其他棋子
//        board[0][0] = new RookPiece(PieceColor.BLACK);
//        board[0][7] = new RookPiece(PieceColor.BLACK);
//        board[7][0] = new RookPiece(PieceColor.WHITE);
//        board[7][7] = new RookPiece(PieceColor.WHITE);
//
//        board[0][2] = new BishopPiece(PieceColor.BLACK);
//        board[0][5] = new BishopPiece(PieceColor.BLACK);
//        board[7][2] = new BishopPiece(PieceColor.WHITE);
//        board[7][5] = new BishopPiece(PieceColor.WHITE);

//        board[0][1] = new KnightPiece(PieceColor.BLACK);
//        board[0][6] = new KnightPiece(PieceColor.BLACK);
//        board[7][1] = new KnightPiece(PieceColor.WHITE);
//        board[7][6] = new KnightPiece(PieceColor.WHITE);

//        board[0][3] = new QueenPiece(PieceColor.BLACK);
//        board[7][3] = new QueenPiece(PieceColor.WHITE);

//        for (int i = 0; i < ChessUtils.BOARD_SIZE; i++) {
//            board[1][i] = new PawnPiece(PieceColor.BLACK);
//            board[6][i] = new PawnPiece(PieceColor.WHITE);
//        }
//        board[0][4] = new KingPiece(PieceColor.BLACK);
//        board[7][4] = new KingPiece(PieceColor.WHITE);
//        board[7][3] = new RookPiece(PieceColor.BLACK);
    }

    private void initializeGame() {
        turn = PieceColor.WHITE;
    }

    public boolean isKingThreatened(PieceColor currentColor) {

        // 獲取對手的棋子
        PieceColor opponentColor = currentColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        // 查找對方棋子攻擊範圍
        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                ChessPiece piece = board[y][x];
                if (piece != null && piece.color == opponentColor) {
                    List<Position> attackRange = getAttackRangeForPiece(piece, x, y);
                    for (Position pos : attackRange) {
                        // 檢查是否對方的攻擊範圍包含自己的國王
                        if (board[pos.y][pos.x] != null && board[pos.y][pos.x].type == PieceType.KING &&
                                board[pos.y][pos.x].color == currentColor) {
                            return true; // 王被威脅
                        }
                    }
                }
            }
        }
        return false; // 王沒被威脅
    }

    private List<Position> getAttackRangeForPiece(ChessPiece piece, int x, int y) {
        List<Position> attackRange = new ArrayList<>();
        switch (piece.type) {
            case ROOK:
                attackRange = RookPiece.getPossibleMoves(x, y, board);
                break;
            case BISHOP:
                attackRange = BishopPiece.getPossibleMoves(x, y, board);
                break;
            case QUEEN:
                attackRange = QueenPiece.getPossibleMoves(x, y, board);
                break;
            case KNIGHT:
                attackRange = KnightPiece.getPossibleMoves(x, y, board);
                break;
            case PAWN:
                attackRange = PawnPiece.getPossibleMoves(x, y, board, lastMove);
                break;
            case KING:
                attackRange = KingPiece.getPossibleMoves(x, y, board);
                break;
        }
        return attackRange;
    }

    private boolean canRemoveThreat(int fromX, int fromY, int toX, int toY) {
        ChessPiece piece = board[fromY][fromX];
        List<Position> possibleMoves = new ArrayList<>();
        boolean canMove = false;

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
        for (Position pos : possibleMoves) {
            if (pos.x == toX && pos.y == toY) {
                canMove = true;
                break;
            }
        }

        // 如果能够合法移动
        if (canMove) {
            // 模拟棋盘的移动并判断是否能消除威胁
            ChessBoard tempBoard = new ChessBoard();
            tempBoard.board = deepCopyBoard(board);  // 使用深拷贝的方法
            tempBoard.turn = this.turn; // 傳遞當前玩家的回合
            tempBoard.lastMove = this.lastMove; // 確保最後移動位置也同步
            tempBoard.isSimulating = true; // 標記模擬狀態

            // 模擬移動
            tempBoard.board[toY][toX] = tempBoard.board[fromY][fromX];
            tempBoard.board[fromY][fromX] = null;

            // 如果移动后王没有被威胁，返回true
            return !tempBoard.isKingThreatened(tempBoard.turn);
        }

        return false;
    }

    // 深拷贝棋盘的方法
    private ChessPiece[][] deepCopyBoard(ChessPiece[][] originalBoard) {
        ChessPiece[][] copy = new ChessPiece[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            for (int j = 0; j < originalBoard[i].length; j++) {
                if (originalBoard[i][j] != null) {
                    copy[i][j] = originalBoard[i][j].clone();  // 假设棋子类实现了Cloneable接口
                }
            }
        }
        return copy;
    }

    public boolean move(int fromX, int fromY, int toX, int toY) {
        if (!ChessUtils.isValidPos(fromX, fromY) || !ChessUtils.isValidPos(toX, toY)) {
            System.out.println("Invalid position.");
            return false;
        }

        if (board[fromY][fromX] == null) {
            System.out.println("No piece at the source position.");
            return false;
        }

        if (board[fromY][fromX].color != turn) {
            System.out.println("It's not your turn.");
            return false;
        }

        if (isKingThreatened(turn)) {
            // 检查所选的移动是否能消除威胁
            if (!canRemoveThreat(fromX, fromY, toX, toY)) {
                System.out.println("You cannot make this move because your king is still under threat.");
                return false;
            }
        }
        ChessPiece piece = board[fromY][fromX];
        List<Position> possibleMoves = new ArrayList<>();
        boolean canMove = false;

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

        // 检查选择的移动是否有效
        for (Position pos : possibleMoves) {
            if (pos.x == toX && pos.y == toY) {
                canMove = true;
                break;
            }
        }

        // 如果是王车易位
        if (piece.type == PieceType.KING) {
            KingPiece king = (KingPiece) piece;

            // 右王车易位
            if (fromX == 4 && toX == 6 && fromY == toY && !king.hasMoved()) {
                ChessPiece rook = board[toY][7];  // 右侧的车
                if (rook == null) {
                    System.out.println("Error: No rook at the destination position for castling (right).");
                    return false;  // Rook must exist at this position
                }

                if (rook.type == PieceType.ROOK && rook.color == turn && !((RookPiece) rook).hasMoved()) {
                    // 执行王车易位
                    board[toY][5] = board[fromY][7];  // 车移动到新的位置
                    board[toY][6] = board[fromY][4];  // 王移动到新的位置

                    board[fromY][4] = null;  // 原位置为空
                    board[fromY][7] = null;  // 原位置为空

                    // 设置王和车已移动
                    ((RookPiece) board[toY][5]).setHasMoved();
                    ((KingPiece) board[toY][6]).setHasMoved();
                    turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE; // 切换回合
                    return true;
                } else {
                    System.out.println("Error: Rook is either missing, has already moved, or is not the correct color.");
                    return false;
                }
            }

            // 左王车易位
            else if (fromX == 4 && toX == 2 && fromY == toY && !king.hasMoved()) {
                ChessPiece rook = board[toY][0];  // 左侧的车
                if (rook == null) {
                    System.out.println("Error: No rook at the destination position for castling (left).");
                    return false;  // Rook must exist at this position
                }

                if (rook.type == PieceType.ROOK && rook.color == turn && !((RookPiece) rook).hasMoved()) {
                    // 执行王车易位
                    board[toY][3] = board[fromY][0];  // 车移动到新的位置
                    board[toY][2] = board[fromY][4];  // 王移动到新的位置
                    
                    board[fromY][4] = null;  // 原位置为空
                    board[fromY][0] = null;  // 原位置为空

                    // 设置王和车已移动
                    ((RookPiece) board[toY][3]).setHasMoved();
                    ((KingPiece) board[toY][2]).setHasMoved();
                    turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE; // 切换回合
                    return true;
                } else {
                    System.out.println("Error: Rook is either missing, has already moved, or is not the correct color.");
                    return false;
                }
            }
        }

        if (canMove) {
            // 处理合法移动
            if (piece.type == PieceType.PAWN) {
                PawnPiece pawn = (PawnPiece) piece;

                // 检查是否到达对手底线
                if ((pawn.color == PieceColor.WHITE && toY == 0) || (pawn.color == PieceColor.BLACK && toY == ChessUtils.BOARD_SIZE - 1)) {
                    System.out.println("Pawn reached the opponent's back rank. Choose promotion (Q, R, B, N):");
                    Scanner scanner = new Scanner(System.in);
                    String choice = scanner.nextLine().toUpperCase();

                    ChessPiece promotedPiece = null;
                    switch (choice) {
                        case "Q":
                            promotedPiece = new QueenPiece(pawn.color);
                            break;
                        case "R":
                            promotedPiece = new RookPiece(pawn.color);
                            break;
                        case "B":
                            promotedPiece = new BishopPiece(pawn.color);
                            break;
                        case "N":
                            promotedPiece = new KnightPiece(pawn.color);
                            break;
                        default:
                            System.out.println("Invalid choice. Defaulting to Queen.");
                            promotedPiece = new QueenPiece(pawn.color);
                    }

                    // 升变后的新棋子替代兵
                    board[toY][toX] = promotedPiece;
                    board[fromY][fromX] = null; // 移除原位置的兵

                    // 升变后切换回合
                    turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

                    // 返回，切换到对方回合
                    return true;
                }

                // 其他处理兵的规则，如过路兵
                if (pawn.enPassantCapture(fromX, fromY, toX, toY, board, lastMove)) {
                    System.out.println("En Passant captured!");
                    board[fromY][toX] = null; // 删除敌方兵
                } else {
                    board[toY][toX] = board[fromY][fromX];
                }

                board[fromY][fromX] = null;
                pawn.setHasMoved(true);
            } else {
                if (piece.type == PieceType.ROOK) {
                    ((RookPiece) piece).setHasMoved();
                } else if (piece.type == PieceType.KING) {
                    ((KingPiece) piece).setHasMoved();
                }

                board[toY][toX] = board[fromY][fromX];
                board[fromY][fromX] = null;
            }

            if (!isSimulating) {
                lastMove = new Position(toX, toY, false);
                turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE; // 切換回合
                printBoard();
            }
            return true;
        } else {
            System.out.println("Invalid move for this piece.");
            return false;
        }
    }


    private boolean hasLegalMoves(PieceColor currentColor) {
        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                ChessPiece piece = board[y][x];
                if (piece != null && piece.color == currentColor) {
                    List<Position> possibleMoves = getAttackRangeForPiece(piece, x, y);
                    for (Position move : possibleMoves) {
                        ChessBoard tempBoard = new ChessBoard();
                        tempBoard.board = deepCopyBoard(board);
                        tempBoard.turn = currentColor;
                        tempBoard.lastMove = lastMove;
                        tempBoard.isSimulating = true;
                        tempBoard.move(x, y, move.x, move.y);
                        if (!tempBoard.isKingThreatened(currentColor)) {
                            return true; // 找到一個合法移動
                        }
                    }
                }
            }
        }
        return false; // 無任何合法移動
    }

    private boolean isInsufficientMaterial() {
        int whitePieces = 0, blackPieces = 0;
        boolean whiteHasBishopOrKnight = false, blackHasBishopOrKnight = false;

        for (int y = 0; y < ChessUtils.BOARD_SIZE; y++) {
            for (int x = 0; x < ChessUtils.BOARD_SIZE; x++) {
                ChessPiece piece = board[y][x];
                if (piece != null) {
                    if (piece.color == PieceColor.WHITE) {
                        whitePieces++;
                        if (piece.type == PieceType.BISHOP || piece.type == PieceType.KNIGHT) {
                            whiteHasBishopOrKnight = true;
                        }
                    } else {
                        blackPieces++;
                        if (piece.type == PieceType.BISHOP || piece.type == PieceType.KNIGHT) {
                            blackHasBishopOrKnight = true;
                        }
                    }
                }
            }
        }

        // 檢查是否僅剩國王，或國王加輕子
        return (whitePieces == 1 || (whitePieces == 2 && whiteHasBishopOrKnight)) &&
                (blackPieces == 1 || (blackPieces == 2 && blackHasBishopOrKnight));
    }

    public boolean isDraw() {
        // 1. 判斷是否無法合法移動（悶死局面）
        if (!hasLegalMoves(turn) && !isKingThreatened(turn)) {
            System.out.println("Stalemate! The game is a draw.");
            return true;
        }

        // 2. 檢查是否為物理和棋（僅剩國王或國王與輕子）
        if (isInsufficientMaterial()) {
            System.out.println("Insufficient material! The game is a draw.");
            return true;
        }

        // 3. 其他情況（如三次重複局面或 50 步規則）可根據需要實現
        return false;
    }

    public boolean isCheckmate() {
        if (isKingThreatened(turn) && !hasLegalMoves(turn)) {
            System.out.println(turn + " is checkmated! The game is over.");
            return true;
        }
        return false;
    }

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
                    String pieceSymbol = board[y][x].color.toString().charAt(0) + "" + board[y][x].type.toString().charAt(0);
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
