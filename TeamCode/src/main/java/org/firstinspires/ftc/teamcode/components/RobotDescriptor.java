package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.RobotDescriptor.WebCamAnchorPoint.anchor;

import com.acmerobotics.dashboard.config.Config;
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

@Config
public class RobotDescriptor {

    public static double mm(double value) {
        return value / 25.4;
    }

    /**
     * The size of the robot in inches, with the x axis being left to right and the y axis being
     * back to front.
     */
    public static Size ROBOT_DIMENSIONS_IN_INCHES = new Size(18, 18);

    public static DriveTuner DRIVE_TUNER = new DriveTuner(
            537.7,
            312,
            false,
            0, 0, 0,
            48 / 25.4,
            1,
            15.9,
            0.014,
            0.0039,
            0.0001,
            30,
            30,
            Math.toDegrees(4.364927661988055),
            180,
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD,
            new PIDCoefficients(5, 0, 0),
            new PIDCoefficients(5, 0, 0),
            1.69,
            1,
            1,
            1
    );

    public static class DriveTuner {
        /*
         * These are motor constants that should be listed online for your motors.
         */
        public double driveTicksPerRev;
        public double driveMaxRpm;

        /*
         * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
         * Set this flag to false if drive encoders are not present and an alternative localization
         * method is in use (e.g., tracking wheels).
         *
         * If using the built-in motor velocity PID, update MOTOR_VELO_PID with the tuned coefficients
         * from DriveVelocityPIDTuner.
         */
        public boolean runUsingEncoder;
        public PIDFCoefficients driveMotorVeloPid;

        /*
         * These are physical constants that can be determined from your robot (including the track
         * width; it will be tune empirically later although a rough estimate is important). Users are
         * free to chose whichever linear distance unit they would like so long as it is consistently
         * used. The default values were selected with inches in mind. Road runner uses radians for
         * angular distances although most angular parameters are wrapped in Math.toRadians() for
         * convenience. Make sure to exclude any gear ratio included in MOTOR_CONFIG from GEAR_RATIO.
         */
        public double driveWheelRadius;
        public double driveGearRatio;
        public double driveTrackWidth;

        /*
         * These are the feedforward parameters used to model the drive motor behavior. If you are using
         * the built-in velocity PID, *these values are fine as is*. However, if you do not have drive
         * motor encoders or have elected not to use them for velocity control, these values should be
         * empirically tuned.
         */
        public double kv;
        public double ka;
        public double kStatic;

        /*
         * These values are used to generate the trajectories for you robot. To ensure proper operation,
         * the constraints should never exceed ~80% of the robot's actual capabilities. While Road
         * Runner is designed to enable faster autonomous motion, it is a good idea for testing to start
         * small and gradually increase them later after everything is working. All distance units are
         * inches.
         */
        public double maxVel;
        public double maxAccel;
        public double maxAngVel;
        public double maxAngAccel;

        /*
         * Adjust the orientations here to match your robot. See the FTC SDK documentation for details.
         */
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDir;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDir;
        public PIDCoefficients translationalPid;
        public PIDCoefficients headingPid;
        public double lateralMultiplier;
        public double vxWeight;
        public double vyWeight;
        public double omegaWeight;
        public TrajectoryVelocityConstraint velConstraint;
        public TrajectoryAccelerationConstraint accelConstraint;

