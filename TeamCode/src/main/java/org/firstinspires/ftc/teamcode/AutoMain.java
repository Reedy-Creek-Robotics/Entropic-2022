package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.openftc.apriltag.AprilTagDetection;

public abstract class AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = 1;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;
    protected AprilTagDetection aprilTagDetection;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        aprilTagDetection = robot.getAprilTagDetector().waitForDetection(2);
        telemetry.log().add("Detected Tag: " + (aprilTagDetection != null ? aprilTagDetection.id : null));
        telemetry.update();

        robot.getAprilTagDetector().deactivate();

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRAVEL);
        robot.waitForCommandsToFinish();
        robot.getTurret().moveToOrientation(Turret.Orientation.FRONT);
        robot.waitForCommandsToFinish();

        // Allow the child class to run its auto path.
        runAutoPath();

        // Save the position to disk, so it can be picked up by the teleop
        robot.savePositionToDisk();
    }

    protected abstract void runAutoPath();

    protected void initRobot() {
        robot = new Robot(this);
        robot.init();
        robotDescriptor = robot.getRobotContext().robotDescriptor;

        // For auto paths, don't use tile edge detection except at key points
        robot.getDriveTrain().getTileEdgeDetectorSide().deactivate();

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

    public enum PoleStackOffset {
        FIRST_CONE(0),
        SECOND_CONE(20),
        THIRD_CONE(40),
        FOURTH_CONE(60),
        FIFTH_CONE(80);

        int ticks;

        PoleStackOffset(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }

        public static PoleStackOffset forConesRemaining(int conesRemaining) {
            if (conesRemaining < 1 || conesRemaining > 5) {
                throw new IllegalArgumentException(String.valueOf(conesRemaining));
            }
            return PoleStackOffset.values()[conesRemaining - 1];
        }
    }

}
