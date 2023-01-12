package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertLine;

import org.junit.Test;

public class RectangleTest {

    private static final double E = 1e-6;

    @Test
    public void clip() {
        Rectangle rectangle = new Rectangle(10, 20, 0, 0);
        Line line = new Line(new Position(-1, -1), new Position(11, 11));
        Line clipped = rectangle.clip(line);
        assertLine(new Line(new Position(0, 0), new Position(10, 10)), clipped);
    }

}
