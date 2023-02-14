package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.geometry.PoleDistanceSolver.PoleContour;
import org.firstinspires.ftc.teamcode.geometry.PoleDistanceSolver.PoleDetection;

import java.util.Arrays;
import java.util.List;

@TeleOp(group = "Calibration")
public class PoleDetectionCalibration extends BaseTeleOp {

    private Controller deliverer;

    @Override
    public void init() {
        super.init();

        deliverer = new Controller(gamepad2);

        // Activate the AprilTag detector
        robot.getPoleDetector().activate();
    }

    @Override
    protected List<Camera> getEnabledCameras() {
        return Arrays.asList(Camera.APRIL);
    }

    @Override
    public void loop() {

        if(controller.isPressed(Controller.Button.A)) {
            robot.getWebCamAprilTag().saveLastFrame();
            robot.waitForCommandsToFinish();
        }

        PoleDetection detection = robot.getPoleDetector().getDetection();
        if (detection != null) {
            PoleContour contour = detection.observedContour;
            telemetry.addData("Pole Center:", contour.centroid);
            telemetry.addData("Pole Average Width:", contour.averageWidth);
            telemetry.addData("Pole Distance:", detection.distance);
        }

        robot.updateStatus();
    }
}
