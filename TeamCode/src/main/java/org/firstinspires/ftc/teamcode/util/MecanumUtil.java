package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public class MecanumUtil implements DriveUtil {

    @Override
    public MotorPowers calculateWheelPowerForDrive(double drive, double strafe, double turn, double speedFactor) {
        MotorPowers motorPowers = new MotorPowers(
                drive - turn + strafe,
                drive + turn - strafe,
                drive - turn - strafe,
                drive + turn + strafe
        );

        motorPowers = MotorPowers.fromVectorN(
                motorPowers.toVectorN()
                        .multiply(speedFactor)
                        .clampToMax(1.0)
        );

        return motorPowers;
    }

    @Override
    public MotorPowers calculateWheelPowerForDriverRelative(double drive, double strafe, double turn, Heading heading, double speedFactor) {

        // Mecanum formulas
        // https://seamonsters-2605.github.io/archive/mecanum/

        // front-left, back-right power = sin(angle+1/4π) * magnitude + turn
        // front-right, back-left power = sin(angle−1/4π) * magnitude + turn

        // angle is the desired offset from current heading [-PI/2, PI/2]
        // magnitude is the speed to move [0, 1]
        // turn is a value from [-1, 1]

        Vector2 joyStickPosition = new Vector2(drive, strafe);

        // The direction the robot wants to move relative to the field
        Heading directionToMove = joyStickPosition.toHeading();

        // Direction the robot needs to move before zeroing it out relative to the robot
        Heading directionToMoveRelativeToRobot = heading.minus(directionToMove);

        // The angle the robot wants to move at relative to itself
        double angle = directionToMoveRelativeToRobot.toRadians();

        Vector2 powerVector = new Vector2(
                Math.sin(angle - Math.PI / 4.0),   // FR, BL
                Math.sin(angle + Math.PI / 4.0)  // FL, BR
        ).withMagnitude(joyStickPosition.magnitude());

        // Add in turn.  For example, to turn left, give less power to the left wheels and more to the right.
        MotorPowers motorPowers = new MotorPowers(
                powerVector.getY() - turn,
                powerVector.getX() + turn,
                powerVector.getX() - turn,
                powerVector.getY() + turn
        );

        motorPowers = MotorPowers.fromVectorN(
                motorPowers.toVectorN()
                        .multiply(speedFactor)
                        .clampToMax(1.0)
        );

        return motorPowers;
    }
}
