package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoStageBlue extends AutoStage{

    @Override
    protected Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
