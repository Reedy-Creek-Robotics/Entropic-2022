package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    private int coneCount = 4;

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        deliverer.analogConfig(LEFT_STICK_Y)
                .withMaxValue(robot.getSlide().getManualPower());
    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

        // Intake

        // Lift

        robot.updateStatus();
    }


}
