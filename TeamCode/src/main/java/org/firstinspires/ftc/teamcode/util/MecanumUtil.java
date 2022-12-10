package org.firstinspires.ftc.teamcode.util;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotDescriptor;

public class MecanumUtil {

    /**
     * Converts tiles traveled into number of ticks moved by
     *
     * @param distance how far you want to travel in tiles
     * @return number of ticks to move
     */
    public static int tilesToTicks(RobotDescriptor robotDescriptor, double distance) {
        double wheelSizeInTiles = DistanceUtil.toTiles(robotDescriptor.wheelSizeInMm, DistanceUnit.MM);
        double wheelCircumference = wheelSizeInTiles * Math.PI;
        double wheelRevolutions = distance / wheelCircumference;

        double ticksPerRevolution = robotDescriptor.wheelMotorEncoderTicksPerRevolution;
        return (int) Math.round(wheelRevolutions * ticksPerRevolution);
    }

    /**
     * Converts ticks moved by the motor into the number of tiles traveled.
     *
     * @param ticks the number of ticks that were moved by the motor
     * @return the distance in tiles that the robot moved
     */
    public static double ticksToTiles(RobotDescriptor robotDescriptor, double ticks) {
        double wheelSizeInTiles = DistanceUtil.toTiles(robotDescriptor.wheelSizeInMm, DistanceUnit.MM);
        double wheelCircumference = wheelSizeInTiles * Math.PI;
        double wheelRevolutions = ticks / robotDescriptor.wheelMotorEncoderTicksPerRevolution;

        return wheelRevolutions * wheelCircumference;
    }

    /**
     * Calculates the offset in field position for a mecanum wheel robot, given the ticks rotation for each wheel.
     */
    public static Vector2 calculatePositionOffsetFromWheelRotations(
            RobotDescriptor robotDescriptor,
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
                ticksToTiles(robotDescriptor, deltaPositionInTicks.getX()),
                ticksToTiles(robotDescriptor, deltaPositionInTicks.getY())
        );

        Vector2 deltaPositionRelativeToRobot = deltaPositionInTiles.rotate(-90);

        Vector2 deltaPositionRelativeToField = deltaPositionRelativeToRobot.rotate(heading.getValue());

        return deltaPositionRelativeToField;
    }

    /**
     * Calculates the power to apply to each mecanum wheel in order to progress toward the target position and heading.
     */
    public static MotorPowers calculateWheelPowerForTargetPosition(
            RobotDescriptor robotDescriptor,
            Position position,
            Heading heading,
            Vector2 velocity,
            Position targetPosition,
            Heading targetHeading,
            double speedFactor
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

        Vector2 powerVector = new Vector2(
                Math.sin(angle + Math.PI / 4.0),  // FL, BR
                Math.sin(angle - Math.PI / 4.0)   // FR, BL
        ).withMagnitude(1.0);

        // Calculate how far off we are from the target heading.
        // Positive turn value means turning left, negative means turning right.
        double turn = targetHeading.delta(heading) / 10.0;
        if (turn >= .25) {
            turn = .25;
        } else if (turn <= -.25) {
            turn = -.25;
        }

        // Add in turn.  For example, to turn left, give less power to the left wheels and more to the right.
        MotorPowers motorPowers = new MotorPowers(
                powerVector.getY() - turn,
                powerVector.getX() + turn,
                powerVector.getX() - turn,
                powerVector.getY() + turn
        );

        // Add in power ramping and desired speed by scaling the motor powers to the desired overall max component.
        // In other words, scale so that the motor getting the most power has its absolute value equal
        // to the power and ramping
        double power = RampUtil.calculateRampingFactor(
                robotDescriptor, position, targetPosition, velocity, speedFactor
        );

        motorPowers = MotorPowers.fromVectorN(
                motorPowers.toVectorN().withMaxComponent(power)
        );

        return motorPowers;
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

        public VectorN toVectorN() {
            return new VectorN(
                    backLeft,
                    backRight,
                    frontLeft,
                    frontRight
            );
        }

        public static MotorPowers fromVectorN(VectorN vector) {
            return new MotorPowers(
                    vector.get(0),
                    vector.get(1),
                    vector.get(2),
                    vector.get(3)
            );
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
