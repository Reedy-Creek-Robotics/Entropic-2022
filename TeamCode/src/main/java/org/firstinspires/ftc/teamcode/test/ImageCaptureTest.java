package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Heading;

@Disabled
@TeleOp
public class ImageCaptureTest extends OpMode {
    public Robot robot;

    public Controller controller;

    RobotDescriptor robotDescriptor;

    public void init() {
        robot = new Robot(this);
        robot.init();

        controller = new Controller(gamepad1);

        robotDescriptor = robot.getRobotContext().descriptor;
    }

    public void loop() {
        if (controller.isPressed(A)) {
            robot.getWebCamSide().saveLastFrame();
            robot.waitForCommandsToFinish();
        } else if (controller.isPressed(DPAD_UP)) {
            robot.getDriveTrain().moveToHeading(new Heading(45),.3);
        } else if (controller.isPressed(DPAD_LEFT)) {
            robot.getDriveTrain().moveToHeading(new Heading(30),.3);
        }else if(controller.isPressed(DPAD_RIGHT)) {
            robot.getDriveTrain().moveToHeading(new Heading(15),.3);
        }else if(controller.isPressed(DPAD_DOWN)) {
            robot.getDriveTrain().moveToHeading(new Heading(60),.3);
        }
    }
}
