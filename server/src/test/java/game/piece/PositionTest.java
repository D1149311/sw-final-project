package game.piece;

import game.Position;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    public static void assertPosition(List<Position> expected, List<Position> actual) {
        List<Position> expectedCopy = new ArrayList<>(expected.stream().toList());
        List<Position> actualCopy = new ArrayList<>(actual.stream().toList());

        assertEquals(expectedCopy.size(), actualCopy.size());

        expectedCopy.sort(Comparator.comparingInt(p -> ((Position)p).row)
                .thenComparingInt(p -> ((Position)p).col));

        actualCopy.sort(Comparator.comparingInt(p -> ((Position)p).row)
                .thenComparingInt(p -> ((Position)p).col));

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expectedCopy.get(i).col, actualCopy.get(i).col);
            assertEquals(expectedCopy.get(i).row, actualCopy.get(i).row);
            assertEquals(expectedCopy.get(i).eatable, actualCopy.get(i).eatable);
        }
    }

    @Test
    void test() {
        List<Position> expected = new ArrayList<>();
        expected.add(new Position(0, 3, false));
        expected.add(new Position(0, 2, true));

        List<Position> actual = new ArrayList<>();
        actual.add(new Position(0, 2, true));
        actual.add(new Position(0, 3, false));

        assertPosition(expected, actual);
    }

    @Test
    void test1() {
        assertEquals(new Position(0, 3, true), new Position(0, 3, false));
        assertNotEquals(new Position(0, 3, true), null);
        assertNotEquals(new Position(1, 3, true), new Position(0, 3, false));
        assertNotEquals(new Position(0, 2, true), new Position(0, 3, false));
    }

    @Test
    void test2() {
        (new Position(0, 3, true)).hashCode();
    }
}
