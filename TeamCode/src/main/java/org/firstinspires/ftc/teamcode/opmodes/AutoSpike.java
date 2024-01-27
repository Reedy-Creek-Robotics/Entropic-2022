package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class AutoSpike extends AutoMain{
    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(12, -60, Math.toRadians(90));
    }

    @Override
    protected void runPath() {

//        telemetry.addData("detection",robot.getTeamPropDetector().waitForDetection(2));

        TrajectorySequence trajectory = spikeTrajectory(getPropRotation())
                .build();

        robot.getDriveTrain().roadrunner.followTrajectorySequence(trajectory);
    }

    @Override
    protected Alliance getAlliance() {
        return null;
    }

}
