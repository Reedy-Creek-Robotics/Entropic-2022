package org.firstinspires.ftc.teamcode.components.drive;

import static org.firstinspires.ftc.teamcode.components.drive.DriveModelUtil.*;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.RampUtil;
import org.firstinspires.ftc.teamcode.util.ScalingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MecanumDriveModel implements DriveModel{

    @Override
    public MotorPowers calculateWheelPowerForDrive(
            double drive,
            double strafe,
            double turn,
            double speedFactor
    ) {
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
    public MotorPowers calculateWheelPowerForDriverRelative(
            double drive,
            double strafe,
            double turn,
            Heading heading,
            double speedFactor
    ) {
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

    @Override
    public Vector2 calculatePositionOffsetFromWheelRotations(
            RobotDescriptor robotDescriptor,
            int deltaBackLeft, int deltaBackRight,
            int deltaFrontLeft, int deltaFrontRight,
            Heading heading,
            MotorPowers motorPowers
    ) {
        // Use vectors at 45 degree angles, u and v, to calculate the overall mecanum movement.
        double du = (deltaBackRight + deltaFrontLeft) / 2.0;
        double dv = (deltaBackLeft + deltaFrontRight) / 2.0;

        double length = 1.0;
        Vector2 u = new Vector2(length, length);
        Vector2 v = new Vector2(-length, length);

        Vector2 deltaPositionInTicks = u.multiply(du).add(v.multiply(dv)).multiply(0.5);

        // Calculate the delta position in tiles based on the above tick movement.
        Vector2 deltaPosition = new Vector2(
                ticksToTiles(robotDescriptor, deltaPositionInTicks.getX()),
                ticksToTiles(robotDescriptor, deltaPositionInTicks.getY())
        );

        // Calculate the empirical strafe correction to use for the currently applied motor powers.
        double strafeCorrection = calculateStrafeCorrectionForMotorPower(robotDescriptor, motorPowers);

        // Apply the strafe correction to the lateral movement of the robot, while leaving the
        // forward and backward movement unaffected.
        Vector2 deltaPositionWithStrafeCorrection = new Vector2(
                deltaPosition.getX() * strafeCorrection,
                deltaPosition.getY()
        );

        // Translate the movement from robot space into field space coordinates by rotating by the
        // robot's current heading.
        Vector2 deltaPositionRelativeToField = deltaPositionWithStrafeCorrection
                .rotate(heading.getValue() - 90);

        return deltaPositionRelativeToField;
    }

    @Override
    public MotorPowers calculateWheelPowerForTargetPosition(
            RobotDescriptor robotDescriptor,
            RobotDescriptor.RampingDescriptor rampingTurnDescriptor,
            Position position, Heading heading,
            Vector2 velocity, Position targetPosition,
            Heading targetHeading, double speedFactor
    ) {
        // Mecanum formulas
        // https://seamonsters-2605.github.io/archive/mecanum/

        // front-left, back-right power = sin(angle+1/4π) * magnitude + turn
        // front-right, back-left power = sin(angle−1/4π) * magnitude + turn

        // angle is the desired offset from current heading [-PI/2, PI/2]
        // magnitude is the speed to move [0, 1]
        // turn is a value from [-1, 1]

        // Gets the angle between the current position and the target position
        Vector2 offset = targetPosition.minus(position);

        // The direction the robot wants to move relative to the field
        Heading directionToMove = offset.toHeading();

        // Direction the robot needs to move before zeroing it out relative to the robot
        Heading directionToMoveRelativeToRobot = directionToMove.minus(heading.minus(90));

        // The angle the robot wants to move at relative to itself
        double angle = directionToMoveRelativeToRobot.toRadians();

        Vector2 powerVector = new Vector2(
                Math.sin(angle + Math.PI / 4.0),  // FL, BR
                Math.sin(angle - Math.PI / 4.0)   // FR, BL
        );

        MotorPowers motorPowers = new MotorPowers(
                powerVector.getY(),
                powerVector.getX(),
                powerVector.getX(),
                powerVector.getY()
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

        // Add in turn.  For example, to turn left, give less power to the left wheels and more to the right.
        double turn = RampUtil.calculateRampingTurnFactor(
                rampingTurnDescriptor, robotDescriptor.rampingTurnExponent, heading, targetHeading, speedFactor
        );

        motorPowers = new MotorPowers(
                motorPowers.backLeft - turn,
                motorPowers.backRight + turn,
                motorPowers.frontLeft - turn,
                motorPowers.frontRight + turn
        );

        motorPowers = MotorPowers.fromVectorN(
                motorPowers.toVectorN().clampToMax(1.0)
        );

        return motorPowers;
    }

    private double calculateStrafeCorrectionForMotorPower(
            RobotDescriptor robotDescriptor,
            MotorPowers motorPowers
    ) {
        List<RobotDescriptor.EmpiricalStrafeCorrection> strafeCorrections = robotDescriptor.empiricalStrafeCorrections;
        boolean enabled = robotDescriptor.enableEmpiricalStrafeCorrection;

        if (motorPowers == null || !enabled || strafeCorrections.isEmpty()) {
            // No measurements, or correction is disabled, so don't apply strafe correction.
            return 1.0;

        } else if (strafeCorrections.size() == 1) {
            // Only one measurement, so use that without scaling.
            return strafeCorrections.get(0).strafeCorrection;

        } else {
            // Multiple measurements, use interpolation to scale between them.
            strafeCorrections = new ArrayList<>(strafeCorrections);

            // Determine the average positive motor power.
            double averageMotorPower = (Math.abs(motorPowers.frontLeft) +
                    Math.abs(motorPowers.frontRight) +
                    Math.abs(motorPowers.backLeft) +
                    Math.abs(motorPowers.backRight)) / 4.0;

            // Find the two closest empirical measurements for this motor power.
            Collections.sort(strafeCorrections, new Comparator<RobotDescriptor.EmpiricalStrafeCorrection>() {
                @Override
                public int compare(RobotDescriptor.EmpiricalStrafeCorrection first, RobotDescriptor.EmpiricalStrafeCorrection second) {
                    return Double.compare(
                            Math.abs(first.motorPower - averageMotorPower),
                            Math.abs(second.motorPower - averageMotorPower)
                    );
                }
            });

            RobotDescriptor.EmpiricalStrafeCorrection low = strafeCorrections.get(0);
            RobotDescriptor.EmpiricalStrafeCorrection high = strafeCorrections.get(1);
            if (low.motorPower > high.motorPower) {
                RobotDescriptor.EmpiricalStrafeCorrection swap = low;
                low = high;
                high = swap;
            }

            // Interpolate between the two measured values.
            return ScalingUtil.scaleLinear(
                    averageMotorPower,
                    low.motorPower, high.motorPower,
                    low.strafeCorrection, high.strafeCorrection
            );
        }
    }

}
