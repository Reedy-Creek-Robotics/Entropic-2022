package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamAnchorPoint.anchor;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
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

    public RampingDescriptor turnRampingDescriptor = new RampingDescriptor(
            2, 45, 1, .05
    );

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
    //public double rotationTargetHeadingReachedThreshold = 2;

    /**
     * For turning, the maximum turn motor power to add.
     */
    //public double rampingMaxTurnPower = 1.0;

    /**
     * For turning, the minimum turn motor power to add.
     */
    //public double rampingMinTurnPower = 0.05;

    /**
     * For turning, the degrees off the target heading at which the maximum turn power will be added.
     */
    //public double rampingMaxTurnDegrees = 45;

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
     * Empirically measured pole distance values based on the average width in pixels.
     * <p>
     * Distance is the distance from the front of the robot in inches.
     */
    public List<EmpiricalPoleDetection> empiricalPoleDetections = Arrays.asList(
            new EmpiricalPoleDetection(4, 68),
            new EmpiricalPoleDetection(8, 44),
            new EmpiricalPoleDetection(12, 32)
    );

    /**
     * The offset of the camera used for pole detection, relative to the front-center of the robot, in inches (the pole
     * reference point).  In other words, add this vector to the front center of the robot to get location of the
     * camera.
     */
    // todo: calibrate this
    /*
    public Vector2 poleDetectionCameraOffset = new Vector2(
            -1.0,
            -robotDimensionsInInches.height / 2.0
    );
    */
    public Vector2 poleDetectionCameraOffset = new Vector2(
            0,
            -3.0
    );

    public static final double LOGITECH_C310_FOV = 60; // diagonal FOV from the data sheet
    public static final double LOGITECH_C310_FOV_HORIZONTAL = (640 / new Vector2(640, 360).magnitude()) * LOGITECH_C310_FOV;

    /**
     * The webcam used for detecting April tags.
     */
    public WebCamDescriptor webCamAprilTagDescriptor = new WebCamDescriptor(
            "WebCamAprilTag",
            WebCamOrientation.FRONT_FORWARD,
            new Size(640, 480),
            LOGITECH_C310_FOV_HORIZONTAL
    );

    /**
     * The webcam used for detecting tile edges on the right side.
     */
    public WebCamDescriptor webCamSideDescriptor = new WebCamDescriptor(
            "WebCamSide",
            WebCamOrientation.RIGHT_SIDE_FIELD,
            anchor(new Position(0, 0), new Position(-0.270, 9.495)),
            anchor(new Position(640, 0), new Position(16.612, 9.84)),
            anchor(new Position(0, 360), new Position(1.752, 0.261)),
            anchor(new Position(640, 360), new Position(15.385, 1.442))
    );

    /**
     * The webcam used for detecting tile edges on the right side.
     */
    public WebCamDescriptor webCamFrontDescriptor = new WebCamDescriptor(
            "WebCamFront",
            WebCamOrientation.FRONT_FIELD,
            anchor(new Position(0, 0), new Position(0.605, 8.471)),
            anchor(new Position(640, 0), new Position(15.078, 7.287)),
            anchor(new Position(0, 360), new Position(0.649, 0.399)),
            anchor(new Position(640, 360), new Position(13.471, -0.681))
    );

    /**
     * An empirically measured strafe correction value for a given motor power.
     * <p>
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

    public static class EmpiricalPoleDetection {

        /**
         * How wide the pole is on the screen, measured in pixels
         */
        public double averageWidth;

        /**
         * How far away the pole is in the y direction, measured in inches.
         */
        public double distance;

        public EmpiricalPoleDetection(double distance, double averageWidth) {
            this.averageWidth = averageWidth;
            this.distance = distance;
        }
    }

    public static class RampingDescriptor {
        public double distanceUntilTargetReached;
        public double distanceStartRamping;
        public double maxPower;
        public double minPower;

        public RampingDescriptor(double distanceUntilTargetReached, double distanceStartRamping, double maxPower, double minPower) {
            this.distanceUntilTargetReached = distanceUntilTargetReached;
            this.distanceStartRamping = distanceStartRamping;
            this.maxPower = maxPower;
            this.minPower = minPower;
        }
    }

    public static class WebCamDescriptor {

        /**
         * The name of the webcam.
         */
        public String name;

        /**
         * The resolution of the side web cam, in pixels.
         */
        public Size resolution = new Size(640, 360);

        /**
         * The exposure duration for the webcam in milliseconds.
         */
        public long exposureMs = 15;

        /**
         * The orientation of the webcam.
         */
        public WebCamOrientation orientation;

        /**
         * The field of view of the webcam, if known.
         */
        public Double fieldOfViewDegrees;

        /**
         * For field facing webcams, the list of known anchor points obtained through calibration.
         */
        public WebCamAnchorPoint topLeft;
        public WebCamAnchorPoint topRight;
        public WebCamAnchorPoint bottomLeft;
        public WebCamAnchorPoint bottomRight;

        public WebCamDescriptor(String name, WebCamOrientation orientation) {
            this.name = name;
            this.orientation = orientation;
        }

        public WebCamDescriptor(
                String name, WebCamOrientation orientation,
                WebCamAnchorPoint topLeft, WebCamAnchorPoint topRight,
                WebCamAnchorPoint bottomLeft, WebCamAnchorPoint bottomRight
        ) {
            this.name = name;
            this.orientation = orientation;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }

        public WebCamDescriptor(String name, WebCamOrientation orientation, Size resolution, double fieldOfViewDegrees) {
            this.name = name;
            this.orientation = orientation;
            this.resolution = resolution;
            this.fieldOfViewDegrees = fieldOfViewDegrees;
        }
    }

    public enum WebCamOrientation {

        /**
         * The webcam is on the front of the robot, facing forward.
         */
        FRONT_FORWARD,

        /**
         * The webcam is on the front of the robot, facing down at the field.
         */
        FRONT_FIELD,

        /**
         * The webcam is on the right side of the robot, facing down at the field.
         */
        RIGHT_SIDE_FIELD

    }

    /**
     * An empirically measured mapping between the pixel coordinates on the webcam image, and
     * the coordinates on the field relative to the robot.
     */
    public static class WebCamAnchorPoint {

        /**
         * The pixel position on the image of the anchor point.
         */
        public Position image;

        /**
         * The position measured in inches from the corner of the robot.
         */
        public Position robot;

        public WebCamAnchorPoint(Position image, Position robot) {
            this.image = image;
            this.robot = robot;
        }

        public static WebCamAnchorPoint anchor(Position image, Position robot) {
            return new WebCamAnchorPoint(image, robot);
        }

    }

}
