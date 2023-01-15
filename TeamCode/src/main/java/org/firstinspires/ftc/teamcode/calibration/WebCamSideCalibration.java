package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.WebCam;

@TeleOp(group = "Calibration")
public class WebCamSideCalibration extends BaseWebCamCalibration {

    @Override
    protected WebCam getWebCam() {
        return robot.getWebCamSide();
    }

    @Override
    protected Robot.CameraMode getCameraMode() {
        return Robot.CameraMode.ENABLED_AND_STREAMING_SIDE;
    }

}
