package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotContext;

@Autonomous
public class AutoStageRed extends AutoStage{

    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(0+(10), (72 - (9.5))* robot.getRobotContext().getAlliance().getValue(), Math.toRadians(getAutoAlliance().getRotation()));
    }

    @Override
    protected RobotContext.Alliance getAutoAlliance() {
        return RobotContext.Alliance.RED;
    }
}
