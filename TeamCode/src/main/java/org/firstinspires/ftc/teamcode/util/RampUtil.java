package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.TelemetryHolder.telemetry;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public class RampUtil {

    /**
     * Returns a scalar value from range (0, 1) that indicates how the power applied to motors
     * should be scaled to account for ramping up and down.
     */
    public static double calculateRampingFactor(
            RobotDescriptor robotDescriptor,
            Position position, Position targetPosition,
            Vector2 velocity,
            double speedFactor
    ) {

        // Start with full power
        double power = 1.0;

        // Speed in tiles per second is the magnitude of the veloctiy vector.
        double speed = velocity.magnitude();

        // Calculate the distance to the destination in tiles.
        double distance = targetPosition.minus(position).magnitude();

        double rampDownDistance = robotDescriptor.rampingDownBeginDistance * speedFactor;
        double rampUpEndSpeed = robotDescriptor.rampingUpEndSpeed;

        if (distance <= robotDescriptor.movementTargetPositionReachedThreshold) {
            // Target reached - don't try to move
            power = 0;

        } else if (distance <= rampDownDistance) {
            // Ramping down - close to target destination
            double scale = distance / rampDownDistance;
            power *= scale;
            power *= speedFactor;

            power = Math.max(power, robotDescriptor.rampingDownMinMotorPower);

        } else if (speed < rampUpEndSpeed) {
            // Ramping up - initial slow speed
            double scale = speed / rampUpEndSpeed;
            power *= scale;
            power *= speedFactor;

            power = Math.max(power, robotDescriptor.rampingUpMinMotorPower);

        } else {
            // At speed
            power *= speedFactor;
        }

        if (telemetry != null) {
            telemetry.addData("Ramping Power", power);
        }

        return power;
    }

    /**
     * Calculates the turning factor to add based on the given heading and target heading.
     * <p>
     * A positive return value means turning left, negative turning right.
     */
    public static double calculateRampingTurnFactor(
            RobotDescriptor.RampingDescriptor rampingDescriptor,
            double rampingTurnExponent, Heading heading,
            Heading targetHeading, double speedFactor
    ) {
        // Calculate how far off we are from the target heading.
        double delta = targetHeading.delta(heading);

        if (Math.abs(delta) < rampingDescriptor.distanceUntilTargetReached) {
            return 0.0;
        } else {
            // Scale geometrically.
            double exponent = rampingTurnExponent;
            double rampingMaxTurnDegrees = rampingDescriptor.distanceStartRamping * speedFactor;
            double xVal = Math.abs(delta) / rampingMaxTurnDegrees;
            double yVal = Math.pow(xVal, exponent) * rampingDescriptor.maxPower;

            double turnFactor = Math.min(yVal, rampingDescriptor.maxPower);

            turnFactor *= speedFactor;

            turnFactor = Math.max(turnFactor, rampingDescriptor.minPower);

            // Add back in direction.
            turnFactor *= Math.signum(delta);

            if (telemetry != null) {
                telemetry.addData("Turn factor", turnFactor);
            }

            return turnFactor;
        }
    }

}
