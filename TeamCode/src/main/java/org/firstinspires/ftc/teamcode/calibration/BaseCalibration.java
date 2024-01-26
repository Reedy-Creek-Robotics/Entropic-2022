package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.game.Controller;

public abstract class BaseCalibration extends OpMode {
    Controller controller;
    int state = 0;

    @Override
    public void init() {
        controller =  new Controller(gamepad1);
        switchToBarebone();
    }

    @Override
    public void loop() {

        if (controller.isPressed(Controller.Button.TRIANGLE)) {
            state = 0;
            switchToBarebone();
        }else if (controller.isPressed(Controller.Button.CIRCLE)) {
            state = 1;
            switchToComponent();
        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            state = 2;
            switchToComponent();
        }

        switch (state){
            case 0:
                hardwareTesting();
                break;
            case 1:
                calibration();
                break;
            case 2:
                confirmation();
                break;
        }

        telemetry.addData("Triangle", "Hardware Tester");
        telemetry.addData("Circle", "Calibrator");
        telemetry.addData("Square", "Confirmer");
    }

    protected abstract void switchToBarebone();
    protected abstract void hardwareTesting();

    protected abstract void switchToComponent();
    protected abstract void calibration();

    protected abstract void confirmation();
}
