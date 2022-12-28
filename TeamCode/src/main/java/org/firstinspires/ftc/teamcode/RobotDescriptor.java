package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.opencv.core.Size;

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
     * The resolution of the side web cam, in pixels.
     */
    public Size webCamResolution = new Size(640, 360);

    /**
     * The coordinates in inches from the front right corner of the robot, to the top left pixel in the webcam's vision.
     */
    public Position webCamImageTopLeftCornerCoordinates = new Position(-0.1, 8.3);
    public Position webCamImageTopRightCornerCoordinates = new Position(13.8, 8.2);
    public Position webCamImageBottomLeftCornerCoordinates = new Position(0.5, 0.75);
    public Position webCamImageBottomRightCornerCoordinates = new Position(12.9, 0.6);

    /**
     * The size of the robot in inches, with the x axis being left to right and the y axis being
     * back to front.
     */
    public Size robotDimensionsInInches = new Size(16.5, 17.25);

}
