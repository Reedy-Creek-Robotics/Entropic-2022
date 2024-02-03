package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoStage extends AutoMain {

    @Override
    protected TrajectorySequence toTileCenter() {
        TrajectorySequence trajectorySequence =  robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(12, 60 * robot.getRobotContext().getAlliance().getValue()))
                .build();

        return trajectorySequence;
    }

    @Override
    public TrajectorySequence toStageTrajectory() {
        TrajectorySequence trajectorySequence =  robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toSpikeTrajectory().end())
                .back(5)
                //.lineToLinearHeading(new Pose2d(24,54,Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(24,54 * robot.getRobotContext().getAlliance().getValue(),Math.toRadians(getAutoAlliance().getRotation())))
                //.turn(Math.toRadians(90))
                .lineToLinearHeading(new Pose2d(48,36 * robot.getRobotContext().getAlliance().getValue(), Math.toRadians(180)))
                .build();

        return trajectorySequence;
    }

    @Override
    public TrajectorySequence toParkTrajectory() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(stageBack().end())
                .lineTo(new Vector2d(48,64 * robot.getRobotContext().getAlliance().getValue()))
                .build();

        return trajectorySequence;
    }
}
