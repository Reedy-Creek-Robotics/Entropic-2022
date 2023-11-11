package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot.Camera;
import org.firstinspires.ftc.teamcode.components.WebCam;

import java.util.Arrays;
import java.util.List;

@TeleOp(group = "Calibration")
public class WebCamFrontCalibration extends BaseWebCamCalibration {

    @Override
    protected WebCam getWebCam() {
        return robot.getWebCamFront();
    }

    @Override
    protected List<Camera> getEnabledCameras() {
        return Arrays.asList(Camera.FRONT);
    }

}
