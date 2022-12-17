package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.RobotDescriptor;

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
        double distance = targetPosition.offset(position).magnitude();

        double rampDownDistance = robotDescriptor.rampingDownBeginDistance * speedFactor;
        double rampUpEndSpeed = robotDescriptor.rampingUpEndSpeed;

        if (distance <= rampDownDistance) {
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

        return power;
    }

}
