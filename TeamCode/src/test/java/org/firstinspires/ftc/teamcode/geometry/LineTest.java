package org.firstinspires.ftc.teamcode.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTest {

    private static final double E = 1e-6;

    @Test
    public void withX() {
        Line line = new Line(new Position(1, 1), new Position(3, 2));
        assertPosEquals(new Position(0, 0.5), line.withX(0));
        assertPosEquals(new Position(4, 2.5), line.withX(4));

        // Horizontal line
        line = new Line(new Position(1, 2), new Position(3, 2));
        assertPosEquals(new Position(0, 2), line.withX(0));
        assertPosEquals(new Position(100, 2), line.withX(100));

        // Vertical line
        line = new Line(new Position(1, 1), new Position(1, 3));
        assertPosEquals(new Position(1, Double.NaN), line.withX(1));
        assertPosEquals(new Position(2, Double.NaN), line.withX(2));
    }

    @Test
    public void withY() {
        Line line = new Line(new Position(1, 1), new Position(3, 2));
        assertPosEquals(new Position(0, 0.5), line.withY(0.5));
        assertPosEquals(new Position(4, 2.5), line.withY(2.5));

        // Vertical line
        line = new Line(new Position(1, 1), new Position(1, 3));
        assertPosEquals(new Position(1, 0), line.withY(0));
        assertPosEquals(new Position(1, 100), line.withY(100));

        // Horizontal line
        line = new Line(new Position(1, 2), new Position(3, 2));
        assertPosEquals(new Position(Double.NaN, 1), line.withY(1));
        assertPosEquals(new Position(Double.NaN, 2), line.withY(2));
    }

    @Test
    public void getAngle() {
        assertEquals(0.0,
                new Line(new Position(0, 0), new Position(1, 0)
                ).getAngle(), E);

        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(0, 1)
                ).getAngle(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(1, 1)
                ).getAngle(), E);

        assertEquals(135.0,
                new Line(new Position(0, 0), new Position(-1, 1)
                ).getAngle(), E);

        assertEquals(180.0,
                new Line(new Position(0, 0), new Position(-1, 0)
                ).getAngle(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(-1, -1)
                ).getAngle(), E);

        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(0, -1)
                ).getAngle(), E);

        assertEquals(135.0,
                new Line(new Position(0, 0), new Position(1, -1)
                ).getAngle(), E);
    }

    @Test
    public void isLeft() {
        assertTrue(new Line(new Position(0, 0), new Position(1, 1)).isLeft(new Position(0, 1)));
        assertFalse(new Line(new Position(0, 0), new Position(1, 1)).isLeft(new Position(1, 0)));
        assertFalse(new Line(new Position(1, 1), new Position(0, 0)).isLeft(new Position(0, 1)));
        assertTrue(new Line(new Position(1, 1), new Position(0, 0)).isLeft(new Position(1, 0)));
    }

    @Test
    public void intersect() {
        Line first = new Line(new Position(1, 1), new Position(5, 5));
        Line second = new Line(new Position(1, 5), new Position(5, 1));
        assertPosEquals(new Position(3, 3), first.intersect(second));
        assertPosEquals(new Position(3, 3), second.intersect(first));

        // Vertical line
        second = new Line(new Position(1, 0), new Position(1, 3));
        assertPosEquals(new Position(1, 1), first.intersect(second));

        // Horizontal line
        second = new Line(new Position(0, 2), new Position(3, 2));
        assertPosEquals(new Position(2, 2), first.intersect(second));

        // Parallel line
        first = new Line(new Position(0, 3), new Position(3, 3));
        assertPosEquals(new Position(Double.POSITIVE_INFINITY, Double.NaN), first.intersect(second));
    }

    private void assertPosEquals(Position expected, Position actual) {
        assertEquals("x", expected.getX(), actual.getX(), E);
        assertEquals("y", expected.getY(), actual.getY(), E);
    }

}
