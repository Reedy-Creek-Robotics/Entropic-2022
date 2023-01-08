package org.firstinspires.ftc.teamcode.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ViewportTest {

    private static final double E = 1e-6;

    @Test
    public void convertExternalToView_unitSquare() {
        Viewport viewport = new Viewport(
                640, 360,
                new Position(0, 1), new Position(1, 1),
                new Position(0, 0), new Position(1, 0)
        );

        assertPosEquals(new Position(0.5, 0.5), viewport.convertViewToExternal(new Position(320, 180)));
        assertPosEquals(new Position(0.25, 0.75), viewport.convertViewToExternal(new Position(160, 90)));
        assertPosEquals(new Position(-0.5, 1.5), viewport.convertViewToExternal(new Position(-320, -180)));
    }

    @Test
    public void convertExternalToView_keystoned() {
        // Keystoned viewport from roughly (1,1) to (15, 10)
        Position topLeft = new Position(1.5, 9.5), topRight = new Position(15.5, 10.5);
        Position bottomLeft = new Position(0.5, 1.5), bottomRight = new Position(14.5, 0.5);
        Viewport viewport = new Viewport(
                640, 360,
                topLeft, topRight,
                bottomLeft, bottomRight
        );

        assertPosEquals(bottomLeft, viewport.convertViewToExternal(new Position(0, 360)));
        assertPosEquals(bottomRight, viewport.convertViewToExternal(new Position(640, 360)));
        assertPosEquals(topLeft, viewport.convertViewToExternal(new Position(0, 0)));
        assertPosEquals(topRight, viewport.convertViewToExternal(new Position(640, 0)));

        Position center = average(topLeft, topRight, bottomLeft, bottomRight);
        assertPosEquals(center, viewport.convertViewToExternal(new Position(320, 180)));
    }

    @Test
    public void convertExternalToView_partialView() {
        // 20x10 inch field of view, but with known coordinates inside view window.
        Position topLeft = new Position(2, 9), topRight = new Position(18, 9);
        Position bottomLeft = new Position(2, 1), bottomRight = new Position(18, 1);
        Viewport viewport = new Viewport(
                new Rectangle(10, 180, 90, 20),
                topLeft, topRight,
                bottomLeft, bottomRight
        );

        assertPosEquals(bottomLeft, viewport.convertViewToExternal(new Position(20, 90)));
        assertPosEquals(bottomRight, viewport.convertViewToExternal(new Position(180, 90)));
        assertPosEquals(topLeft, viewport.convertViewToExternal(new Position(20, 10)));
        assertPosEquals(topRight, viewport.convertViewToExternal(new Position(180, 10)));
        assertPosEquals(new Position(10, 5), viewport.convertViewToExternal(new Position(100, 50)));

        assertPosEquals(new Position(0, 0), viewport.convertViewToExternal(new Position(0, 100)));
        assertPosEquals(new Position(0, 10), viewport.convertViewToExternal(new Position(0, 0)));
        assertPosEquals(new Position(20, 10), viewport.convertViewToExternal(new Position(200, 0)));
        assertPosEquals(new Position(20, 0), viewport.convertViewToExternal(new Position(200, 100)));
    }

    private Position average(Position... positions) {
        double x = 0.0, y = 0.0;
        for (Position position : positions) {
            x += position.getX();
            y += position.getY();
        }
        return new Position(x / positions.length, y / positions.length);
    }

    private void assertPosEquals(Position expected, Position actual) {
        assertEquals("x", expected.getX(), actual.getX(), E);
        assertEquals("y", expected.getY(), actual.getY(), E);
    }

}
