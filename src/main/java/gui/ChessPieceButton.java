package gui;

import game.Piece;
import game.PieceColor;
import game.PieceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChessPieceButton extends JButton {
    public final int x, y;
    private BufferedImage image;
    private Piece piece;

    public ChessPieceButton(int x, int y) {
        this.x = x;
        this.y = y;

        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawBoard(g);
        drawChess(g);
    }

    private void drawBoard(Graphics g) {
        Rectangle btn = getBounds();

        if ((this.x + this.y) % 2 == 0) {
            g.setColor(Color.decode("#8B4513"));
        } else {
            g.setColor(Color.decode("#FFFFF0"));
        }

        g.fillRect(0, 0, btn.width, btn.height);

        g.setColor(Color.decode("#8B4513"));
        g.drawRect(0, 0, btn.width, btn.height);
    }

    private void drawChess(Graphics g) {
        Rectangle btn = getBounds();

        if (this.image != null) {
            g.drawImage(this.image, 0, 0, btn.width, btn.height, this); // 繪製圖片到 (0, 0) 位置
        }
    }

    public void setChess(Piece piece) {
        this.piece = piece;

        try {
            if (this.piece == null) {
                this.image = null;
            } else {
                String color = piece.getColor() == PieceColor.WHITE ? "w" : "b";
                String type = "";
                if (piece.getType() == PieceType.KING) {
                    type = "k";
                } else if (piece.getType() == PieceType.BISHOP) {
                    type = "b";
                } else if (piece.getType() == PieceType.PAWN) {
                    type = "p";
                } else if (piece.getType() == PieceType.KNIGHT) {
                    type = "n";
                } else if (piece.getType() == PieceType.QUEEN) {
                    type = "q";
                } else if (piece.getType() == PieceType.ROOK) {
                    type = "r";
                }

                this.image = ImageIO.read(new File(String.format("src/main/resources/%s%s.png", color, type))); // 替換成你的 PNG 文件路徑
                revalidate();
                repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
