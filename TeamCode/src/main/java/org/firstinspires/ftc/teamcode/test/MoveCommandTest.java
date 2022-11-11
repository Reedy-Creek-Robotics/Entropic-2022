package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.DriveTrain.Direction.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp
public class MoveCommandTest extends OpMode {
    public static double BASE_SPEED = 1;

    public Robot robot;

    private ElapsedTime commandMove;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        commandMove = new ElapsedTime();
    }

    @Override
    public void loop() {

        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;

        //sets the power to the drivetrain
        robot.getDriveTrain().drive(drive, turn, strafe);

        if(commandMove.seconds() > .5) {
            if (gamepad1.dpad_up) {
                robot.getDriveTrain().move(1, y, BASE_SPEED);
                robot.waitForCommandsToFinish();
            } else if (gamepad1.dpad_left) {
                robot.getDriveTrain().move(1, x, BASE_SPEED);
                robot.waitForCommandsToFinish();
            } else if (gamepad1.dpad_down) {
                robot.getDriveTrain().move(-1, y, BASE_SPEED);
                robot.waitForCommandsToFinish();
            } else if (gamepad1.dpad_right) {
                robot.getDriveTrain().move(-1, x, BASE_SPEED);
                robot.waitForCommandsToFinish();
            }else if(gamepad1.a) {
                robot.getDriveTrain().rotate(90, BASE_SPEED);
                robot.waitForCommandsToFinish();
            }else if(gamepad1.b) {
                robot.getDriveTrain().rotate(-90, BASE_SPEED);
                robot.waitForCommandsToFinish();
            }
        }

    }
}
