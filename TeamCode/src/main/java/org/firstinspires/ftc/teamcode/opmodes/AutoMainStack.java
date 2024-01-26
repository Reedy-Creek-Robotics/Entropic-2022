package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public abstract class AutoMainStack extends AutoMain {

    @Override
    protected Pose2d getStartPosition() {
        return null;
    }

    @Override
    public void runAuto() {


        TrajectorySequence autoStack = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(getStartPosition())
                .splineTo(new Vector2d(-36, -36), Math.toRadians(getPropRotation()))
                .forward(2)
                .addDisplacementMarker(() -> {
                    robot.getIntake().autoOuttakeManual();
                    try {
                        wait(1);
                        robot.getOuttake().stopAllCommands();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .back(2)
                .splineTo(new Vector2d(48, 12), Math.toRadians(180))
                .lineTo(new Vector2d(36, 10))
                .addDisplacementMarker(() -> {
                    robot.getIntake().intakeManual();
                    try {
                        wait(1);
                        robot.getIntake().stopIntake();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .lineTo(new Vector2d(12, 12))
                .lineTo(new Vector2d(12, 96))
                .lineTo(new Vector2d(36, 96))
                .lineTo(new Vector2d(36, 108))
                .addDisplacementMarker(() -> {
                    //TODO:Do the April Tag thing
                })
                .lineTo(new Vector2d(12, 96))
                .lineTo(new Vector2d(12,108))
                .build();

        waitForStart();

        if (!isStopRequested())
            robot.getDriveTrain().roadrunner.followTrajectorySequence(autoStack);
    }

}
