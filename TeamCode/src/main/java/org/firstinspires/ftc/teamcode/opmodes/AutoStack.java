package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.components.TeamPropDetector;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoStack extends AutoMain {

    @Override
    protected TrajectorySequence toTileCenter() {
        TrajectorySequence trajectorySequence =  robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(-36, 60 * robot.getRobotContext().getAlliance().getValue()))
                .build();

        return trajectorySequence;
    }

    @Override
    public TrajectorySequence toStageTrajectory() {

        TrajectorySequence trajectorySequence;

        if(propPosition == TeamPropDetector.TeamPropPosition.RIGHT || propPosition == TeamPropDetector.TeamPropPosition.LEFT){
            trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toSpikeTrajectory().end())
                    .back(5)
                    .lineToLinearHeading(new Pose2d(-36,42 * robot.getRobotContext().getAlliance().getValue(),Math.toRadians(robot.getRobotContext().getAlliance().getRotation())))
                    .lineTo(new Vector2d(-36,12 * robot.getRobotContext().getAlliance().getValue()))
                    .lineTo(new Vector2d(24,12 * robot.getRobotContext().getAlliance().getValue()))
                    .lineToLinearHeading(new Pose2d(48,36 * robot.getRobotContext().getAlliance().getValue(),Math.toRadians(180)))
                    .build();
        }else {
            trajectorySequence =  robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toSpikeTrajectory().end())
                    .back(5)
                    .lineToLinearHeading(new Pose2d(-48,54 * robot.getRobotContext().getAlliance().getValue(),Math.toRadians(robot.getRobotContext().getAlliance().getRotation())))
                    .lineToLinearHeading(new Pose2d(-56,48 * robot.getRobotContext().getAlliance().getValue(),Math.toRadians(180)))
                    .lineTo(new Vector2d(-56,12 * robot.getRobotContext().getAlliance().getValue()))
                    .lineTo(new Vector2d(24,12 * robot.getRobotContext().getAlliance().getValue()))
                    .lineTo(new Vector2d(48,36 * robot.getRobotContext().getAlliance().getValue()))
                    .build();
        }

        return trajectorySequence;
    }

    @Override
    public TrajectorySequence toParkTrajectory() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(stageBack().end())
                .lineTo(new Vector2d(48,12 * robot.getRobotContext().getAlliance().getValue()))
                .build();

        return trajectorySequence;
    }

}
