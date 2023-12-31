package org.firstinspires.ftc.teamcode.bareBones;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.ColorDetector;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.WebCam;

import java.util.List;

@TeleOp(group = "Barebone Component Testing")
public class ColorDetectorTester extends OpMode {

    ColorDetector colorDetector;
    WebCam webCam;
    @Override
    public void init() {
        RobotContext context = new RobotContext(this, new RobotDescriptor());
        webCam = new WebCam(context, context.robotDescriptor.webCamFrontDescriptor, true);
        colorDetector = new ColorDetector(context, webCam);
        webCam.init();
        colorDetector.init();
        colorDetector.activate();
    }
    @Override
    public void loop() {
        List<ColorDetector.ColorDetection> detections = colorDetector.getDetections();
        telemetry.addData("Detections", detections.size());
        telemetry.update();
    }
}
