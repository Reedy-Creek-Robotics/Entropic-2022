package org.firstinspires.ftc.teamcode.components;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.firstinspires.ftc.teamcode.util.FileUtil;
import org.firstinspires.ftc.teamcode.util.TelemetryHolder;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;

public class Robot extends BaseComponent {

    private static final double VOLTAGE_WARNING_THRESHOLD = 12.0;

    private DriveTrain driveTrain;
    private WebCam webCamAprilTag;
    private LinearSlide slide;

    private int updateCount;
    private ElapsedTime initTime;
    private ElapsedTime firstUpdateTime;

    public enum Camera {
        APRIL,
        SIDE,
        FRONT
    }

    public Robot(OpMode opMode, Camera streamingCamera, List<Camera> enabledCameras) {
        super(createRobotContext(opMode));

        this.webCamAprilTag = new WebCam(context, descriptor.WEBCAM_APRILTAG_DESCRIPTOR,
                streamingCamera == Camera.APRIL);

        this.driveTrain = new DriveTrain(context);

        this.slide = new LinearSlide(context);

        addSubComponents(driveTrain, slide);

        for (Camera camera : enabledCameras) {
            addSubComponents(getWebCam(camera));
        }

        TelemetryHolder.telemetry = telemetry;
    }

    private Mat createFrontCameraMask() {
        Mat mask = Mat.ones(descriptor.WEBCAM_FRONT_DESCRIPTOR.resolution, CvType.CV_8UC1);

        Imgproc.rectangle(mask, new Rect(
                new Point(0, 143),
                new Point(121, 360)
        ), new Scalar(0), -1);

        Imgproc.rectangle(mask, new Rect(
                new Point(0, 303),
                new Point(610, 360)
        ), new Scalar(0), -1);

        return mask;
    }

    public RobotContext getRobotContext() {
        return context;
    }

    /**
     * Inits with default settings.
     */
    public Robot(OpMode opMode) {
        this(opMode, null, Arrays.asList(Camera.FRONT, Camera.SIDE));
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

    public WebCam getWebCam(Camera camera) {
        switch (camera) {
            case APRIL:
                return getWebCamAprilTag();
            default:
                throw new IllegalArgumentException();
        }
    }

    public WebCam getWebCamAprilTag() {
        return webCamAprilTag;
    }
    public void savePositionToDisk() {
        savePositionToDisk("robot-position");
    }

    public void savePositionToDisk(String filename) {
        FileUtil.writeLines(
                filename,
                driveTrain.roadrunner.getLocalizer().getPoseEstimate().getX(),
                driveTrain.roadrunner.getLocalizer().getPoseEstimate().getY(),
                driveTrain.roadrunner.getLocalizer().getPoseEstimate().getHeading()
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

                driveTrain.roadrunner.getLocalizer().setPoseEstimate(new Pose2d(position.getX(),position.getY(),heading.getValue()));

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
