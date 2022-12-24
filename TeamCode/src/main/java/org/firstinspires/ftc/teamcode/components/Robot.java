package org.firstinspires.ftc.teamcode.components;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.util.TelemetryHolder;


public class Robot extends BaseComponent {

    private DriveTrain driveTrain;
    private WebCam webCamSide;
    private AprilTagDetector aprilTagDetector;

    private Turret turret;
    private Intake intake;
    private LinearSlide slide;

    private int updateCount;
    private ElapsedTime initTime;

    public Robot(OpMode opMode, boolean initWithCamera) {
        super(createRobotContext(opMode));

        this.webCamSide = new WebCam(context, "WebcamFront", initWithCamera);
        this.driveTrain = new DriveTrain(context, webCamSide);
        this.aprilTagDetector = new AprilTagDetector(context, webCamSide);

        //this.turret = new Turret(context);
        //this.slide = new LinearSlide(context);
        //this.intake = new Intake(context);

        addSubComponents(driveTrain);  //, turret, slide, intake);

        if (initWithCamera) {
            addSubComponents(webCamSide);
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
        this(opMode, true);
    }

    @Override
    public void init() {
        super.init();

        telemetry.log().add("Robot is initialized");
        telemetry.update();

        initTime = new ElapsedTime();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateStatus() {
        super.updateStatus();

        updateCount++;
        double updatesPerSecond = updateCount / initTime.seconds();
        telemetry.addData("Updates / sec", String.format("%.1f", updatesPerSecond));

        // Update telemetry once per iteration after all components have been called.
        telemetry.update();
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

    public WebCam getFrontWebCam() {
        return webCamSide;
    }

    public AprilTagDetector getAprilTagDetector() {
        return aprilTagDetector;
    }

}
