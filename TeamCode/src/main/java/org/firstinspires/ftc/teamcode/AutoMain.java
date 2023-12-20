package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.openftc.apriltag.AprilTagDetection;

public abstract class AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = 0.8;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;
    protected AprilTagDetection aprilTagDetection;

    protected boolean usingHough = true;
    protected boolean samProposal = true;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            // Allow the child class to run its auto path.
            runAutoPath();

        } finally {
            // Save the position to disk, so it can be picked up by the TeleOp
            robot.savePositionToDisk();
        }
    }

    protected abstract void runAutoPath();

    protected void initRobot() {

    }

    protected abstract Position getStartPosition();



}
