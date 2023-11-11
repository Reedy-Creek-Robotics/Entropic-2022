package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseDrivingTeleOp;
import org.firstinspires.ftc.teamcode.components.TileEdgeDetector;
import org.firstinspires.ftc.teamcode.util.HoughLineDetector;

@TeleOp(group = "Calibration")
public class HoughTransformCalibration extends BaseDrivingTeleOp {

    @Override
    public void loop() {

        applyBasicDriving();

        TileEdgeDetector tileEdgeDetector = robot.getDriveTrain().getTileEdgeDetectorSide();
        HoughLineDetector.HoughParameters parametersVertical = tileEdgeDetector.getHoughLineDetectorVertical().getParameters();
        HoughLineDetector.HoughParameters parametersHorizontal = tileEdgeDetector.getHoughLineDetectorHorizontal().getParameters();

        if (controller.isButtonDown(RIGHT_BUMPER)) {
            if (controller.isPressed(DPAD_UP)) {
                parametersVertical.similarLineRhoThreshold += .05;
            } else if (controller.isPressed(DPAD_DOWN)) {
                parametersVertical.similarLineRhoThreshold -= .05;
            } else if (controller.isPressed(DPAD_RIGHT)) {
                parametersVertical.similarLineThetaThreshold += 1;
            } else if (controller.isPressed(DPAD_LEFT)) {
                parametersVertical.similarLineThetaThreshold -= 1;
            }

        } else if (controller.isButtonDown(LEFT_BUMPER)) {
            if (controller.isPressed(DPAD_UP)) {
                parametersHorizontal.similarLineRhoThreshold += .05;
            } else if (controller.isPressed(DPAD_DOWN)) {
                parametersHorizontal.similarLineRhoThreshold -= .05;
            } else if (controller.isPressed(DPAD_RIGHT)) {
                parametersHorizontal.similarLineThetaThreshold += 1;
            } else if (controller.isPressed(DPAD_LEFT)) {
                parametersHorizontal.similarLineThetaThreshold -= 1;
            }

        } else if (controller.isPressed(LEFT_TRIGGER)) {
            // Adjust horizontal voter pixels
            if (controller.isPressed(DPAD_DOWN)) {
                parametersHorizontal.pixelVoterThreshold -= 5;
            } else if (controller.isPressed(DPAD_UP)) {
                parametersHorizontal.pixelVoterThreshold += 5;
            }

        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            // Adjust vertical voter pixels
            if (controller.isPressed(DPAD_DOWN)) {
                parametersVertical.pixelVoterThreshold -= 5;
            } else if (controller.isPressed(DPAD_UP)) {
                parametersVertical.pixelVoterThreshold += 5;
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
        telemetry.addData("Horizontal Similar Line Rho Threshold", format(parametersHorizontal.similarLineRhoThreshold, 1));
        telemetry.addData("Horizontal Similar Line Theta Threshold", format(parametersHorizontal.similarLineThetaThreshold, 2));
        telemetry.addData("Vertical Similar Line Rho Threshold", format(parametersVertical.similarLineRhoThreshold, 1));
        telemetry.addData("Vertical Similar Line Theta Threshold", format(parametersVertical.similarLineThetaThreshold, 2));

        telemetry.addData("Horizontal Pixel Voters", tileEdgeDetector.getHoughLineDetectorHorizontal().getParameters().pixelVoterThreshold);
        telemetry.addData("Vertical Pixel Voters", tileEdgeDetector.getHoughLineDetectorVertical().getParameters().pixelVoterThreshold);

        robot.updateStatus();
    }
}