        public DriveTuner(double driveTicksPerRev, double driveMaxRpm, boolean runUsingEncoder, double p, double i, double d, double driveWheelRadius, double driveGearRatio, double driveTrackWidth, double kv, double ka, double kStatic, double maxVel, double maxAccel, double maxAngVel, double maxAngAccel, RevHubOrientationOnRobot.LogoFacingDirection logoFacingDir, RevHubOrientationOnRobot.UsbFacingDirection usbFacingDir, PIDCoefficients translationalPid, PIDCoefficients headingPid, double lateralMultiplier, double vxWeight, double vyWeight, double omegaWeight) {
            this.driveTicksPerRev = driveTicksPerRev;
            this.driveMaxRpm = driveMaxRpm;
            this.runUsingEncoder = runUsingEncoder;
            this.driveMotorVeloPid = new PIDFCoefficients(p, i, d, getMotorVelocityF(driveMaxRpm / 60 * driveTicksPerRev));
            this.driveWheelRadius = driveWheelRadius;
            this.driveGearRatio = driveGearRatio;
            this.driveTrackWidth = driveTrackWidth;
            this.kv = kv ;
            this.ka = ka;
            this.kStatic = kStatic;
            this.maxVel = maxVel;
            this.maxAccel = maxAccel;
            this.maxAngVel = Math.toRadians(maxAngVel);
            this.maxAngAccel = Math.toRadians(maxAngAccel);
            this.logoFacingDir = logoFacingDir;
            this.usbFacingDir = usbFacingDir;
            this.translationalPid = translationalPid;
            this.headingPid = headingPid;
            this.lateralMultiplier = lateralMultiplier;
            this.vxWeight = vxWeight;
            this.vyWeight = vyWeight;
            this.omegaWeight = omegaWeight;
            this.velConstraint = getVelocityConstraint(maxVel, maxAngVel, driveTrackWidth);
            this.accelConstraint = getAccelerationConstraint(maxAccel);
        }

        public double encoderTicksToInches(double ticks) {
            return driveWheelRadius * 2 * Math.PI * driveGearRatio * ticks / driveTicksPerRev;
        }

        public double rpmToVelocity(double rpm) {
            return rpm * driveGearRatio * 2 * Math.PI * driveWheelRadius / 60.0;
        }

        public double getMotorVelocityF(double ticksPerSecond) {
            // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
            return 32767 / ticksPerSecond;
        }

        public TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
            return new MinVelocityConstraint(Arrays.asList(
                    new AngularVelocityConstraint(maxAngularVel),
                    new MecanumVelocityConstraint(maxVel, trackWidth)
            ));
        }

        public TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
            return new ProfileAccelerationConstraint(maxAccel);
        }

    }

    public static OdometryTuner ODOMETRY_TUNER = new OdometryTuner(
            2000,
            mm(24),
            1,
            1,
            1,
            5.71285,
            5
    );

    public static class OdometryTuner {
        public double odometryTicksPerRev;
        public double odometryWheelRadius; // in
        public double odometryGearRatio; // output (wheel) speed / input (encoder) speed

        public double xMultiplier; // Multiplier in the X direction
        public double yMultiplier; // Multiplier in the Y direction

        public double odometryLateralDistance; // in; distance between the left and right wheels
        public double odometryForwardOffset; // in; offset of the lateral wheel

        public OdometryTuner(double odometryTicksPerRev, double odometryWheelRadius, double odometryGearRatio, double xMultiplier, double yMultiplier, double odometryLateralDistance, double odometryForwardOffset) {
            this.odometryTicksPerRev = odometryTicksPerRev;
            this.odometryWheelRadius = odometryWheelRadius;
            this.odometryGearRatio = odometryGearRatio;
            this.xMultiplier = xMultiplier;
            this.yMultiplier = yMultiplier;
            this.odometryLateralDistance = odometryLateralDistance;
            this.odometryForwardOffset = odometryForwardOffset;
        }
    }

    /**
     * The webcam used for detecting April tags.
     */
    public final WebCamDescriptor webcamAprilTagDescriptor = new WebCamDescriptor(
            "WebCamAprilTag",
            WebCamOrientation.BACK_BACKWARD,
            new Size(640, 480)
    );

    /**
     * The webcam used for detecting tile edges on the right side.
     */
    public final WebCamDescriptor webcamFrontDescriptor = new WebCamDescriptor(
            "WebCamFront",
            WebCamOrientation.FRONT_FORWARD,
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
        public Size resolution = new Size(1920, 1080);

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
        public WebCamAnchorPoint topLeft;
        public WebCamAnchorPoint topRight;
        public WebCamAnchorPoint bottomLeft;
        public WebCamAnchorPoint bottomRight;

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
         * The webcam is on the back of the robot, facing backward.
         */
        BACK_BACKWARD,

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
