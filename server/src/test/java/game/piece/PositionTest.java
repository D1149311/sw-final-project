package game.piece;

import org.junit.jupiter.api.Test;
import game.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    public static void assertPosition(List<Position> expected, List<Position> actual) {
//        for (int i = 0; i < expected.size(); i++) {
//            int expect_x = expected.get(i).x;
//            int expect_y = expected.get(i).y;
//            boolean expect_eat = actual.get(i).eatable;
//            int actual_x = actual.get(i).x;
//            int actual_y = actual.get(i).y;
//            boolean actual_eat = actual.get(i).eatable;
//            assertEquals(expect_x, actual_x);
//            assertEquals(expect_y, actual_y);
//            assertEquals(expect_eat, actual_eat);
//        }
        Set<Position> expectedSet = new HashSet<Position>(expected);
        Set<Position> actualSet = new HashSet<Position>(actual);
        assertEquals(expectedSet, actualSet);
    }
}
