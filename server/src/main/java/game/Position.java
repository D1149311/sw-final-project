package game;

import java.util.Objects;


public class Position {
    public int x;
    public int y;
    public boolean eatable;

    public Position(int x, int y, boolean eatable) {
        this.x = x;
        this.y = y;
        this.eatable = eatable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
