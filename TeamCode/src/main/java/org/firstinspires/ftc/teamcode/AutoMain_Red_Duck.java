package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoMain_Red_Duck extends AutoMainDuck {

    @Override
    StartPosition getStartPosition() {
        return StartPosition.RED_DUCK;
    }

}
