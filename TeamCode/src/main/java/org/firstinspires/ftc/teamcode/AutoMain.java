package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.components.TeamPropDetector;
import org.firstinspires.ftc.teamcode.geometry.Position;

import java.util.Arrays;

public abstract class AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = 0.8;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;

    protected TeamPropDetector.TeamPropPosition teamPropPosition;

    protected RobotDescriptor.RampingDescriptor exactRampingDescriptor = new RobotDescriptor.RampingDescriptor(
            0, 45,1,.05
    );

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            robot.getTeamPropDetector().activate();

            teamPropPosition = robot.getTeamPropDetector().waitForDetection(2);
            telemetry.log().add("Detected Team Prop: " + teamPropPosition);

            robot.getTeamPropDetector().deactivate();

            // Allow the child class to run its auto path.
            runAutoPath();

        } finally {
            // Save the position to disk, so it can be picked up by the TeleOp
            robot.savePositionToDisk();
        }
    }

    protected abstract void runAutoPath();

    protected void initRobot() {
        robot = new Robot(this, Camera.FRONT, Arrays.asList(Camera.FRONT));
        robot.init();

        robotDescriptor = robot.getRobotContext().robotDescriptor;

        robot.getTeamPropDetector().activate();

        robot.getDriveTrain().setPosition(getStartPosition());
    }

    protected abstract Position getStartPosition();

    public enum Pole {
        HIGH,
        MEDIUM,
        LOW,
        GROUND
    }

}
