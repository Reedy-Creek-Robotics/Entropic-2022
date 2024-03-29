package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public abstract class AutoSpike extends AutoMain{
    @Override
    protected Pose2d getStartPosition() {
        return new Pose2d(12, -60, Math.toRadians(90));
    }

    @Override
    protected void runPath() {

    }

    @Override
    protected Alliance getAlliance() {
        return null;
    }

}
