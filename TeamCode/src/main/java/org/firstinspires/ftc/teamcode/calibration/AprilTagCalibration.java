package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.openftc.apriltag.AprilTagDetection;

import java.util.Arrays;
import java.util.List;

@TeleOp(group = "Calibration")
public class AprilTagCalibration extends BaseTeleOp {

    @Override
    public void init() {
        super.init();

        // Activate the AprilTag detector
        robot.getAprilTagDetector().activate();
    }

    @Override
    protected List<Camera> getEnabledCameras() {
        return Arrays.asList(Camera.APRIL);
    }

    @Override
    public void loop() {
        List<AprilTagDetection> currentDetections = robot.getAprilTagDetector().getDetections();

        if (!currentDetections.isEmpty()) {
            AprilTagDetection detection = currentDetections.get(0);
            telemetry.addData("Tag", detection.id);
        } else {
            telemetry.addData("No tag", null);
        }

        robot.updateStatus();
    }
}
