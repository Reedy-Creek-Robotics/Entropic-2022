package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.AprilTagDetector;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.openftc.apriltag.AprilTagDetection;

import java.util.List;

@Disabled
@TeleOp
public class AprilTagTest extends OpMode {
    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        // Activate the AprilTag detector
        // todo: have a way to deactivate this as well
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
        telemetry.update();

        robot.updateStatus();
    }
}
