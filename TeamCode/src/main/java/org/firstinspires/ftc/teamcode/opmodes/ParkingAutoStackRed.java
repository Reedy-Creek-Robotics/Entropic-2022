package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public abstract class ParkingAutoStackRed extends AutoMain {

    protected Pose2d getStartPosition() {
        return null;
    }
    public void runAuto() {


        TrajectorySequence autoPark = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(36,12))
                .lineTo(new Vector2d(12, 12))
                .lineTo(new Vector2d(12, 132))
                .build();

        waitForStart();

        if (!isStopRequested())
            robot.getDriveTrain().roadrunner.followTrajectorySequence(autoPark);
    }

}
