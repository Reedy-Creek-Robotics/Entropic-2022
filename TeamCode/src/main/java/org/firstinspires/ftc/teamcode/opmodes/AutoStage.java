package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoStage extends AutoMain {

    @Override
    protected TrajectorySequence toTileCenter() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(12, 60))
                .build();
    }

    @Override
    public TrajectorySequence toStageTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toSpikeTrajectory(propPosition).end())
                .lineTo(new Vector2d(24, 58))
                .turn(Math.toRadians(-95))
                .lineTo(new Vector2d(60, 47))
                .build();

                //14 in to y
                //12 in to x
                //5-10 deg turn
    }

    @Override
    public TrajectorySequence toParkTrajectory() {
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toParkTrajectory().end())
                .lineTo(new Vector2d(48,60))
                .build();
    }

    @Override
    protected void runPath() {
        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toTileCenter());
        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toSpikeTrajectory(propPosition));

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

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toParkTrajectory());


    }

}
