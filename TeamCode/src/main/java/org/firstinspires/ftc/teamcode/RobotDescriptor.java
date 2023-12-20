package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamAnchorPoint.anchor;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.opencv.core.Size;

import java.util.Arrays;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The size of the robot in inches, with the x axis being left to right and the y axis being
     * back to front.
     */
    public static final Size ROBOT_DIMENSIONS_IN_INCHES = new Size(15.5, 15.5);

    /*
     * These are motor constants that should be listed online for your motors.
     */
    public static final double DRIVE_TICKS_PER_REV = 2000;
    public static final double DRIVE_MAX_RPM = 1;

    /*
     * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
     * Set this flag to false if drive encoders are not present and an alternative localization
     * method is in use (e.g., tracking wheels).
     *
     * If using the built-in motor velocity PID, update MOTOR_VELO_PID with the tuned coefficients
     * from DriveVelocityPIDTuner.
     */
    public static final boolean RUN_USING_ENCODER = false;
    public static PIDFCoefficients DRIVE_MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0,
            getMotorVelocityF(DRIVE_MAX_RPM / 60 * DRIVE_TICKS_PER_REV));

    /*
     * These are physical constants that can be determined from your robot (including the track
     * width; it will be tune empirically later although a rough estimate is important). Users are
     * free to chose whichever linear distance unit they would like so long as it is consistently
     * used. The default values were selected with inches in mind. Road runner uses radians for
     * angular distances although most angular parameters are wrapped in Math.toRadians() for
     * convenience. Make sure to exclude any gear ratio included in MOTOR_CONFIG from GEAR_RATIO.
     */
    public static double DRIVE_WHEEL_RADIUS = 2; // in
    public static double DRIVE_GEAR_RATIO = 1; // output (wheel) speed / input (motor) speed
    public static double DRIVE_TRACK_WIDTH = 1; // in

    /*
     * These are the feedforward parameters used to model the drive motor behavior. If you are using
     * the built-in velocity PID, *these values are fine as is*. However, if you do not have drive
     * motor encoders or have elected not to use them for velocity control, these values should be
     * empirically tuned.
     */
    public static double kV = 1.0 / rpmToVelocity(DRIVE_MAX_RPM);
    public static double kA = 0;
    public static double kStatic = 0;

    /*
     * These values are used to generate the trajectories for you robot. To ensure proper operation,
     * the constraints should never exceed ~80% of the robot's actual capabilities. While Road
     * Runner is designed to enable faster autonomous motion, it is a good idea for testing to start
     * small and gradually increase them later after everything is working. All distance units are
     * inches.
     */
    public static double MAX_VEL = 30;
    public static double MAX_ACCEL = 30;
    public static double MAX_ANG_VEL = Math.toRadians(60);
    public static double MAX_ANG_ACCEL = Math.toRadians(60);

    /*
     * Adjust the orientations here to match your robot. See the FTC SDK documentation for details.
     */
    public static RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIR =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static RevHubOrientationOnRobot.UsbFacingDirection USB_FACING_DIR =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;


    public static double encoderTicksToInches(double ticks) {
        return DRIVE_WHEEL_RADIUS * 2 * Math.PI * DRIVE_GEAR_RATIO * ticks / DRIVE_TICKS_PER_REV;
    }

    public static double rpmToVelocity(double rpm) {
        return rpm * DRIVE_GEAR_RATIO * 2 * Math.PI * DRIVE_WHEEL_RADIUS / 60.0;
    }

    public static double getMotorVelocityF(double ticksPerSecond) {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond;
    }

    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(0, 0, 0);
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);

    public static double LATERAL_MULTIPLIER = 1;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double OMEGA_WEIGHT = 1;

    public static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, DRIVE_TRACK_WIDTH);
    public static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(MAX_ACCEL);

    public static TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
        return new MinVelocityConstraint(Arrays.asList(
                new AngularVelocityConstraint(maxAngularVel),
                new MecanumVelocityConstraint(maxVel, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
        return new ProfileAccelerationConstraint(maxAccel);
    }

    public static double mm(double value) {
        return value / 25.4;
    }

    public static double ODOMETRY_TICKS_PER_REV = 2000 * 2;
    public static double ODOMETRY_WHEEL_RADIUS = mm(48); // in
    public static double ODOMETRY_GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double X_MULTIPLIER = 1; // Multiplier in the X direction
    public static double Y_MULTIPLIER = 1; // Multiplier in the Y direction

    public static double ODOMETRY_LATERAL_DISTANCE = mm(196.525); // in; distance between the left and right wheels
    public static double ODOMETRY_FORWARD_OFFSET = mm(-150); // in; offset of the lateral wheel

    /**
     * The webcam used for detecting April tags.
     */
    public static final WebCamDescriptor WEBCAM_APRILTAG_DESCRIPTOR = new WebCamDescriptor(
            "WebCamAprilTag",
            WebCamOrientation.FRONT_FORWARD,
            new Size(640,480)
    );

    /**
     * The webcam used for detecting tile edges on the right side.
     */
    public static final WebCamDescriptor WEBCAM_SIDE_DESCRIPTOR = new WebCamDescriptor(
            "WebCamSide",
            WebCamOrientation.RIGHT_SIDE_FIELD,
            anchor(new Position(0, 0), new Position(-0.125, 9.660)),
            anchor(new Position(640, 0), new Position(16.746, 9.948)),
            anchor(new Position(0, 360), new Position(1.845, 0.316)),
            anchor(new Position(640, 360), new Position(15.489, 0.816))
    );

    /**
     * The webcam used for detecting tile edges on the right side.
     */
    public static final WebCamDescriptor WEBCAM_FRONT_DESCRIPTOR = new WebCamDescriptor(
            "WebCamFront",
            WebCamOrientation.FRONT_FIELD,
            anchor(new Position(0, 0), new Position(0.361, 8.352)),
            anchor(new Position(640, 0), new Position(14.355, 7.216)),
            anchor(new Position(0, 360), new Position(0.638, 0.051)),
            anchor(new Position(640, 360), new Position(13.570, -0.670))
    );


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
         * For field facing webcams, the list of known anchor points obtained through calibration.
         */
        //public List<WebCamAnchorPoint> anchorPoints = new ArrayList<>();
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

        public WebCamDescriptor(String name, WebCamOrientation orientation, Size resolution) {
            this.name = name;
            this.orientation = orientation;
            this.resolution = resolution;
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
