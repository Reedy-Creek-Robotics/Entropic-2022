package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.components.Robot.CameraMode.ENABLED_AND_STREAMING_SIDE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.TileEdgeDetector;
import org.firstinspires.ftc.teamcode.util.HoughLineDetector;

@TeleOp(group = "Calibration")
public class RampingCalibration extends OpMode {

    private Robot robot;
    private Controller controller;

    private double limiter;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        limiter = .3;

        controller = new Controller(gamepad1);
    }

    @Override
    public void loop() {

        //Moving the robot
        if (controller.isPressed(DPAD_UP)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
        } else if (controller.isPressed(DPAD_LEFT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
        } else if (controller.isPressed(DPAD_DOWN)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
        } else if (controller.isPressed(DPAD_RIGHT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
        } else if (controller.isPressed(RIGHT_BUMPER) && limiter < 1) {
            limiter += 0.05;
        } else if (controller.isPressed(LEFT_BUMPER) && limiter > 0) {
            limiter -= 0.05;
        }

        //changing ramping values
        if(Controller.nonZero(controller.leftStickY())) {
            robot.getRobotContext().robotDescriptor.rampingUpMinMotorPower += controller.leftStickY() * 0.01;
        }

        robot.updateStatus();
    }
}
