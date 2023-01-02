package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.Button.*;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.TileEdgeDetector;
import org.firstinspires.ftc.teamcode.util.HoughLineDetector;

@TeleOp
public class HoughTransformCalibration extends OpMode {

    private Robot robot;
    private Controller controller;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        controller = new Controller(gamepad1);
    }

    @Override
    public void loop() {

        TileEdgeDetector tileEdgeDetector = robot.getDriveTrain().getTileEdgeDetectorSide();

        if (controller.leftTrigger() > 0) {
            // Adjust horizontal voter pixels
            HoughLineDetector.HoughParameters parameters = tileEdgeDetector.getHoughLineDetectorHorizontal().getParameters();
            if (controller.isPressed(DPAD_DOWN)) {
                parameters.pixelVoterThreshold -= 5;
            } else if (controller.isPressed(DPAD_UP)) {
                parameters.pixelVoterThreshold += 5;
            }

        } else if (controller.rightTrigger() > 0) {
            // Adjust vertical voter pixels
            HoughLineDetector.HoughParameters parameters = tileEdgeDetector.getHoughLineDetectorVertical().getParameters();
            if (controller.isPressed(DPAD_DOWN)) {
                parameters.pixelVoterThreshold -= 5;
            } else if (controller.isPressed(DPAD_UP)) {
                parameters.pixelVoterThreshold += 5;
            }

        } else {
            if (controller.isPressed(DPAD_DOWN)) {
                robot.getWebCamSide().setExposure(robot.getWebCamSide().getExposure() - 1);
            } else if (controller.isPressed(DPAD_UP)) {
                robot.getWebCamSide().setExposure(robot.getWebCamSide().getExposure() + 1);
            } else if (controller.isPressed(B)) {
                robot.getWebCamSide().saveLastFrame();
            }
        }

        telemetry.addData("Exposure (ms)", robot.getWebCamSide().getExposure());
        telemetry.addData("Horizontal Pixel Voters", tileEdgeDetector.getHoughLineDetectorHorizontal().getParameters().pixelVoterThreshold);
        telemetry.addData("Vertical Pixel Voters", tileEdgeDetector.getHoughLineDetectorVertical().getParameters().pixelVoterThreshold);

        robot.updateStatus();
    }
}
