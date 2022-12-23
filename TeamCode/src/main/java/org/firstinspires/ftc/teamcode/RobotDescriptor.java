package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.util.Vector2;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The diameter of the robot's wheels in mm.
     */
    public double wheelSizeInMm = 100.0;

    /**
     * The number of encoder ticks that the robot's drive train motors count per full revolution.
     */
    public double wheelMotorEncoderTicksPerRevolution = 537.6;

    /**
     * The speed, in tiles per second, after which ramping up should end.
     */
    public double rampingUpEndSpeed = 1.0; // tiles / sec

    /**
     * For ramping up, the minimum motor power that will be applied to move the robot.
     */
    public double rampingUpMinMotorPower = 0.3;

    /**
     * The distance, in tiles, at which to begin ramping down.
     */
    public double rampingDownBeginDistance = 0.75; // tiles

    /**
     * For ramping down, the minimum motor power that will be applied to move the robot.
     */
    public double rampingDownMinMotorPower = 0.10;

    /**
     * For ramping down, the distance threshold under which no motor power will be applied for movement.
     * Even within this distance threshold, power may still be applied for turning to the target heading.
     */
    public double movementTargetPositionReachedThreshold = 0.01;

    /**
     * The threshold in degrees under which no motor power will be applied for turning.
     */
    public double rotationTargetHeadingReachedThreshold = 2;

    /**
     * For turning, the maximum turn motor power to add.
     */
    public double rampingMaxTurnPower = 1.0;

    /**
     * For turning, the minimum turn motor power to add.
     */
    public double rampingMinTurnPower = 0.05;

    /**
     * For turning, the degrees off the target heading at which the maximum turn power will be added.
     */
    public double rampingMaxTurnDegrees = 45;

    /**
     * For turning, the exponent to use when scaling.
     */
    public double rampingTurnExponent = 2.0;

    /**
     * The height at which the webcam is mounted above the ground.
     */
    public double webCamHeightInInches = 14.25;

    /**
     * The angle in degrees between the webcam and the side of the robot (to account for skew in mounting).
     */
    public double webCamAngleToRightSideOfRobot = 0.0;

    /**
     * The angle in degrees between the webcam and ground.
     *
     * For example, 90 means pointing straight down.  100 means pointing degrees away from the robot.
     */
    public double webCamAngleToGround = 100.0;

    /**
     * The horizontal fiel
     */
    public double webCamFieldOfViewHorizontal = ;  // 30 cm horizontal, 20 cm vertical

    /**
     * The vertical field of view for the webcam, in degrees.
     */
    public double webCamFieldOfViewVertical = ;

    public Vector2 webCamPositionRelativeToRobotFrontRightInInches;

    public Vector2 robotDimensionsInInches;

}
