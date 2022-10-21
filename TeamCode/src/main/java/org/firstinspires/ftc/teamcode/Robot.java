package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Robot extends BaseComponent {

    private DriveTrain driveTrain;
    private WebCam webCamSide;

    public Robot(OpMode opMode, boolean initWithCamera) {
        super(opMode);

        this.webCamSide = new WebCam(opMode, "WebcamFront", false);
        this.driveTrain = new DriveTrain(opMode, webCamSide);

        addSubComponents(driveTrain);

        if (initWithCamera) {
            addSubComponents(webCamSide);
        }
    }

    /**
     * Inits the front webcam to stream
     */
    public Robot(OpMode opMode) {
        super(opMode);

        this.webCamSide = new WebCam(opMode, "WebcamFront", true);
        this.driveTrain = new DriveTrain(opMode, webCamSide);

        addSubComponents(driveTrain, webCamSide);
    }

    public Robot(OpMode opMode, AprilTagDetectionPipeline aprilTagDetectionPipeline) {
        super(opMode);

        this.webCamSide = new WebCam(opMode,"WebcamFront",true, aprilTagDetectionPipeline);
        this.driveTrain = new DriveTrain(opMode,webCamSide);

        addSubComponents(driveTrain,webCamSide);
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

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public WebCam getFrontWebCam() {
        return webCamSide;
    }
}
