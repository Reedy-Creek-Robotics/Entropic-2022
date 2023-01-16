package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.openftc.apriltag.AprilTagDetection;

import java.util.Arrays;

public abstract class AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = .7;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;
    protected AprilTagDetection aprilTagDetection;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            aprilTagDetection = robot.getAprilTagDetector().waitForDetection(2);
            telemetry.log().add("Detected Tag: " + (aprilTagDetection != null ? aprilTagDetection.id : null));

            robot.getAprilTagDetector().deactivate();
            robot.getWebCamAprilTag().stop();

            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRAVEL);
            robot.waitForCommandsToFinish();
            robot.getTurret().moveToOrientation(Turret.Orientation.FRONT);
            robot.waitForCommandsToFinish();

            // Allow the child class to run its auto path.
            runAutoPath();

        } finally {
            // Save the position to disk, so it can be picked up by the TeleOp
            robot.savePositionToDisk();
        }
    }

    protected abstract void runAutoPath();

    protected void initRobot() {
        robot = new Robot(this, null,
                Arrays.asList(Camera.APRIL, Camera.FRONT, Camera.SIDE));
        robot.init();
        robotDescriptor = robot.getRobotContext().robotDescriptor;

        // For auto paths, don't use tile edge detection except at key points
        robot.getDriveTrain().deactivateTileEdgeDetection();

        //robot.getWebCamFront().waitUntilReady();
        robot.getAprilTagDetector().activate();

        robot.getDriveTrain().setPosition(getStartPosition());
    }

    protected abstract Position getStartPosition();

    protected int getAprilTagPosition() {
        return aprilTagDetection != null ? aprilTagDetection.id : 2;
    }

    public enum Pole {
        HIGH,
        MEDIUM,
        LOW,
        GROUND
    }

}
