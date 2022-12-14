package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.START;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Position;

@TeleOp
@Disabled
public class MoveCommandTest extends OpMode {

    private Robot robot;

    private Controller controller;

    private double limiter;


    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        controller = new Controller(gamepad1);

        limiter = 0.3;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {

        RobotDescriptor robotDescriptor = robot.getRobotContext().robotDescriptor;

        double drive = controller.leftStickY();
        double strafe = controller.leftStickX();
        double turn = controller.rightStickX();

        if (nonZero(drive, turn, strafe) || !robot.getDriveTrain().isBusy()) {
            robot.getDriveTrain().driverRelative(drive, turn, strafe, limiter);
        }

        if (controller.isPressed(DPAD_UP)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
        } else if (controller.isPressed(DPAD_LEFT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
        } else if (controller.isPressed(DPAD_DOWN)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
        } else if (controller.isPressed(DPAD_RIGHT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
        } else if (controller.isPressed(Controller.Button.X)) {
            robot.getDriveTrain().rotate(-90, limiter);
        } else if (controller.isPressed(Controller.Button.Y)) {
            robot.getDriveTrain().rotate(90, limiter);
        } else if (controller.isPressed(B)) {
            robot.getDriveTrain().stopAllCommands();
        } else if (controller.isPressed(A)) {
            robot.getDriveTrain().centerInCurrentTile(limiter);
        } else if (controller.isPressed(START)) {
            robot.getDriveTrain().setPosition(new Position(.5,.5));
        } else if (controller.isPressed(LEFT_BUMPER)) {
            limiter -= 0.05;
        } else if (controller.isPressed(RIGHT_BUMPER)) {
            limiter += 0.05;
        }

        telemetry.addData("Limiter", limiter);
        //telemetry.addData("Ramp Up Min Power", robotDescriptor.rampingUpMinMotorPower);
        //telemetry.addData("Ramp Up End Speed", robotDescriptor.rampingUpEndSpeed);
        //telemetry.addData("Ramp Down Min Power", robotDescriptor.rampingDownMinMotorPower);
        //telemetry.addData("Ramp Down Begin Distance", robotDescriptor.rampingDownBeginDistance);
        //telemetry.addData("Ramp Min Turn Power", robotDescriptor.rampingMinTurnPower);
        //telemetry.addData("Ramp Max Turn Power", robotDescriptor.rampingMaxTurnPower);
        //telemetry.addData("Ramp Max Turn Degrees", robotDescriptor.rampingMaxTurnDegrees);
        //telemetry.addData("Ramp Turn Exponent", robotDescriptor.rampingTurnExponent);

        robot.updateStatus();

    }

    private static boolean nonZero(double... values) {
        for (double value : values) {
            if (value != 0.0) {
                return true;
            }
        }
        return false;
    }

}
