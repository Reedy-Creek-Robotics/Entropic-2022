package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.RobotDescriptor.DriveTuner;
import static org.firstinspires.ftc.teamcode.components.RobotDescriptor.OdometryTuner;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.roadrunner.drive.ModifiedMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.util.LynxModuleUtil;
import org.firstinspires.ftc.teamcode.util.DriveUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("DefaultLocale")
public class DriveTrain extends BaseComponent {

    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    private List<DcMotorEx> motors;

    private IMU imu;
    private VoltageSensor batteryVoltageSensor;

    public ModifiedMecanumDrive roadrunner;

    private RobotContext context;

    public static DriveTuner driveTuner;
    public static OdometryTuner odometryTuner;

    public DriveTrain(RobotContext context) {
        super(context);

        driveTuner = descriptor.DRIVE_TUNER;
        odometryTuner = descriptor.ODOMETRY_TUNER;

        List<Integer> lastTrackingEncPositions = new ArrayList<>();
        List<Integer> lastTrackingEncVels = new ArrayList<>();

        //this.context.localizer = new StandardTrackingWheelLocalizer(hardwareMap, lastTrackingEncPositions, lastTrackingEncVels, odometryTuner);

        //TODO: move LynxModule to robot and set to manual(don't get new info until we tell you)
        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        // TODO: adjust the names of the following hardware devices to match your configuration
        /* imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                DriveConstants.LOGO_FACING_DIR, DriveConstants.USB_FACING_DIR));
        imu.initialize(parameters);*/

        leftFront = hardwareMap.get(DcMotorEx.class, "FrontLeft");
        leftRear = hardwareMap.get(DcMotorEx.class, "BackLeft");
        rightRear = hardwareMap.get(DcMotorEx.class, "BackRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "FrontRight");

//        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
//        leftRear.setDirection(DcMotorEx.Direction.REVERSE);

        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        //TODO: move localizer to context eventually
        StandardTrackingWheelLocalizer localizer = new StandardTrackingWheelLocalizer(hardwareMap,
                lastTrackingEncPositions,
                lastTrackingEncVels,
                odometryTuner
        );

        roadrunner = new ModifiedMecanumDrive(
                localizer,
                motors,
                imu,
                batteryVoltageSensor,
                driveTuner
        );

        if (driveTuner.runUsingEncoder) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (driveTuner.runUsingEncoder && driveTuner.driveMotorVeloPid != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, driveTuner.driveMotorVeloPid);
        }
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void drive(double drive, double strafe, double turn, double speedFactor) {
        DriveUtil.MotorPowers motorPowers = context.driveUtil.calculateWheelPowerForDrive(drive, strafe, turn, speedFactor);

        leftFront.setPower(motorPowers.frontLeft);
        leftRear.setPower(motorPowers.backLeft);
        rightFront.setPower(motorPowers.frontRight);
        rightRear.setPower(motorPowers.backRight);
    }

    public void driverRelative(double drive, double strafe, double turn, double speedFactor) {
        DriveUtil.MotorPowers motorPowers = context.driveUtil.calculateWheelPowerForDriverRelative(drive, strafe, turn, new Heading(context.localizer.getPoseEstimate().getHeading()), speedFactor);

        leftFront.setPower(motorPowers.frontLeft);
        leftRear.setPower(motorPowers.backLeft);
        rightFront.setPower(motorPowers.frontRight);
        rightRear.setPower(motorPowers.backRight);
    }
}
