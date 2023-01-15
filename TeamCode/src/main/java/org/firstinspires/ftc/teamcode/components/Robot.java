package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.firstinspires.ftc.teamcode.util.FileUtil;
import org.firstinspires.ftc.teamcode.util.TelemetryHolder;

import java.util.List;


public class Robot extends BaseComponent {

    private static final double VOLTAGE_WARNING_THRESHOLD = 12.3;

    private DriveTrain driveTrain;
    private WebCam webCamAprilTag;
    private WebCam webCamSide;
    private WebCam webCamFront;
    private AprilTagDetector aprilTagDetector;

    private Turret turret;
    private Intake intake;
    private LinearSlide slide;

    private int updateCount;
    private ElapsedTime initTime;
    private ElapsedTime firstUpdateTime;

    public enum CameraMode {
        DISABLED,
        ENABLED,
        ENABLED_AND_STREAMING_APRIL,
        ENABLED_AND_STREAMING_SIDE,
        ENABLED_AND_STREAMING_FRONT;

        public boolean isEnabled() {
            return this != DISABLED;
        }
    }

    public Robot(OpMode opMode, CameraMode cameraMode) {
        super(createRobotContext(opMode));

        this.webCamAprilTag = new WebCam(
                context, robotDescriptor.webCamAprilTagDescriptor,
                cameraMode == CameraMode.ENABLED_AND_STREAMING_APRIL
        );
        this.webCamSide = new WebCam(
                context, robotDescriptor.webCamSideDescriptor,
                cameraMode == CameraMode.ENABLED_AND_STREAMING_SIDE
        );
        this.webCamFront = new WebCam(
                context, robotDescriptor.webCamFrontDescriptor,
                cameraMode == CameraMode.ENABLED_AND_STREAMING_FRONT
        );

        this.driveTrain = new DriveTrain(context, webCamSide, webCamFront);
        getRobotContext().robotPositionProvider = driveTrain;

        this.aprilTagDetector = new AprilTagDetector(context, webCamAprilTag);

        this.turret = new Turret(context, new Turret.SafetyCheck() {
            @Override
            public boolean isSafeToMove() {
                return slide.isAtOrAbove(TRAVEL);
            }
        });

        this.slide = new LinearSlide(context);
        this.intake = new Intake(context);

        addSubComponents(driveTrain, turret, slide, intake);

        if (cameraMode.isEnabled()) {
            addSubComponents(webCamSide);
            addSubComponents(webCamFront);
            addSubComponents(aprilTagDetector);
        }

        TelemetryHolder.telemetry = telemetry;
    }

    private static RobotContext createRobotContext(OpMode opMode) {
        return new RobotContext(
                opMode,
                new RobotDescriptor()
        );
    }

    public RobotContext getRobotContext() {
        return context;
    }

    /**
     * Inits with default settings.
     */
    public Robot(OpMode opMode) {
        this(opMode, CameraMode.ENABLED);
    }

    @Override
    public void init() {
        super.init();

        double voltage = computeBatteryVoltage();
        if (voltage < VOLTAGE_WARNING_THRESHOLD) {
            telemetry.log().add("LOW BATTERY WARNING");
            telemetry.log().add("My battery is low and it's getting dark -Opportunity");
        }

        telemetry.log().add("Robot is initialized");
        telemetry.update();

        initTime = new ElapsedTime();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateStatus() {
        super.updateStatus();

        if (updateCount == 0) {
            firstUpdateTime = new ElapsedTime();
            onStart();
        }
        updateCount++;
        //double updatesPerSecond = updateCount / firstUpdateTime.seconds();
        //telemetry.addData("Updates / sec", String.format("%.1f", updatesPerSecond));

        // Update telemetry once per iteration after all components have been called.
        telemetry.update();
    }

    /**
     * Runs once, the first time the robot's updateStatus is called in an OpMode.
     */
    public void onStart() {
        // todo: Commenting this out for now since we no longer need to worry about moving the turrent from a diagonal
        // todo: start position to the front.  We may need to revisit this since the turret won't "lock" in place
        // todo: until the slide moves above a specific height.
        //slide.moveToHeight(TRAVEL);

        telemetry.log().clear();
    }

    /**
     * Finish all currently running commands.
     */
    public void waitForCommandsToFinish() {
        waitForCommandsToFinish(Double.MAX_VALUE);
    }

    public void waitForCommandsToFinish(double maxTime) {
        // While the components are busy trying to execute a command, keep looping and giving
        // each of them a chance to update.
        ElapsedTime time = new ElapsedTime();
        while (!isStopRequested() && isBusy() && time.seconds() < maxTime) {
            updateStatus();
        }
    }

    /**
     * Makes the robot not perform any additional commands until the idle time is finished
     */
    public void idle(double idleTime) {
        ElapsedTime time = new ElapsedTime();
        while (!isStopRequested() && time.seconds() < idleTime) {
            updateStatus();
        }
    }

    /**
     * Idle until the opMode is stopped.
     */
    public void waitForStop() {
        while (!isStopRequested()) {
            updateStatus();
        }
        stopAllCommands();
    }

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public LinearSlide getSlide() {
        return slide;
    }

    public Turret getTurret() {
        return turret;
    }

    public Intake getIntake() {
        return intake;
    }

    public WebCam getWebCamSide() {
        return webCamSide;
    }

    public WebCam getWebCamFront() {
        return webCamFront;
    }

    public AprilTagDetector getAprilTagDetector() {
        return aprilTagDetector;
    }

    public void savePositionToDisk() {
        savePositionToDisk("robot-position");
    }

    public void savePositionToDisk(String filename) {
        FileUtil.writeLines(
                filename,
                driveTrain.getPosition().getX(),
                driveTrain.getPosition().getY(),
                driveTrain.getHeading().getValue()
        );
    }

    public void loadPositionFromDisk() {
        loadPositionFromDisk("robot-position");
    }

    public void loadPositionFromDisk(String filename) {
        List<String> lines = FileUtil.readLines(filename);
        if (!lines.isEmpty()) {
            try {
                if (lines.size() != 3) {
                    throw new IllegalArgumentException("Expected 3 lines but found [" + lines.size() + "]");
                }

                Position position = new Position(
                        Double.parseDouble(lines.get(0)),
                        Double.parseDouble(lines.get(1))
                );
                Heading heading = new Heading(Double.parseDouble(lines.get(2)));

                driveTrain.setPosition(position);
                driveTrain.setHeading(heading);

            } catch (Exception e) {
                telemetry.log().add("Error loading position: " + ErrorUtil.convertToString(e));
            }

            // Now that the position has been consumed, remove the file
            FileUtil.removeFile(filename);
        }
    }

    private double computeBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

}
