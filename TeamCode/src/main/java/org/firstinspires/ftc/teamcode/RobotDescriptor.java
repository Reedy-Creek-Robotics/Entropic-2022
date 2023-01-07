package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.opencv.core.Size;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The diameter of the robot's wheels in mm.
     */
    public double wheelSizeInMm = 96;

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
     * The exposure duration for the webcam in milliseconds.
     */
    public long webCamExposureMs = 25;

    /**
     * The coordinates in inches from the front right corner of the robot, to the top left pixel in the webcam's vision.
     */
    public Position webCamImageTopLeftCornerCoordinates = new Position(0.892, 10.370);
    public Position webCamImageTopRightCornerCoordinates = new Position(18.069, 10.385);
    public Position webCamImageBottomLeftCornerCoordinates = new Position(1.971, 0.899);
    public Position webCamImageBottomRightCornerCoordinates = new Position(16.12, 0.686);

    /**
     * The size of the robot in inches, with the x axis being left to right and the y axis being
     * back to front.
     */
    public Size robotDimensionsInInches = new Size(15.5, 15.5);

}
