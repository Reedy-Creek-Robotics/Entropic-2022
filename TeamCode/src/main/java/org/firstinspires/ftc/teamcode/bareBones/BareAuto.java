package org.firstinspires.ftc.teamcode.bareBones;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class BareAuto extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();

        Pose2d startPose = new Pose2d(12, -60, Math.toRadians(90));

        robot.getDriveTrain().roadrunner.setPoseEstimate(startPose);

        TrajectorySequence trajectory = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(startPose)
                .forward(24)
                /*.turn(Math.toRadians(75))
                .forward(3)
                .addDisplacementMarker(()->{
                    //robot.getIntake().rollOut(0.1);
                })*/
                .build();

        waitForStart();

        if (!isStopRequested()) return;

        robot.getDriveTrain().roadrunner.followTrajectorySequence(trajectory);

        /*Pose2d poseEstimate = robot.getDriveTrain().roadrunner.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();*/

        while (!isStopRequested() && opModeIsActive()){};
    }
}
