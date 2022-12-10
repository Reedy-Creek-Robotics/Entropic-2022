package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.Robot;

@TeleOp
public class MoveCommandTest extends OpMode {
    public static double BASE_SPEED = 1;

    public Robot robot;

    private ElapsedTime lastButtonTime;

    public double limiter;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        limiter = .3;
        lastButtonTime = new ElapsedTime();
    }

    private boolean deadZoneCheck(double... values) {
        for (double value : values) {
            if (Math.abs(value) > .1) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {

        double drive = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        RobotDescriptor robotDescriptor = robot.getRobotContext().robotDescriptor;

        //telemetry.addData("left stick", String.format("%.3f, %.3f", gamepad1.left_stick_x, gamepad1.left_stick_y));
        //telemetry.addData("right stick", String.format("%.3f, %.3f", gamepad1.right_stick_x, gamepad1.right_stick_y));

        if (deadZoneCheck(gamepad1.left_trigger)) {
            if (deadZoneCheck(gamepad1.left_stick_y)) {
                robotDescriptor.rampingUpMaximumSpeedToMotorPowerRatio += gamepad1.left_stick_y / 4;
            } else if (deadZoneCheck(gamepad1.right_stick_y)) {
                robotDescriptor.rampingDownMaximumMotorPowerToDistanceRemainingRatio += gamepad1.right_stick_y / 4;
            }

        } else {
            //sets the power to the drivetrain
            if (deadZoneCheck(drive, turn, strafe) || !robot.getDriveTrain().isBusy())
                robot.getDriveTrain().drive(drive, turn, strafe, limiter);
        }

        if (lastButtonTime.seconds() > 0.25) {
            boolean buttonPress = true;
            if (gamepad1.dpad_up) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
            } else if (gamepad1.dpad_left) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
            } else if (gamepad1.dpad_down) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
            } else if (gamepad1.dpad_right) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
            } else if (gamepad1.x) {
                robot.getDriveTrain().rotate(-90, limiter);
            } else if (gamepad1.y) {
                robot.getDriveTrain().rotate(90, limiter);
            } else if (gamepad1.b) {
                robot.getDriveTrain().stopAllCommands();
            } else if (gamepad1.a) {
                robot.getDriveTrain().centerInCurrentTile(limiter);
            } else if (gamepad1.start) {
                robot.getDriveTrain().resetPosition();
            } else if (gamepad1.left_bumper) {
                limiter -= 0.05;
            } else if (gamepad1.right_bumper) {
                limiter += 0.05;
            } else {
                buttonPress = false;
            }

            if (buttonPress) {
                lastButtonTime.reset();
            }
        }

        telemetry.addData("Limiter", limiter);
        //telemetry.addData("Ramp Up Ratio", robotDescriptor.rampingUpMaximumSpeedToMotorPowerRatio);
        //telemetry.addData("Ramp Down Ratio", robotDescriptor.rampingDownMaximumMotorPowerToDistanceRemainingRatio);
        robot.updateStatus();

    }
}
