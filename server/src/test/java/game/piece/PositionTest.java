package game.piece;

import game.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    public static void assertPosition(List<Position> expected, List<Position> actual) {
        Set<Position> expectedSet = new HashSet<Position>(expected);
        Set<Position> actualSet = new HashSet<Position>(actual);
        assertEquals(expectedSet, actualSet);
        for(int i=0 ; i<expected.size() ; i++) {
            Position e = expected.get(i);
            Position a = actual.get(i);
            assertEquals(e.eatable, a.eatable);
        }
    }
}
