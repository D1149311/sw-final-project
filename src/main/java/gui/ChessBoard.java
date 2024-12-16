package gui;

import game.IChessController;
import game.Piece;
import game.StandaloneController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoard extends JFrame implements ActionListener {
    private static final int PIECE_WIDTH = 70;
    private static final int PIECE_HEIGHT = 70;
    private static final int BOARD_COL = 8;
    private static final int BOARD_ROW = 8;
    private static final int BOARD_BORDER = 20;
    private static final int BOARD_WIDTH = BOARD_COL * PIECE_WIDTH;
    private static final int BOARD_HEIGHT = BOARD_ROW * PIECE_HEIGHT;
    private static final int FRAME_WIDTH = BOARD_WIDTH + 14 + 2 * BOARD_BORDER;
    private static final int FRAME_HEIGHT = BOARD_HEIGHT + 38 + 2 * BOARD_BORDER;
    private static final int FRAME_X_ORIGIN = 0;
    private static final int FRAME_Y_ORIGIN = 0;

    private IChessController controller;
    private JComponent panel;
    private ChessPieceButton board[][];

    public ChessBoard(IChessController controller) {
        super("西洋棋");

        this.controller = controller;

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        this.setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.initBoard();
        this.initController();
    }

    private void initBoard() {
        board = new ChessPieceButton[BOARD_ROW][BOARD_COL];

        panel = new JLayeredPane();
        getContentPane().add(panel);

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.decode("#654321"));
        boardPanel.setBounds(BOARD_BORDER, BOARD_BORDER, BOARD_WIDTH, BOARD_HEIGHT);
        boardPanel.setLayout(new GridLayout(BOARD_ROW, BOARD_COL));

        for (int y = 0; y < BOARD_ROW; y++) {
            for (int x = 0; x < BOARD_COL; x++) {
                ChessPieceButton button = new ChessPieceButton(x, y);
                button.addActionListener(this);
                button.setChess(null);
                boardPanel.add(button);

                board[y][x] = button;
            }
        }

        panel.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
    }

    public void initController() {
        this.controller.start(this::setChess);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void setChess(int x, int y, Piece piece) {
        board[y][x].setChess(piece);
    }

    public static void main(String[] args) {
        IChessController controller = new StandaloneController();
        new ChessBoard(controller);
    }
}
