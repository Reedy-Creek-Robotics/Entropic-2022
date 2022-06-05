package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.util.RectUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public class QRCodeDetector {
    private static final int THRESHOLD = 1;

    private int detectionCount;
    private int detectionAttempts;
    private Rect detectionBounds;
    private Point center;

    private org.opencv.objdetect.QRCodeDetector detector = new org.opencv.objdetect.QRCodeDetector();

    //Initializes a QR code detector.
    public QRCodeDetector(Point center, int size) {
        this.center = center;
        //creates a rectangle of the given size with its center at the given point
        this.detectionBounds = new Rect(
            (int) (center.x - size / 2),
            (int) center.y - size / 2,
            size, size
        );
        reset();
    }

    public void reset() {
        //resets the QR code detector so it can be used again.
        detectionCount = 0;
        detectionAttempts = 0;
    }

    public boolean isDetected() {
        //if we have detected more than or equal to the number of detections we want
        //return true
        return detectionCount >= THRESHOLD;
    }

    public int getDetectionCount() {
        return detectionCount;
    }

    public Rect getDetectionBounds() {
        return detectionBounds;
    }

    public Point getCenter() {
        return center;
    }

    public double getDetectionPercentage() {
        return ((double)detectionCount) / detectionAttempts;
    }

    public void processFrame(Mat input) {
        //if we detect a QR code then add 1 to the detection count
        //regardless add one to the detection attempts
        if (detect(input)) {
            detectionCount++;
        }
        detectionAttempts++;
    }

    //image is passed in from the webcam
    private boolean detect(Mat image) {
        //makes a rectangle that determines where the image is cropped
        Rect bounds = detectionBounds.clone();
        RectUtil.clip(bounds, image.size());

        //makes a new cropped image out of the image
        Mat cropped;
        cropped = new Mat(image, bounds);

        //calls the opencv to detect a QR code on the image and returns the points
        //of the QR code
        Mat result = new Mat();
        detector.detect(cropped, result);

        //if we have points meaning we found the QR code then return true,
        //otherwise return false
        return result.cols() > 0;
    }
}
