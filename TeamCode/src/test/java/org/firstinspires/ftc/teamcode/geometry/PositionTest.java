package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionTest {

    @Test
    public void distanceToLine() {
        assertEquals(0.0, new Position(0, 0).distance(
                new Line(new Position(0, -1), new Position(0, 1))), E);
        assertEquals(1.0, new Position(0, 0).distance(
                new Line(new Position(1, -1), new Position(1, 1))), E);
        assertEquals(1.0, new Position(0, 0).distance(
                new Line(new Position(-1, -1), new Position(-1, 1))), E);
        assertEquals(Math.sqrt(2) / 2, new Position(0, 0).distance(
                new Line(new Position(1, 0), new Position(0, 1))), E);
    }

}
