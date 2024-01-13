package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.WebCam;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.opmodes.BaseTeleOp;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

@TeleOp(group = "Calibration")
public class WebCamColorCalibration extends BaseTeleOp {

    private WebCam webCam;

    private Position target;

    @Override
    public void init() {
        super.init();

        webCam = robot.getWebCamFront();
        Size resolution = webCam.getResolution();

        target = new Position(resolution.width / 2, resolution.height / 2);

        webCam.setFrameProcessor(new WebCam.FrameProcessor() {

            private Mat hsv = new Mat();

            @Override
            public void processFrame(Mat input, Mat output, WebCam.FrameContext frameContext) {
                // Convert frame to HSV
                Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

                DrawUtil.drawMarker(output, target, Color.GREEN);

                double[] rgbValue = input.get((int) target.getY(), (int) target.getX());
                double[] hsvValue = hsv.get((int) target.getY(), (int) target.getX());

                DrawUtil.drawText(output, "RGB [" + formatColor(rgbValue) + "]", new Position(10, 30), Color.GREEN);
                DrawUtil.drawText(output, "HSV [" + formatColor(hsvValue) + "]", new Position(10, 60), Color.GREEN);
            }
        });
    }

    @Override
    public void loop() {
        double moveSpeed = 1.0;
        target = target.add(new Vector2(-controller.leftStickX(), -controller.leftStickY()).multiply(moveSpeed));

        telemetry.addData("Pixel", target.toString(0));

        robot.updateStatus();
    }

    private String formatColor(double[] color) {
        StringBuilder sb = new StringBuilder();
        for (double value : color) {
            if (sb.length() > 0) sb.append(',');

            String digit = Integer.toHexString((int) value);
            if (digit.length() == 1) {
                digit = "0" + digit;
            }
            sb.append(digit);
        }

        return sb.toString();
    }
}
