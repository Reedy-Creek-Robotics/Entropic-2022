package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.TelemetryUtil.telemetry;

import android.annotation.SuppressLint;

public class MecanumUtil {

    /**
     * Calculates the offset in field position for a mecanum wheel robot, given the ticks rotation for each wheel.
     */
    public static Vector2 calculatePositionOffsetFromWheelRotations(
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

    /**
     * Calculates the power to apply to each mecanum wheel in order to progress toward the target position and heading.
     */
    public static MotorPowers calculateWheelPowerForTargetPosition(
            Position position,
            Heading heading,
            Position targetPosition,
            Heading targetHeading,
            double speed
    ) {
        // Mecanum formulas
        // https://seamonsters-2605.github.io/archive/mecanum/

        // front-left, back-right power = sin(angle+1/4π) * magnitude + turn
        // front-right, back-left power = sin(angle−1/4π) * magnitude + turn

        // angle is the desired offset from current heading [-PI/2, PI/2]
        // magnitude is the speed to move [0, 1]
        // turn is a value from [-1, 1]

        // Gets the angle between the current position and the target position
        Vector2 offset = targetPosition.offset(position);

        // The direction the robot wants to move relative to the field
        Heading directionToMove = offset.toHeading();

        // Direction the robot needs to move before zeroing it out relative to the robot
        Heading directionToMoveRelativeToRobot = directionToMove.minus(heading.minus(90));

        // The angle the robot wants to move at relative to itself
        double angle = directionToMoveRelativeToRobot.toRadians();

        double power = speed; // todo: add in some kind of ramping from getPowerCurve...

        // Calculate how far off we are from the target heading.
        double turn = targetHeading.delta(heading) / 50.0;

        if (telemetry != null) {
            telemetry.addData("Offset to Move", offset);
            telemetry.addData("Angle", directionToMoveRelativeToRobot);
            telemetry.addData("Sin FLBR", Math.sin(angle + Math.PI / 4.0));
            telemetry.addData("Sin FRBL", Math.sin(angle - Math.PI / 4.0));
        }

        Vector2 powerVector = new Vector2(
                Math.sin(angle + Math.PI / 4.0),  // FL, BR
                Math.sin(angle - Math.PI / 4.0)   // FR, BL
        );

        // Add in turn, but keep the overall magnitude the same
        //Vector2 powerVectorWithTurn = powerVector.add(new Vector2(turn, turn));
        //Vector2 powerVectorWithTurnAndMagnitude = powerVectorWithTurn.withMagnitude(powerVector.magnitude());

        // Add in power ramping and desired speed
        //Vector2 finalPower = powerVectorWithTurnAndMagnitude.multiply(power);
        Vector2 finalPower = powerVector.multiply(power);

        double powerFLBR = finalPower.getX();
        double powerFRBL = finalPower.getY();

        return new MotorPowers(
                powerFRBL,
                powerFLBR,
                powerFLBR,
                powerFRBL
        );
    }

    public static class MotorPowers {
        public double backLeft;
        public double backRight;
        public double frontLeft;
        public double frontRight;

        public MotorPowers(double backLeft, double backRight, double frontLeft, double frontRight) {
            this.backLeft = backLeft;
            this.backRight = backRight;
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
        }

        @SuppressLint("DefaultLocale")
        public String toString() {
            return String.format(
                    "BL %.2f, BR %.2f, FL %.2f, FR %.2f",
                    backLeft, backRight, frontLeft, frontRight
            );
        }
    }

}
