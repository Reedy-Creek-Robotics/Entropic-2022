package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoStackRed extends AutoStack{
    @Override
    protected Alliance getAlliance() {
        return Alliance.RED;
    }
}
