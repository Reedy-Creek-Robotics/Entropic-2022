package org.firstinspires.ftc.teamcode.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.junit.Test;

public class MecanumUtilTest {

    private static final double E = 1e-6;

    private static RobotDescriptor descriptor = new RobotDescriptor();

    @Test
    public void offsetFromWheelDelta_simpleMoveForward() {
        Vector2 offset = MecanumUtil.calculatePositionOffsetFromWheelRotations(
                descriptor,
                10, 10, 10, 10,
                new Heading(90)
        );

        double expectedTiles = MecanumUtil.ticksToTiles(descriptor, 10);

        assertVectorEquals(new Vector2(0.0, expectedTiles), offset);
    }

    @Test
    public void offsetFromWheelDelta_strafeRight() {
        Vector2 offset = MecanumUtil.calculatePositionOffsetFromWheelRotations(
                descriptor,
                -10, 10, 10, -10,
                new Heading(90)
        );

        double expectedTiles = MecanumUtil.ticksToTiles(descriptor, 10);

        assertVectorEquals(new Vector2(expectedTiles, 0.0), offset);
    }

    @Test
    public void offsetFromWheelDelta_move45() {
        Vector2 offset = MecanumUtil.calculatePositionOffsetFromWheelRotations(
                descriptor,
                10, 10, 10, 10,
                new Heading(45)
        );

        double expectedTiles = MecanumUtil.ticksToTiles(descriptor, 10) / Math.sqrt(2);

        assertVectorEquals(new Vector2(expectedTiles, expectedTiles), offset);
    }

    @Test
    public void calculateWheelPowerForTargetPosition_moveForward() {
        MecanumUtil.MotorPowers powers = MecanumUtil.calculateWheelPowerForTargetPosition(
                descriptor,
                new Position(0.5, 0.5), new Heading(90), new Vector2(0, 0),
                new Position(0.5, 1.5), new Heading(90),
                0.5
        );

        // Moving straight ahead, so all motor powers should be positive and equal
        assertTrue(powers.backLeft > 0);
        assertTrue(powers.backRight > 0);
        assertTrue(powers.frontLeft > 0);
        assertTrue(powers.frontRight > 0);

        assertEquals(powers.backLeft, powers.backRight, E);
        assertEquals(powers.backLeft, powers.frontRight, E);
        assertEquals(powers.backLeft, powers.frontLeft, E);
    }

    @Test
    public void calculateWheelPowerForTargetPosition_moveBackward() {
        MecanumUtil.MotorPowers powers = MecanumUtil.calculateWheelPowerForTargetPosition(
                descriptor,
                new Position(0.5, 1.5), new Heading(90), new Vector2(0, 0),
                new Position(0.5, 0.5), new Heading(90),
                0.5
        );

        // Moving straight backward, so all motor powers should be negative and equal
        assertTrue(powers.backLeft < 0);
        assertTrue(powers.backRight < 0);
        assertTrue(powers.frontLeft < 0);
        assertTrue(powers.frontRight < 0);

        assertEquals(powers.backLeft, powers.backRight, E);
        assertEquals(powers.backLeft, powers.frontRight, E);
        assertEquals(powers.backLeft, powers.frontLeft, E);
    }

    @Test
    public void calculateWheelPowerForTargetPosition_strafeRight() {
        MecanumUtil.MotorPowers powers = MecanumUtil.calculateWheelPowerForTargetPosition(
                descriptor,
                new Position(0.5, 0.5), new Heading(90), new Vector2(0, 0),
                new Position(1.5, 0.5), new Heading(90),
                0.5
        );

        // Strafing right, so back left and front right are negative, others are positive
        assertTrue(powers.backLeft < 0);
        assertTrue(powers.frontRight < 0);
        assertTrue(powers.backRight > 0);
        assertTrue(powers.frontLeft > 0);

        // Power should be equal but inverted
        assertEquals(powers.backLeft, powers.frontRight, E);
        assertEquals(powers.backRight, powers.frontLeft, E);
        assertEquals(powers.backLeft, -powers.frontLeft, E);
    }

    private void assertVectorEquals(Vector2 expected, Vector2 actual) {
        assertEquals(expected.getX(), actual.getX(), E);
        assertEquals(expected.getY(), actual.getY(), E);
    }

}
