package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.components.WebCam;

import java.util.Arrays;
import java.util.List;

@TeleOp(group = "Calibration")
public class WebCamSideCalibration extends BaseWebCamCalibration {

    @Override
    protected WebCam getWebCam() {
        return robot.getWebCamSide();
    }

    @Override
    protected List<Camera> getEnabledCameras() {
        return Arrays.asList(Camera.SIDE);
    }

}
