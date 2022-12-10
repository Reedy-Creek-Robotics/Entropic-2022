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

        /*
        // Speed in tiles per second is the magnitude of the veloctiy vector.
        double speed = velocity.magnitude();

        // Calculate the distance to the destination in tiles.
        double distance = targetPosition.offset(position).magnitude();

        // Ramping up - scale down power if the current speed is low.
        double speedToPowerRatio = speed / power;
        if (speedToPowerRatio > robotDescriptor.rampingUpMaximumSpeedToMotorPowerRatio) {
            power = speed / robotDescriptor.rampingUpMaximumSpeedToMotorPowerRatio;
        }

        // Ramping down - scale down power as we approach the destination.
        double powerToDistanceRemainingRatio = power / distance;
        if (powerToDistanceRemainingRatio < robotDescriptor.rampingDownMaximumMotorPowerToDistanceRemainingRatio) {
            power = power / robotDescriptor.rampingDownMaximumMotorPowerToDistanceRemainingRatio;
        }
        */

        // Now include the overall speed factor.
        power = power * speedFactor;

        // Never go below the minimum power required to move the robot.
        power = Math.max(power, robotDescriptor.rampingMinMotorPower);

        return power;
    }

}
