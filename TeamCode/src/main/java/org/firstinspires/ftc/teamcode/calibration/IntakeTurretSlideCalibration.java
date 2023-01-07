package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.BACK;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.Controller.Button.Y;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.LEFT_SIDE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.components.Robot;

@TeleOp(group = "Calibration")
public class IntakeTurretSlideCalibration extends OpMode {

    private Robot robot;
    private Controller controller;

    private int slideTicks;
    private double turretPosition;

    private double intakeTime;
    private double intakePower;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        controller = new Controller(gamepad1);

        slideTicks = 0;
        turretPosition = LEFT_SIDE.getServoPosition();

        intakeTime = 1.0;
        intakePower = 1.0;
    }

    @Override
    public void loop() {


        if (controller.isPressed(LEFT_STICK_X)) {
            slideTicks += controller.leftStickY() * 5;
        }

        if (controller.isPressed(RIGHT_STICK_Y)) {
            turretPosition += controller.rightStickY() * .01;
        }

        if (controller.isPressed(DPAD_UP)) {
            intakeTime += .05;
        } else if (controller.isPressed(DPAD_DOWN)) {
            intakeTime -= .05;
        } else if (controller.isPressed(DPAD_RIGHT)) {
            intakePower += .05;
        } else if (controller.isPressed(DPAD_LEFT)) {
            intakePower -= .05;
        }

        if (controller.isPressed(A)) {
            robot.getIntake().intake(intakePower, intakeTime);
        } else if (controller.isPressed(B)) {
            robot.getIntake().outtake(intakePower, intakeTime);
        }

        if (controller.isPressed(X)) {
            robot.getTurret().moveToPosition(turretPosition);
        }
        if (controller.isPressed(Y)) {
            robot.getSlide().moveToTicks(slideTicks);
        }

        if (controller.isPressed(BACK)) {
            robot.getSlide().stopAllCommands();
            robot.getTurret().stopTurret();
            robot.getIntake().stopIntake();
        }

        telemetry.addData("Slide Ticks", slideTicks);
        telemetry.addData("Target Turret Position", turretPosition);
        telemetry.addData("Turret Position", robot.getTurret().getTargetPosition());

        telemetry.addData("Intake Power", intakePower);
        telemetry.addData("Intake Time", intakeTime);

        robot.updateStatus();
    }
}
