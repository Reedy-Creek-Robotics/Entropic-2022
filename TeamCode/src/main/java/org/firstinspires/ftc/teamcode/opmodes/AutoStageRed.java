package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoStageRed extends AutoStage{

    @Override
    protected Alliance getAlliance() {
        return Alliance.RED;
    }
}
