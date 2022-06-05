package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition.LEFT;
import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition.MIDDLE;
import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition.RIGHT;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedHashMap;
import java.util.Map;

public class BarCodePositionDetector extends BaseComponent {

    public enum BarCodePosition {
        LEFT,
        MIDDLE,
        RIGHT;
    }

    private WebCam webCam;

    private Map<BarCodePosition, QRCodeDetector> detectors;

    private BarCodePosition defaultIfNotDetected;

    private BarCodePosition detectedPosition;

    public BarCodePositionDetector(OpMode opMode, WebCam webCam) {
        super(opMode);
        this.webCam = webCam;
    }

    /**
     * Sets the center point in the image for each of the possible bar code positions.
     *
     * This must be invoked prior to robot.init()
     */
    void setBarCodePositions(
            Point leftPosition,
            Point middlePosition,
            Point rightPosition,
            BarCodePosition defaultIfNotDetected
    ) {
        int size = 200;

        detectors = new LinkedHashMap<>();
        if (leftPosition != null) detectors.put(LEFT, new QRCodeDetector(leftPosition, size));
        if (middlePosition != null) detectors.put(MIDDLE, new QRCodeDetector(middlePosition, size));
        if (rightPosition != null) detectors.put(RIGHT, new QRCodeDetector(rightPosition, size));
        this.defaultIfNotDetected = defaultIfNotDetected;
    }

    /**
     * Resets the detectors so it can detect a new position.
     */
    public void reset() {
        detectedPosition = null;
        for (QRCodeDetector detector : detectors.values()) {
            detector.reset();
        }
    }

    /**
     * Detects the barcode position and returns it.
     *
     * If the barcode has already been detected (detection starts with the init method), then this
     * method will immediately return the position.
     *
     * If the barcode detection has not completed, this method will block and wait until it is
     * complete and then return it.
     */
    public BarCodePosition detect() {
        ElapsedTime time = new ElapsedTime();
        double maxTime = 5; // seconds

        while (!isFinished() && time.seconds() < maxTime && !isStopRequested()) {
            sleep(100);
        }

        if (detectedPosition == null) {
            detectedPosition = defaultIfNotDetected;
        }

        return getDetectedPosition();
    }

    /**
     * Initializes the barcode detection.
     *
     * Detection will automatically begin when this method is called.
     */
    @Override
    public void init() {
        assert detectors != null : "setBarCodePositions should have been called before init()";

        webCam.setFrameProcessor(new FrameProcessor());
    }

    /**
     * @return where the QR code was detected
     */
    public BarCodePosition getDetectedPosition() {
        return detectedPosition;
    }

    public boolean isFinished() {
        return detectedPosition != null;
    }

    private class FrameProcessor implements WebCam.FrameProcessor {
        @SuppressLint("DefaultLocale")
        @Override
        public void processFrame(Mat input, Mat output) {
            //for every QR code detector
            for (Map.Entry<BarCodePosition, QRCodeDetector> entry : detectors.entrySet()) {
                BarCodePosition position = entry.getKey();
                QRCodeDetector detector = entry.getValue();

                // If detection hasn't already completed, attempt to detect with each detector.
                if (!isFinished()) {
                    detector.processFrame(input);

                    if (detector.isDetected()) {
                        detectedPosition = position;
                    }
                }

                //makes the box on screen turn to green as it detects
                Scalar color;
                if (detector.isDetected()) {
                    color = Color.GREEN.toRGBA();
                } else if (detector.getDetectionCount() > 0) {
                    color = Color.ORANGE.toRGBA();
                } else {
                    color = Color.RED.toRGBA();
                }

                // Add the number of detections in text on the screen
                DrawUtil.drawText(output, Integer.toString(detector.getDetectionCount()), detector.getCenter(), color);

                // Add the bounding rectangle for the detector
                Imgproc.rectangle(output, detector.getDetectionBounds(), color, 3);

            }
        }
    }

}
