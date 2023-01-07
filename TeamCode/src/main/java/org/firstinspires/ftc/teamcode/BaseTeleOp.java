package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.Robot;

public abstract class BaseTeleOp extends OpMode {

    protected Robot robot;
    protected RobotDescriptor descriptor;

    protected Controller controller;

    @Override
    public void init() {
        robot = new Robot(this, getCameraMode());
        robot.init();

        descriptor = robot.getRobotContext().robotDescriptor;

        controller = new Controller(gamepad1);
    }

    protected Robot.CameraMode getCameraMode() {
        return Robot.CameraMode.ENABLED;
    }

}
