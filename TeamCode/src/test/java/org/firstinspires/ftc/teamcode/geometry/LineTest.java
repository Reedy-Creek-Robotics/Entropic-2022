package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertPosition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTest {

    private static final double E = 1e-5;

    @Test
    public void withX() {
        Line line = new Line(new Position(1, 1), new Position(3, 2));
        assertPosition(new Position(0, 0.5), line.withX(0));
        assertPosition(new Position(4, 2.5), line.withX(4));

        // Horizontal line
        line = new Line(new Position(1, 2), new Position(3, 2));
        assertPosition(new Position(0, 2), line.withX(0));
        assertPosition(new Position(100, 2), line.withX(100));

        // Vertical line
        line = new Line(new Position(1, 1), new Position(1, 3));
        assertPosition(new Position(1, Double.NaN), line.withX(1));
        assertPosition(new Position(2, Double.NaN), line.withX(2));
    }

    @Test
    public void withY() {
        Line line = new Line(new Position(1, 1), new Position(3, 2));
        assertPosition(new Position(0, 0.5), line.withY(0.5));
        assertPosition(new Position(4, 2.5), line.withY(2.5));

        // Vertical line
        line = new Line(new Position(1, 1), new Position(1, 3));
        assertPosition(new Position(1, 0), line.withY(0));
        assertPosition(new Position(1, 100), line.withY(100));

        // Horizontal line
        line = new Line(new Position(1, 2), new Position(3, 2));
        assertPosition(new Position(Double.NaN, 1), line.withY(1));
        assertPosition(new Position(Double.NaN, 2), line.withY(2));
    }

    @Test
    public void getAngleToX() {
        assertEquals(0.0,
                new Line(new Position(0, 0), new Position(1, 0)
                ).getAngleToX(), E);

        assertEquals(30,
                new Line(new Position(0, 0), new Position(Math.sqrt(3), 1)
                ).getAngleToX(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(1, 1)
                ).getAngleToX(), E);

        assertEquals(60,
                new Line(new Position(0, 0), new Position(1, Math.sqrt(3))
                ).getAngleToX(), E);

        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(0, 1)
                ).getAngleToX(), E);

        assertEquals(-60,
                new Line(new Position(0, 0), new Position(-1, Math.sqrt(3))
                ).getAngleToX(), E);

        assertEquals(-45.0,
                new Line(new Position(0, 0), new Position(-1, 1)
                ).getAngleToX(), E);

        assertEquals(-30,
                new Line(new Position(0, 0), new Position(Math.sqrt(3), -1)
                ).getAngleToX(), E);

        assertEquals(0.0,
                new Line(new Position(0, 0), new Position(-1, 0)
                ).getAngleToX(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(-1, -1)
                ).getAngleToX(), E);

        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(0, -1)
                ).getAngleToX(), E);

        assertEquals(-45.0,
                new Line(new Position(0, 0), new Position(1, -1)
                ).getAngleToX(), E);
    }

    @Test
    public void getAngleToY() {
        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(1, 0)
                ).getAngleToY(), E);

        assertEquals(-60,
                new Line(new Position(0, 0), new Position(Math.sqrt(3), 1)
                ).getAngleToY(), E);

        assertEquals(-45.0,
                new Line(new Position(0, 0), new Position(1, 1)
                ).getAngleToY(), E);

        assertEquals(-30,
                new Line(new Position(0, 0), new Position(1, Math.sqrt(3))
                ).getAngleToY(), E);

        assertEquals(0.0,
                new Line(new Position(0, 0), new Position(0, 1)
                ).getAngleToY(), E);

        assertEquals(30,
                new Line(new Position(0, 0), new Position(-1, Math.sqrt(3))
                ).getAngleToY(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(-1, 1)
                ).getAngleToY(), E);

        assertEquals(60,
                new Line(new Position(0, 0), new Position(Math.sqrt(3), -1)
                ).getAngleToY(), E);

        assertEquals(90.0,
                new Line(new Position(0, 0), new Position(-1, 0)
                ).getAngleToY(), E);

        assertEquals(-45.0,
                new Line(new Position(0, 0), new Position(-1, -1)
                ).getAngleToY(), E);

        assertEquals(0.0,
                new Line(new Position(0, 0), new Position(0, -1)
                ).getAngleToY(), E);

        assertEquals(45.0,
                new Line(new Position(0, 0), new Position(1, -1)
                ).getAngleToY(), E);
    }


    @Test
    public void getAngleToLine() {
        Line line = new Line(new Position(0, 0), new Position(1, 1));

        assertEquals(0.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(1, 1))), E);

        assertEquals(0.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(-1, -1))), E);

        assertEquals(90.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(-1, 1))), E);

        assertEquals(45.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(1, 0))), E);

        assertEquals(15.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(Math.sqrt(3), 1))), E);

        assertEquals(-45.0, line.getAngleToLine(
                new Line(new Position(0, 0), new Position(0, -1))), E);
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
        assertPosition(new Position(3, 3), first.intersect(second));
        assertPosition(new Position(3, 3), second.intersect(first));

        // Vertical line
        second = new Line(new Position(1, 0), new Position(1, 3));
        assertPosition(new Position(1, 1), first.intersect(second));

        // Horizontal line
        second = new Line(new Position(0, 2), new Position(3, 2));
        assertPosition(new Position(2, 2), first.intersect(second));

        // Parallel line
        first = new Line(new Position(0, 3), new Position(3, 3));
        assertPosition(new Position(Double.POSITIVE_INFINITY, Double.NaN), first.intersect(second));
    }

}
