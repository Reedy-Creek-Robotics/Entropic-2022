package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoStage extends AutoMain {


    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(24-(robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.width/2), (54 + (robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.height/2))* getAlliance().value, Math.toRadians(180));
    }

    @Override
    protected TrajectorySequence toTileCenter() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(12, 60 * getAlliance().value))
                .build();
    }

    @Override
    public TrajectorySequence toStageTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toTileCenter().end())
                .lineTo(new Vector2d(50,48* getAlliance().value))
                .lineTo(new Vector2d(50,36* getAlliance().value))
                .build();

                //14 in to y
                //12 in to x
                //5-10 deg turn
    }

    @Override
    public TrajectorySequence toParkTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(stageBack().end())
                .lineTo(new Vector2d(48,64* getAlliance().value))
                .build();
    }

    @Override
    protected void runPath() {
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

        /*robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toSpikeTrajectory(propPosition));

        //push out to spike
        robot.getIntake().rollOut(0.3);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toStageTrajectory());

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.FIRST_LEVEL);
        robot.waitForCommandsToFinish();

        robot.getOuttake().outtakeRight();
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();
        //outtake

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toParkTrajectory());*/


    }

}
