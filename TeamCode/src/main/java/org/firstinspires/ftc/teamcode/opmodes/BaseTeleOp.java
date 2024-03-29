package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.game.Controller;

import java.util.Arrays;
import java.util.List;

public abstract class BaseTeleOp extends OpMode {

    protected Robot robot;
    protected RobotDescriptor descriptor;

    protected Controller controller;

    @Override
    public void init() {
        robot = new Robot(this, getStreamingCamera(), getEnabledCameras());
        robot.init();

        descriptor = robot.getRobotContext().descriptor;

        controller = new Controller(gamepad1);
    }

    protected Camera getStreamingCamera() {
        List<Camera> enabledCameras = getEnabledCameras();
        return !enabledCameras.isEmpty() ?
                enabledCameras.get(0) :
                null;
    }

    protected List<Camera> getEnabledCameras() {
        return Arrays.asList(
                Camera.FRONT
        );
    }

}
