package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.Button.A;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class PictureOp extends BaseDrivingTeleOp {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        applyBasicDriving();

        if(controller.isPressed(A)) {
            robot.getWebCamAprilTag().saveLastFrame();
            robot.waitForCommandsToFinish();
        }
    }
}
