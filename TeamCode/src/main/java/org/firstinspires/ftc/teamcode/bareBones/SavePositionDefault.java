package org.firstinspires.ftc.teamcode.bareBones;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;

@Autonomous
public class SavePositionDefault extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();
        robot.setAlliance(RobotContext.Alliance.RED);
        robot.getDriveTrain().getRoadrunner().setPoseEstimate(new Pose2d(0, 0, Math.toRadians(180)));

        waitForStart();

        robot.savePositionToDisk();
    }
}
