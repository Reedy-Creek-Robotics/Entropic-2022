package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoStack extends AutoMain {
    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(-24-(robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.width/2), (54 + (robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.height/2))* getAlliance().value,Math.toRadians(180));
    }

    @Override
    protected TrajectorySequence toTileCenter() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(-36, 60 * getAlliance().value))
                .build();
    }

    @Override
    public TrajectorySequence toStageTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toTileCenter().end())
                .lineTo(new Vector2d(-36,12 * getAlliance().value))
                .lineTo(new Vector2d(48,12 * getAlliance().value))
                .lineTo(new Vector2d(48,36 * getAlliance().value))
                .build();
    }

    @Override
    public TrajectorySequence toParkTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toStageTrajectory().end())
                .lineTo(new Vector2d(48,12 * getAlliance().value))
                .build();
    }

    @Override
    public void runPath() {
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();
        robot.getSlide().rotate(LinearSlide.RotationPoints.INTAKE);

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toTileCenter());
        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toStageTrajectory());

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.FIRST_LEVEL);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(stageLineUp());

        robot.getOuttake().outtakeBoth();
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(stageBack());

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toParkTrajectory());
    }

}
