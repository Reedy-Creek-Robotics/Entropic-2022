package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Robot extends BaseComponent {

    private Arm arm;
    private DuckSpinner duckSpinner;
    private DriveTrain driveTrain;
    private Intake intake;
    private Capper capper;
    private WebCam webCamRear;
    private WebCam webCamSide;
    private BarCodePositionDetector barCodePositionDetector;
    private LedStrip ledStrip;

    public Robot(OpMode opMode, boolean initWithCamera) {
        super(opMode);

        this.arm = new Arm(opMode);
        this.duckSpinner = new DuckSpinner(opMode);
        this.intake = new Intake(opMode);
        this.capper = new Capper(opMode);
        this.webCamRear = new WebCam(opMode, "WebcamRear", false);
        this.webCamSide = new WebCam(opMode, "WebcamFront", false);
        this.barCodePositionDetector = new BarCodePositionDetector(opMode, webCamRear);
        this.driveTrain = new DriveTrain(opMode, webCamRear, webCamSide);
        this.ledStrip = new LedStrip(opMode);

        addSubComponents(arm, duckSpinner, driveTrain, intake, capper);

        if (initWithCamera) {
            addSubComponents(webCamRear, webCamSide, barCodePositionDetector);
        }
    }

    public Robot(OpMode opMode, int yeah) {
        super(opMode);

        this.arm = new Arm(opMode);
        this.duckSpinner = new DuckSpinner(opMode);
        this.intake = new Intake(opMode);
        this.capper = new Capper(opMode);
        this.webCamRear = new WebCam(opMode, "WebcamRear", true);
        this.webCamSide = new WebCam(opMode, "WebcamFront", false);
        this.barCodePositionDetector = new BarCodePositionDetector(opMode, webCamRear);
        this.driveTrain = new DriveTrain(opMode, webCamRear, webCamSide);
        this.ledStrip = new LedStrip(opMode);

        addSubComponents(arm, duckSpinner, driveTrain, intake, capper,webCamRear, webCamSide, barCodePositionDetector);
    }

    public Robot(OpMode opMode) {
        super(opMode);

        this.arm = new Arm(opMode);
        this.duckSpinner = new DuckSpinner(opMode);
        this.intake = new Intake(opMode);
        this.capper = new Capper(opMode);
        this.webCamRear = new WebCam(opMode, "WebcamRear", false);
        this.webCamSide = new WebCam(opMode, "WebcamFront", true);
        this.barCodePositionDetector = new BarCodePositionDetector(opMode, webCamRear);
        this.driveTrain = new DriveTrain(opMode, webCamRear, webCamSide);
        this.ledStrip = new LedStrip(opMode);

        addSubComponents(arm, duckSpinner, driveTrain, intake, capper,webCamRear, webCamSide, barCodePositionDetector);
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
        stopCommand();
    }

    public Arm getArm() {
        return arm;
    }

    public DuckSpinner getDuckSpinner() {
        return duckSpinner;
    }

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public Intake getIntake() {
        return intake;
    }

    public Capper getCapper() {
        return capper;
    }

    public WebCam getRearWebCam() {
        return webCamRear;
    }

    public WebCam getFrontWebCam() {
        return webCamSide;
    }

    public BarCodePositionDetector getBarCodePositionDetector() {
        return barCodePositionDetector;
    }

    public LedStrip getLedStrip(){return ledStrip;}
}
