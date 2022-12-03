package org.firstinspires.ftc.teamcode.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MecanumUtilTest {

    private static final double E = 1e-6;

    @Test
    public void offsetFromWheelDelta_simpleMoveForward() {
        Vector2 offset = MecanumUtil.offsetFromWheelDelta(
                10, 10, 10, 10,
                new Heading(90)
        );

        double expectedTiles = DistanceUtil.ticksToTiles(10);

        assertVectorEquals(new Vector2(0.0, expectedTiles), offset);
    }

    @Test
    public void offsetFromWheelDelta_strafeRight() {
        Vector2 offset = MecanumUtil.offsetFromWheelDelta(
                -10, 10, 10, -10,
                new Heading(90)
        );

        double expectedTiles = DistanceUtil.ticksToTiles(10);

        assertVectorEquals(new Vector2(expectedTiles, 0.0), offset);
    }

    @Test
    public void offsetFromWheelDelta_move45() {
        Vector2 offset = MecanumUtil.offsetFromWheelDelta(
                10, 10, 10, 10,
                new Heading(45)
        );

        double expectedTiles = DistanceUtil.ticksToTiles(10) / Math.sqrt(2);

        assertVectorEquals(new Vector2(expectedTiles, expectedTiles), offset);
    }


    private void assertVectorEquals(Vector2 expected, Vector2 actual) {
        assertEquals(expected.getX(), actual.getX(), E);
        assertEquals(expected.getY(), actual.getY(), E);
    }

}