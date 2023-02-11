package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseDrivingTeleOp;
import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.components.PoleDetector;
import org.firstinspires.ftc.teamcode.components.PoleDetector.PoleDetectionParameters;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver.PoleContour;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver.PoleDetection;
import org.opencv.core.Scalar;
import org.openftc.apriltag.AprilTagDetection;

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

        PoleDetection detection = robot.getPoleDetector().getDetection();
        PoleContour contour = detection.observedContour;

        telemetry.addData("Pole Center:", contour.centroid);
        telemetry.addData("Pole Average Width:", contour.averageWidth);
        telemetry.addData("Pole Distance:", detection.distance);

        robot.updateStatus();
    }
}
