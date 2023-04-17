package org.firstinspires.ftc.teamcode.components.drive;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public interface DriveModel {

    /**
     * Calculates the power to apply to each wheel for bot relative movement.
     */
    MotorPowers calculateWheelPowerForDrive(
            double drive,
            double strafe,
            double turn,
            double speedFactor
    );

    /**
     * Calculates the power to apply to each wheel for driver relative movement.
     */
    MotorPowers calculateWheelPowerForDriverRelative(
            double drive,
            double strafe,
            double turn,
            Heading heading,
            double speedFactor
    );

    /**
     * Calculates the offset in field position for a wheel robot, given the ticks rotation for each wheel.
     */
    Vector2 calculatePositionOffsetFromWheelRotations(
            RobotDescriptor robotDescriptor,
            int deltaBackLeft,
            int deltaBackRight,
            int deltaFrontLeft,
            int deltaFrontRight,
            Heading heading,
            MotorPowers motorPowers
    );

    /**
     * Calculates the power to apply to each wheel in order to progress toward the target position and heading.
     */
    MotorPowers calculateWheelPowerForTargetPosition(
            RobotDescriptor robotDescriptor,
            RobotDescriptor.RampingDescriptor rampingTurnDescriptor,
            Position position,
            Heading heading,
            Vector2 velocity,
            Position targetPosition,
            Heading targetHeading,
            double speedFactor
    );

}