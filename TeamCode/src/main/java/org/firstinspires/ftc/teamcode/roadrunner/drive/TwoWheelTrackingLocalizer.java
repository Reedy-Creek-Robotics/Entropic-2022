package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    ^
 *    |
 *    | ( x direction)
 *    |
 *    v
 *    <----( y direction )---->

 *        (forward)
 *    /--------------\
 *    |     ____     |
 *    |     ----     |    <- Perpendicular Wheel
 *    |           || |
 *    |           || |    <- Parallel Wheel
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
public class TwoWheelTrackingLocalizer extends TwoTrackingWheelLocalizer {

    // Parallel/Perpendicular to the forward axis
    // Parallel wheel is parallel to the forward axis
    // Perpendicular is perpendicular to the forward axis
    private Encoder parallelEncoder, perpendicularEncoder;

    private IMU imu;

    private static RobotDescriptor.OdometryTuner odometryTuner;
    private static RobotDescriptor.DriveTuner driveTuner;

    public TwoWheelTrackingLocalizer(HardwareMap hardwareMap, RobotDescriptor descriptor) {
        super(Arrays.asList(
            new Pose2d(descriptor.ODOMETRY_TUNER.parrallel_x, descriptor.ODOMETRY_TUNER.parrallel_y, 0),
            new Pose2d(descriptor.ODOMETRY_TUNER.perpendicular_x, descriptor.ODOMETRY_TUNER.perpendicular_y, Math.toRadians(90))
        ));

        odometryTuner = descriptor.ODOMETRY_TUNER;
        driveTuner = descriptor.DRIVE_TUNER;

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                driveTuner.logoFacingDir,  driveTuner.usbFacingDir));
        imu.initialize(parameters);

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "BackLeft"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FrontLeft"));

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
        //perpendicularEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    public static double encoderTicksToInches(double ticks) {
        return odometryTuner.odometryWheelRadius * 2 * Math.PI * odometryTuner.odometryGearRatio * ticks / odometryTuner.odometryTicksPerRev;
    }

    @Override
    public double getHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

    }

    @Override
    public Double getHeadingVelocity() {
        return (double) imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(parallelEncoder.getCurrentPosition()) * odometryTuner.xMultiplier,
                encoderTicksToInches(perpendicularEncoder.getCurrentPosition() * odometryTuner.yMultiplier)
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(
                encoderTicksToInches(parallelEncoder.getRawVelocity()) * odometryTuner.xMultiplier,
                encoderTicksToInches(perpendicularEncoder.getRawVelocity() * odometryTuner.yMultiplier)
        );
    }
}
