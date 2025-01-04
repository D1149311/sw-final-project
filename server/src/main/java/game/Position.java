package game;


import java.util.Objects;  // 如果使用 Objects.hash

/**
 * 儲存棋盤上的格子
 **/
public class Position {
    public int col;
    public int row;
    public boolean eatable;

    /**
     * 初始化Position
     **/
    public Position(final int col,final int row,final boolean eatable) {
        this.col = col;
        this.row = row;
        this.eatable = eatable;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Position
                && this.col == ((Position) obj).col
                && this.row == ((Position) obj).row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
