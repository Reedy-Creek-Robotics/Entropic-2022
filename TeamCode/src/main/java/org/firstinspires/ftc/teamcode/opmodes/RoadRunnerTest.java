package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;

public class RoadRunnerTest extends LinearOpMode {
    DriveTrain driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(BaseComponent.createRobotContext(this));

        Trajectory trajectory = driveTrain.roadrunner.trajectoryBuilder(new Pose2d(0,0))
                .lineTo(new Vector2d(-6,-6))
                .lineTo(new Vector2d(-6,6))
                .lineTo(new Vector2d(6,6))
                .lineTo(new Vector2d(6,-6))
                .lineToSplineHeading(new Pose2d(-6,6,Math.toRadians(90)))
                .build();

        waitForStart();

    }
}
