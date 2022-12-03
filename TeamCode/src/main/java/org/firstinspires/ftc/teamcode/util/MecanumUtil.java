package org.firstinspires.ftc.teamcode.util;

public class MecanumUtil {

    /**
     * Calculates the offset in field position for a mecanum wheel robot, given the ticks rotation for each wheel.
     */
    public static Vector2 offsetFromWheelDelta(
            int deltaBackLeft,
            int deltaBackRight,
            int deltaFrontLeft,
            int deltaFrontRight,
            Heading heading
    ) {
        double du = (deltaBackRight + deltaFrontLeft) / 2.0;
        double dv = (deltaBackLeft + deltaFrontRight) / 2.0;

        double length = 1.0; // Math.sqrt(2);
        Vector2 u = new Vector2(length, length);
        Vector2 v = new Vector2(-length, length);

        Vector2 deltaPositionInTicks = u.multiply(du).add(v.multiply(dv)).multiply(0.5);

        Vector2 deltaPositionInTiles = new Vector2(
                DistanceUtil.ticksToTiles(deltaPositionInTicks.getX()),
                DistanceUtil.ticksToTiles(deltaPositionInTicks.getY())
        );

        Vector2 deltaPositionRelativeToRobot = deltaPositionInTiles.rotate(-90);

        Vector2 deltaPositionRelativeToField = deltaPositionRelativeToRobot.rotate(heading.getValue());

        return deltaPositionRelativeToField;
    }


}
