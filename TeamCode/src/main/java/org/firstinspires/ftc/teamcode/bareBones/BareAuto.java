package org.firstinspires.ftc.teamcode.bareBones;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class BareAuto extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        robot = new Robot(this);
        robot.init();

//        Pose2d startPose = new Pose2d(12, -60, Math.toRadians(90));
        Pose2d startPose = new Pose2d(12, 60, Math.toRadians(-90));
        robot.getDriveTrain().roadrunner.setPoseEstimate(startPose);

        TrajectorySequence trajectory = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(startPose)
                .forward(24)
                .turn(Math.toRadians(60))
                .forward(5)
                .back(5)
                .splineToLinearHeading(new Pose2d(24,54,Math.toRadians(180)),Math.toRadians(180))
                .lineTo(new Vector2d(48,36))
                .lineTo(new Vector2d(48,60))
                .build();

        waitForStart();

        telemetry.clearAll();

        if (isStopRequested()) return;

        robot.getDriveTrain().roadrunner.followTrajectorySequence(trajectory);
        telemetry.addLine("here");

        Pose2d poseEstimate = robot.getDriveTrain().roadrunner.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive());
    }
}
