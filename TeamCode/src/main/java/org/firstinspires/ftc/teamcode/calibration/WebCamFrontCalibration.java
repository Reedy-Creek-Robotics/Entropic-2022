package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.WebCam;

@TeleOp(group = "Calibration")
public class WebCamFrontCalibration extends BaseWebCamCalibration {

    @Override
    protected WebCam getWebCam() {
        return robot.getWebCamFront();
    }

    @Override
    protected Robot.CameraMode getCameraMode() {
        return Robot.CameraMode.ENABLED_AND_STREAMING_FRONT;
    }

}
