package org.firstinspires.ftc.teamcode.bareBones;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Robot;

@Autonomous
public class BareAuto extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        robot = new Robot(this);
        robot.init();

//        Pose2d startPose = new Pose2d(12, -60, Math.toRadians(90));
        //Pose2d startPose = new Pose2d(12, -60, Math.toRadians(90));

        //robot.getDriveTrain().roadrunner.setPoseEstimate(startPose);

        Trajectory trajectory = robot.getDriveTrain().roadrunner.trajectoryBuilder(new Pose2d())
                .forward(24)
                /*.turn(Math.toRadians(75))
                .forward(3)
                .addDisplacementMarker(()->{
                    //robot.getIntake().rollOut(0.1);
                })*/
                .build();

        waitForStart();

        telemetry.clearAll();

        if (isStopRequested()) return;

        robot.getDriveTrain().roadrunner.followTrajectory(trajectory);
        telemetry.addLine("here");

        Pose2d poseEstimate = robot.getDriveTrain().roadrunner.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive());
    }
}
