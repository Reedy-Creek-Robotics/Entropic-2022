package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous
public class AutoStageBlue extends AutoStage{
    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(24-(robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.width/2), 54 + (robot.getRobotContext().descriptor.ROBOT_DIMENSIONS_IN_INCHES.height/2), Math.toRadians(-90));
    }

    @Override
    protected Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
