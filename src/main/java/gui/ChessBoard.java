package gui;

import game.IChessController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoard extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 578;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_X_ORIGIN = 0;
    private static final int FRAME_Y_ORIGIN = 0;

    private IChessController controller;
    private JComponent panel;

    public ChessBoard(IChessController controller) {
        super("西洋棋");

        this.controller = controller;

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        this.setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.initBoard();
    }

    private void initBoard() {
        panel = new JLayeredPane();
        getContentPane().add(panel);

        JPanel board = new JPanel();
        board.setBackground(Color.decode("#654321"));
        board.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        board.setLayout(new GridLayout(9, 9));

        panel.add(board, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
