package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.AprilTagDetector;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.openftc.apriltag.AprilTagDetection;

import java.util.List;

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
        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;

        //sets the power to the drivetrain
        robot.getDriveTrain().drive(drive, turn, strafe);

        List<AprilTagDetection> currentDetections = robot.getAprilTagDetector().getDetections();

        if (currentDetections.size() != 0) {
            AprilTagDetection tagOfInterest = currentDetections.get(0);
            telemetry.addData("Tag", tagOfInterest.id);
        } else {
            telemetry.addData("No tag", null);
        }
        telemetry.update();
    }
}
