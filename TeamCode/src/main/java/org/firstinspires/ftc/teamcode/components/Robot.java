package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.AprilTagDetector;
import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.components.WebCam;
import org.firstinspires.ftc.teamcode.util.TelemetryUtil;


public class Robot extends BaseComponent {

    private DriveTrain driveTrain;
    private WebCam webCamSide;
    private AprilTagDetector aprilTagDetector;

    private Turret turret;
    private Intake intake;
    private LinearSlide slide;

    public Robot(OpMode opMode, boolean initWithCamera) {
        super(opMode);

        this.webCamSide = new WebCam(opMode, "WebcamFront", initWithCamera);
        this.driveTrain = new DriveTrain(opMode, webCamSide);
        this.aprilTagDetector = new AprilTagDetector(opMode, webCamSide);

        //this.turret = new Turret(opMode);
        //this.slide = new LinearSlide(opMode);
        //this.intake = new Intake(opMode);

        addSubComponents(driveTrain);  //, turret, slide, intake);

        if (initWithCamera) {
            addSubComponents(webCamSide);
            addSubComponents(aprilTagDetector);
        }

        TelemetryUtil.telemetry = opMode.telemetry;
    }

    /**
     * Inits with default settings.
     */
    public Robot(OpMode opMode) {
        this(opMode, true);
    }

    @Override
    public void updateStatus() {
        super.updateStatus();

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
