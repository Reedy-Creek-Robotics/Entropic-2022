package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.Button.A;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;

import java.util.Arrays;
import java.util.List;

@TeleOp
public class PictureOp extends BaseDrivingTeleOp {
    @Override
    public void init() {
        super.init();
    }

    @Override
    protected List<Robot.Camera> getEnabledCameras() {
        return Arrays.asList(Robot.Camera.APRIL);
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
