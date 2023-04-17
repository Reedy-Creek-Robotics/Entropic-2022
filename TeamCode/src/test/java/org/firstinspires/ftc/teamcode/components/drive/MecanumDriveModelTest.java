package org.firstinspires.ftc.teamcode.components.drive;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertVector;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import junit.framework.TestCase;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MecanumDriveModelTest {

    private static RobotDescriptor descriptor = new RobotDescriptor();
    private DriveModel model = new MecanumDriveModel();

    @Test
    public void offsetFromWheelDelta_simpleMoveForward() {
        Vector2 offset = model.calculatePositionOffsetFromWheelRotations(
                descriptor,
                10, 10, 10, 10,
                new Heading(90), null
        );

        double expectedTiles = DriveModelUtil.ticksToTiles(descriptor, 10);

        assertVector(new Vector2(0.0, expectedTiles), offset);
    }

    @Test
    public void offsetFromWheelDelta_strafeRight() {
        Vector2 offset = model.calculatePositionOffsetFromWheelRotations(
                descriptor,
                -10, 10, 10, -10,
                new Heading(90), null
        );

        double expectedTiles = DriveModelUtil.ticksToTiles(descriptor, 10);

        assertVector(new Vector2(expectedTiles, 0.0), offset);
    }

    @Test
    public void offsetFromWheelDelta_strafeRight_withStrafeCorrection() {
        double strafeCorrection = 0.95;
        descriptor.empiricalStrafeCorrections = Arrays.asList(
                new RobotDescriptor.EmpiricalStrafeCorrection(0.5, strafeCorrection)
        );

        Vector2 offset = model.calculatePositionOffsetFromWheelRotations(
                descriptor,
                -10, 10, 10, -10,
                new Heading(90),
                new MotorPowers(-0.5, 0.5, 0.5, -0.5)
        );

        double expectedTiles = DriveModelUtil.ticksToTiles(descriptor, 10) * strafeCorrection;

        assertVector(new Vector2(expectedTiles, 0.0), offset);
    }

    @Test
    public void offsetFromWheelDelta_strafeRight_withStrafeCorrectionInterpolated() {
        descriptor.empiricalStrafeCorrections = Arrays.asList(
                new RobotDescriptor.EmpiricalStrafeCorrection(0.2, 0.99),
                new RobotDescriptor.EmpiricalStrafeCorrection(0.5, 0.9),
                new RobotDescriptor.EmpiricalStrafeCorrection(0.75, 0.85),
                new RobotDescriptor.EmpiricalStrafeCorrection(0.9, 0.84)
        );

        // Use motor power of 0.6, which is 2/5 of the way between 0.5 and 0.75, therefore the
        // applied strafe correction should be 2/5 of the way between 0.9 and 0.85
        double motorPower = 0.6;
        double strafeCorrection = 0.88;

        Vector2 offset = model.calculatePositionOffsetFromWheelRotations(
                descriptor,
                -10, 10, 10, -10,
                new Heading(90),
                new MotorPowers(-motorPower, motorPower, motorPower, -motorPower)
        );

        double expectedTiles = DriveModelUtil.ticksToTiles(descriptor, 10) * strafeCorrection;

        assertVector(new Vector2(expectedTiles, 0.0), offset);
    }

    @Test
    public void offsetFromWheelDelta_move45() {
        Vector2 offset = model.calculatePositionOffsetFromWheelRotations(
                descriptor,
                10, 10, 10, 10,
                new Heading(45), null
        );

        double expectedTiles = DriveModelUtil.ticksToTiles(descriptor, 10) / Math.sqrt(2);

        assertVector(new Vector2(expectedTiles, expectedTiles), offset);
    }

    @Test
    public void calculateWheelPowerForTargetPosition_moveForward() {
        MotorPowers powers = model.calculateWheelPowerForTargetPosition(
                descriptor, descriptor.turnRampingDescriptor,
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
        MotorPowers powers = model.calculateWheelPowerForTargetPosition(
                descriptor, descriptor.turnRampingDescriptor,
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
        MotorPowers powers = model.calculateWheelPowerForTargetPosition(
                descriptor, descriptor.turnRampingDescriptor,
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

}