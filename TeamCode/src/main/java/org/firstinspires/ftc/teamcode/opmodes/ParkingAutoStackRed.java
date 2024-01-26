package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class ParkingAutoStackRed extends AutoMain {

    protected Pose2d getStartPosition() {
        return new Pose2d();
    }
    public void runAuto() {

        // Updated
        TrajectorySequence autoPark = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(-60,-36))
                .lineTo(new Vector2d(-60, -12))
                .lineTo(new Vector2d(60, -12))
                .build();

        waitForStart();

        if (!isStopRequested())
            robot.getDriveTrain().roadrunner.followTrajectorySequence(autoPark);
    }

}
