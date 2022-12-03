package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;

import java.util.Arrays;
import java.util.List;

@TeleOp
public class MoveCommandTest extends OpMode {
    public static double BASE_SPEED = 1;

    public static double LIMITER = .3;


    public Robot robot;

    private ElapsedTime lastButtonTime;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        lastButtonTime = new ElapsedTime();
    }

    private boolean deadZoneCheck(List<Double> values) {
        for (Double value : values) {
            if (Math.abs(value) > .1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loop() {

        double drive = -gamepad1.left_stick_y;
        double turn = -gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;

        //sets the power to the drivetrain
        if (deadZoneCheck(Arrays.asList(drive, turn, strafe)) || !robot.getDriveTrain().isBusy())
            robot.getDriveTrain().drive(drive, turn, strafe, LIMITER);

        if (lastButtonTime.seconds() > 0.25) {
            boolean buttonPress = true;
            if (gamepad1.dpad_up) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, Y, BASE_SPEED);
            } else if (gamepad1.dpad_left) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, X, BASE_SPEED);
            } else if (gamepad1.dpad_down) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, BASE_SPEED);
            } else if (gamepad1.dpad_right) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, X, BASE_SPEED);
            } else if (gamepad1.x) {
                robot.getDriveTrain().rotate(-90, BASE_SPEED);
            } else if (gamepad1.y) {
                robot.getDriveTrain().rotate(90, BASE_SPEED);
            } else if (gamepad1.b) {
                robot.getDriveTrain().stopAllCommands();
            } else if (gamepad1.start) {
                robot.getDriveTrain().resetPosition();
            } else {
                buttonPress = false;
            }
            if (buttonPress) {
                lastButtonTime.reset();
            }
        }

        robot.updateStatus();

    }
}
