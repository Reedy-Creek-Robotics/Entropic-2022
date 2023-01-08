package org.firstinspires.ftc.teamcode.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangleTest {

    private static final double E = 1e-6;

    @Test
    public void clip() {
        Rectangle rectangle = new Rectangle(10, 20, 0, 0);
        Line line = new Line(new Position(-1, -1), new Position(11, 11));
        Line clipped = rectangle.clip(line);
        assertLineEquals(new Line(new Position(0, 0), new Position(10, 10)), clipped);
    }

    private void assertLineEquals(Line expected, Line actual) {
        assertPosEquals(expected.getP1(), actual.getP1());
        assertPosEquals(expected.getP2(), actual.getP2());
    }

    private void assertPosEquals(Position expected, Position actual) {
        assertEquals("x", expected.getX(), actual.getX(), E);
        assertEquals("y", expected.getY(), actual.getY(), E);
    }

}
