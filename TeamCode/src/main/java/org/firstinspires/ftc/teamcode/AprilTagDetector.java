package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.openftc.apriltag.AprilTagDetection;

import java.util.List;

public class AprilTagDetector extends BaseComponent {

    /**
     * The webcam to use for detection
     */
    private WebCam webCam;

    // UNITS ARE METERS
    private double tagsize = 0.166;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    private double fx = 578.272;
    private double fy = 578.272;
    private double cx = 402.145;
    private double cy = 221.506;
    private AprilTagDetectionFrameProcessor frameProcessor;

    public AprilTagDetector(OpMode opMode, WebCam webCam) {
        super(opMode);
        this.webCam = webCam;
    }

    /**
     * Sets the detection parameters.  This should be called prior to calling activate.
     */
    public void setDetectionParameters(double tagsize, double fx, double fy, double cx, double cy) {
        this.tagsize = tagsize;
        this.fx = fx;
        this.fy = fy;
        this.cx = cx;
        this.cy = cy;
    }

    public void setDecimation(float decimation) {
        frameProcessor.setDecimation(decimation);
    }

    public void activate() {
        frameProcessor = new AprilTagDetectionFrameProcessor(tagsize, fx, fy, cx, cy);
        webCam.setFrameProcessor(frameProcessor);
    }

    public List<AprilTagDetection> getDetections() {
        return frameProcessor.getDetections();
    }
}
