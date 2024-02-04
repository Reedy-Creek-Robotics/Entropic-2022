package org.firstinspires.ftc.teamcode.components;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.bareBones.StackKnocker;
import org.firstinspires.ftc.teamcode.roadrunner.util.LynxModuleUtil;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.firstinspires.ftc.teamcode.util.FileUtil;
import org.firstinspires.ftc.teamcode.util.TelemetryHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Robot extends BaseComponent {

    private static final double VOLTAGE_WARNING_THRESHOLD = 12.0;

    private List<LynxModule> lynxModules;

    private DriveTrain driveTrain;
    private WebCam webCamFront;
    private TeamPropDetector teamPropDetector;
    private WebCam webCamAprilTag;
    private LinearSlide slide;
    private Intake intake;
    private Outtake outtake;
    private RiggingLift riggingLift;
    private StackKnocker stackKnocker;
    private DroneLauncher droneLauncher;

    private int updateCount;
    private ElapsedTime initTime;
    private ElapsedTime firstUpdateTime;

    public enum Camera {
        FRONT,
        APRIL
    }

    public Robot(OpMode opMode, Camera streamingCamera, List<Camera> enabledCameras) {
        super(createRobotContext(opMode));

        this.lynxModules = hardwareMap.getAll(LynxModule.class);

        this.webCamFront = new WebCam(context, descriptor.webcamFrontDescriptor,
                streamingCamera == Camera.FRONT);
        this.webCamAprilTag = new WebCam(context, descriptor.webcamAprilTagDescriptor,
                streamingCamera == Camera.APRIL);

        this.driveTrain = new DriveTrain(context);

        this.slide = new LinearSlide(context);

        this.intake = new Intake(context);

        this.outtake = new Outtake(context);

        this.riggingLift = new RiggingLift(context);

        this.stackKnocker = new StackKnocker(context);

        this.droneLauncher = new DroneLauncher(context);

        this.teamPropDetector = new TeamPropDetector(context, webCamFront);

        addSubComponents(driveTrain, intake, outtake, stackKnocker, droneLauncher, teamPropDetector, slide, riggingLift);

        for (Camera camera : enabledCameras) {
            addSubComponents(getWebCam(camera));
        }

        TelemetryHolder.telemetry = telemetry;
    }

    public RobotContext getRobotContext() {
        return context;
    }

    /**
     * Inits with default settings.
     */
    public Robot(OpMode opMode) {
        this(opMode, null, Arrays.asList(Camera.FRONT));
    }

    @Override
    public void init() {
        super.init();

        // Check the battery level and print a warning if it's low
        double voltage = computeBatteryVoltage();
        if (voltage < VOLTAGE_WARNING_THRESHOLD) {
            telemetry.log().add("LOW BATTERY WARNING");
            telemetry.log().add("My battery is low and it's getting dark -Opportunity");
        }

        // Set the caching mode for reading values from Lynx components to manual. This means that when reading values
        // like motor positions, the code will grab all values at once instead of one at a time. It will also keep
        // these values and not update them until a manual call is made to clear the cache. We do this once per loop
        // in the Robot's update method.
        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);
        for (LynxModule module : lynxModules) {
            //module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        telemetry.log().add("Robot is initialized");
        telemetry.update();

        initTime = new ElapsedTime();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void update() {
        if (updateCount == 0) {
            firstUpdateTime = new ElapsedTime();
            onStart();
        }

        // Compute and print the updates per second
        computeUpdatesPerSecond();

        // Clear the bulk cache so that new values will be read for each component
        for (LynxModule lynxModule : lynxModules) {
            lynxModule.clearBulkCache();
        }

        // Allow all the subcomponents to do their work.
        super.update();

        // Update telemetry once per iteration after all components have been called.
        telemetry.update();

        if (slide.getPosition() <= slide.ROTATION_POINT){
            outtake.closeAll();
        }
    }

    @SuppressLint("DefaultLocale")
    private void computeUpdatesPerSecond() {
        updateCount++;

        double updatesPerSecond = updateCount / firstUpdateTime.seconds();
        telemetry.addData("Updates / sec", String.format("%.1f", updatesPerSecond));
    }

    /**
     * Runs once, the first time the robot's update method is called in an OpMode.
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
            update();
        }
    }

    /**
     * Makes the robot not perform any additional commands until the idle time is finished
     */
    public void idle(double idleTime) {
        ElapsedTime time = new ElapsedTime();
        while (!isStopRequested() && time.seconds() < idleTime) {
            update();
        }
    }

    /**
     * Idle until the opMode is stopped.
     */
    public void waitForStop() {
        while (!isStopRequested()) {
            update();
        }
        stopAllCommands();
    }

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public TeamPropDetector getTeamPropDetector() {
        return teamPropDetector;
    }

    public LinearSlide getSlide() {
        return slide;
    }

    public Intake getIntake() {
        return intake;
    }

    public Outtake getOuttake() {
        return outtake;
    }

    public RiggingLift getRiggingLift() {
        return riggingLift;
    }

    public StackKnocker getStackKnocker() { return stackKnocker; }

    public DroneLauncher getDroneLauncher() { return droneLauncher; }

    public WebCam getWebCam(Camera camera) {
        switch (camera) {
            case FRONT:
                return getWebCamFront();
            case APRIL:
                return getWebCamAprilTag();
            default:
                throw new IllegalArgumentException();
        }
    }

    public WebCam getWebCamFront() {
        return webCamFront;
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
                driveTrain.roadrunner.getLocalizer().getPoseEstimate().getHeading(),
                this.context.getAlliance().toString(),
                slide.getPosition()
        );
    }

    public void loadPositionFromDisk() {
        loadPositionFromDisk("robot-position");
    }

    public void loadPositionFromDisk(String filename) {
        List<String> lines = FileUtil.readLines(filename);
        if (!lines.isEmpty()) {
            try {
                if (lines.size() != 4) {
                    throw new IllegalArgumentException("Expected 4 lines but found [" + lines.size() + "]");
                }

                Pose2d pose2d = new Pose2d(
                        Double.parseDouble(lines.get(0)),
                        Double.parseDouble(lines.get(1)),
                        Double.parseDouble(lines.get(2))
                );

                driveTrain.roadrunner.getLocalizer().setPoseEstimate(pose2d);

                this.setAlliance(Objects.equals(lines.get(3), "BLUE") ? RobotContext.Alliance.BLUE : RobotContext.Alliance.RED);

                slide.setStartPosition(Integer.parseInt(lines.get(4)));

            } catch (Exception e) {
                telemetry.log().add("Error loading position: " + ErrorUtil.convertToString(e));
            }

            // Now that the position has been consumed, remove the file
            FileUtil.removeFile(filename);
        }
    }

    public void clearFileFromDisk(){
        clearFileFromDisk("robot-position");
    }
    public void clearFileFromDisk(String filename){
        FileUtil.removeFile(filename);

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

    public void setAlliance(RobotContext.Alliance alliance) {
        this.context.alliance = alliance;
        teamPropDetector.setTargetColor(alliance);
    }


}
