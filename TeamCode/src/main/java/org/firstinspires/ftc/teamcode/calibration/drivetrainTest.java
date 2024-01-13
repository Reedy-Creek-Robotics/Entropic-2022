package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class drivetrainTest extends OpMode {
    DriveTrain driveTrain;
    Controller controller;

    boolean relative = false;

    @Override
    public void init() {
        driveTrain = new DriveTrain(BaseComponent.createRobotContext(this));
        controller = new Controller(gamepad1);
    }

    @Override
    public void loop() {
        if (controller.isPressed(Controller.Button.A)){
            relative = !relative;
        }

        if(!relative){
            driveTrain.drive(controller.leftStickY(),controller.leftStickX(), controller.rightStickX(), 0.3);
        } else {
            driveTrain.driverRelative(controller.leftStickY(),controller.leftStickX(), controller.rightStickX(), 0.3);
        }
    }
}
