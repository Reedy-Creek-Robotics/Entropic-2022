package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class RoadRunnerTest extends LinearOpMode {
    DriveTrain driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(BaseComponent.createRobotContext(this));

        Pose2d startPose = new Pose2d(-36, 60, Math.toRadians(270));

        driveTrain.roadrunner.setPoseEstimate(startPose);

        TrajectorySequence trajectory = driveTrain.roadrunner.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-54,12),Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-36,60),Math.toRadians(90))
                //.splineToLinearHeading(new Pose2d(0,0),Math.toRadians(0))
                .build();

        waitForStart();


        if (!isStopRequested())
            driveTrain.roadrunner.followTrajectorySequence(trajectory);

    }
}
