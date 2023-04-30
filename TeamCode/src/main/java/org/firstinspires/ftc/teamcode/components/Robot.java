package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.drive.DriveTrain;
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
    private WebCam webCamSide;
    private WebCam webCamFront;

    private AprilTagDetector aprilTagDetector;
    private PoleDetector poleDetector;

    private Turret turret;
    private Intake intake;
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

//        this.webCamAprilTag = new WebCam(context, robotDescriptor.webCamAprilTagDescriptor,
//                streamingCamera == Camera.APRIL);
//        this.webCamSide = new WebCam(context, robotDescriptor.webCamSideDescriptor,
//                streamingCamera == Camera.SIDE);
//        this.webCamFront = new WebCam(context, robotDescriptor.webCamFrontDescriptor,
//                streamingCamera == Camera.FRONT);

        this.driveTrain = new DriveTrain(context, webCamSide, webCamFront);
        getRobotContext().robotPositionProvider = driveTrain;

//        this.poleDetector = new PoleDetector(context, webCamAprilTag);

        //this.driveTrain.getTileEdgeDetectorFront().setWebCamMask(createFrontCameraMask());

        //this.aprilTagDetector = new AprilTagDetector(context, webCamAprilTag);

        /*this.turret = new Turret(context, new Turret.SafetyCheck() {
            @Override
            public boolean isSafeToMove() {
                return slide.isAtOrAbove(TRAVEL);
            }
        });*/

//        this.slide = new LinearSlide(context);
//        this.intake = new Intake(context);

        addSubComponents(driveTrain);//, turret, slide, intake);

        /*for (Camera camera : enabledCameras) {
            addSubComponents(getWebCam(camera));
        }*/

        TelemetryHolder.telemetry = telemetry;
    }

    private Mat createFrontCameraMask() {
        Mat mask = Mat.ones(robotDescriptor.webCamFrontDescriptor.resolution, CvType.CV_8UC1);

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

    public Turret getTurret() {
        return turret;
    }

    public Intake getIntake() {
        return intake;
    }

    public WebCam getWebCam(Camera camera) {
        switch (camera) {
            case APRIL:
                return getWebCamAprilTag();
            case SIDE:
                return getWebCamSide();
            case FRONT:
                return getWebCamFront();
            default:
                throw new IllegalArgumentException();
        }
    }

    public WebCam getWebCamSide() {
        return webCamSide;
    }

    public WebCam getWebCamFront() {
        return webCamFront;
    }

    public WebCam getWebCamAprilTag() {
        return webCamAprilTag;
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
