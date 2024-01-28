package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_TRIGGER;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.Outtake;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class IntakeTest extends OpMode {

    Intake intake;
    Outtake outtake;
    Controller controller;
    DriveTrain driveTrain;

    @Override
    public void init() {
        intake = new Intake(BaseComponent.createRobotContext(this));
        outtake = new Outtake(BaseComponent.createRobotContext(this));
        controller = new Controller(gamepad1);
        driveTrain = new DriveTrain(BaseComponent.createRobotContext(this));

    }

    @Override
    public void loop() {
        if (controller.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !driveTrain.isBusy()) {
            double drive = controller.leftStickY();
            double strafe = controller.leftStickX();
            double turn = controller.rightStickX();

            driveTrain.drive(drive, strafe, turn, 0.25);
        }

        // Intake
        if (controller.isPressed(LEFT_TRIGGER)) {
            intake.intakeManual();
        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            intake.outtakeManual();
        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            intake.rollOut(1);
        } else {
            intake.stopIntake();
        }

        telemetry.update();
    }
}
