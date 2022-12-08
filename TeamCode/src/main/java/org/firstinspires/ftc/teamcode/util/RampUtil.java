package org.firstinspires.ftc.teamcode.util;

public class RampUtil {

    /**
     * Returns a scalar value from range (0, 1) that indicates how the power applied to motors
     * should be scaled to account for ramping up and down.
     */
    public static double calculateRampingFactor(Position position, Position targetPosition, Vector2 velocity, double speedFactor) {

        // Start with full power
        double power = 1.0;

        // Speed in tiles per second is the magnitude of the veloctiy vector.
        double speed = velocity.magnitude();

        // Calculate the distance to the destination in tiles.
        double distance = targetPosition.offset(position).magnitude();

        // Ramping up - scale down power if the current speed is low.

        // References
        // https://www.physicsforums.com/threads/how-much-of-torque-do-you-need-to-start-wheels-slipping.681324/
        // https://www.engineeringtoolbox.com/tractive-effort-d_1783.html
        // https://calculator.academy/tractive-force-calculator/

        // From physics, the wheels will slip if the tractive force applied by the wheels is
        // greater than the force of friction between the wheels and the ground.

        // The force of friction is calculated as:
        //   Normal force * coefficient of friction

        // The tractive force applied by the wheel is:
        //   ???

        // todo: implement the math formula.  For now, just scale by some factor according to velocity

        // Ramping down - scale down power as we approach the destination.

        // todo: scale down according to distance remaining

        return power;
    }

}
