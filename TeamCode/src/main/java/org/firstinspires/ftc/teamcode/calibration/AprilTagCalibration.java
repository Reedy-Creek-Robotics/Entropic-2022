package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.openftc.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp(group = "Calibration")
public class AprilTagCalibration extends OpMode {

    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(this, Robot.CameraMode.ENABLED_AND_STREAMING_FRONT);
        robot.init();

        // Activate the AprilTag detector
        robot.getAprilTagDetector().activate();
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
