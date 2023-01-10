package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.opencv.core.Size;

import java.util.Arrays;
import java.util.List;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The size of the robot in inches, with the x axis being left to right and the y axis being
     * back to front.
     */
    public Size robotDimensionsInInches = new Size(15.5, 15.5);

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
     * Empirically measured strafe correction values, for more accurate encoder position tracking.
     * Note that these should be measured after calibrating ramping values above.
     */
    public List<EmpiricalStrafeCorrection> empiricalStrafeCorrections = Arrays.asList(
            new EmpiricalStrafeCorrection(0.0, 0.865),
            new EmpiricalStrafeCorrection(0.3, 0.865),
            new EmpiricalStrafeCorrection(0.5, 0.865),
            new EmpiricalStrafeCorrection(0.7, 0.865),
            new EmpiricalStrafeCorrection(0.8, 0.855),
            new EmpiricalStrafeCorrection(1.0, 0.845)
    );

    /**
     * Indicates whether empirical strafe correction using the above measurements should be enabled.
     */
    public boolean enableEmpiricalStrafeCorrection = true;

    /**
     * The resolution of the side web cam, in pixels.
     */
    public Size webCamResolution = new Size(640, 360);

    /**
     * The exposure duration for the webcam in milliseconds.
     */
    public long webCamExposureMs = 15;

    /**
     * The coordinates in inches from the front right corner of the robot, to the top left pixel in the webcam's vision.
     */
    public Position webCamImageTopLeftCornerCoordinates = new Position(0.892, 10.370);
    public Position webCamImageTopRightCornerCoordinates = new Position(18.069, 10.385);
    public Position webCamImageBottomLeftCornerCoordinates = new Position(1.971, 0.899);
    public Position webCamImageBottomRightCornerCoordinates = new Position(16.12, 0.686);

    /**
     * An empirically measured strafe correction value for a given motor power.
     *
     * Using encoder ticks to track position will lead to inaccuracy for lateral movement due to
     * wheel slippage inherent in mecanum wheels.  This slippage tends to be higher with higher
     * motor power.  By measuring the amount of slippage at various motor powers we can correct
     * for this and get better position tracking.
     */
    public static class EmpiricalStrafeCorrection {

        /**
         * The motor power at which the empirical strafe correction was measured.
         */
        public double motorPower;

        /**
         * The strafe correction amount - any lateral movement indicated by encoder ticks will be
         * multipied by this fraction to account for wheel slippage while strafing.  For example,
         * if the encoders indicate strafing movement of 1 tile, a value of 0.95 will result in
         * the robot's position only being updated by 0.95 tiles.
         */
        public double strafeCorrection;

        public EmpiricalStrafeCorrection(double motorPower, double strafeCorrection) {
            this.motorPower = motorPower;
            this.strafeCorrection = strafeCorrection;
        }
    }

}
