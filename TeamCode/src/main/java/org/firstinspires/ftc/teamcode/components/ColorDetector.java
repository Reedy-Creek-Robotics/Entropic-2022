package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.WebCam.FrameContext;

import android.util.Pair;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.util.Color;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

public class ColorDetector extends BaseComponent {
    /**
     * The webcam to use for detection.
     */
    private WebCam webCam;

    private FrameProcessor frameProcessor;

    /**
     * The parameters to use when doing color detection.
     */
    private ColorDetectionParameters parameters;

    /**
     * The most recent color detections.
     */
    private List<ColorDetection> detections = new ArrayList<>();

    public ColorDetector(RobotContext context, WebCam webCam) {
        this(context, webCam, new ColorDetectionParameters());
    }

    public ColorDetector(RobotContext context, WebCam webCam, ColorDetectionParameters parameters) {
        super(context);

        this.webCam = webCam;
        this.parameters = parameters;
    }

    public void activate() {
        frameProcessor = new FrameProcessor();
        webCam.setFrameProcessor(frameProcessor);
    }

    public boolean isActive() {
        return frameProcessor != null;
    }

    public void deactivate() {
        webCam.removeFrameProcessor();
        frameProcessor = null;
    }

    public List<ColorDetection> getDetections() {
        return detections;
    }

    public static class ColorDetectionParameters {

        /**
         * A list of the color ranges that should be detected
         */
        public List<Pair<Scalar, Scalar>> ranges = new ArrayList<>();

    }

    public static class ColorDetection {

        /**
         * The id of the contour according to the image detection source (e.g. OpenCv).
         */
        public int id;

        /**
         * The bounding rectangle of the contour.
         */
        public Rectangle boundingRect;

        /**
         * The area of the contour.
         */
        public double area;

        /**
         * The centroid of the contour.
         */
        public Position centroid;

        public ColorDetection(int id, Rectangle boundingRect, double area, Position centroid) {
            this.id = id;
            this.boundingRect = boundingRect;
            this.area = area;
            this.centroid = centroid;
        }
    }

    private class FrameProcessor implements WebCam.FrameProcessor {
        private Mat hsv = new Mat();
        private Mat threshold = new Mat();
        private Mat merged = new Mat();
        private Mat morphed = new Mat();
        private Mat kernel = Mat.ones(7, 7, CvType.CV_8U);
        private Mat hierarchy = new Mat();

        @Override
        public void processFrame(Mat input, Mat output, FrameContext frameContext) {
            // Attempt to locate objects of a given color.
            // https://techvidvan.com/tutorials/detect-objects-of-similar-color-using-opencv-in-python/

            // First, convert to HSV format.  We have to do this detection in HSV space to better handle variance
            // in lighting in the room.
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            // Loop over each color range, and add the pixels that match it to the output.
            boolean first = true;
            for (Pair<Scalar, Scalar> range : parameters.ranges) {
                Scalar low = range.first;
                Scalar high = range.second;

                // Create a mask with only the pixels that are in the given color ranges.
                if (first) {
                    // First color range, use the "merged" mat as output
                    Core.inRange(hsv, low, high, merged);

                    first = false;

                } else {
                    // Second and beyond, use a separate mat, and then combine with "merged".
                    Core.inRange(hsv, low, high, threshold);

                    Core.bitwise_or(threshold, merged, merged);
                }
            }

            // Remove noise in the form of small patches of white or black pixels, ideally leaving us with
            // only a single large contour per colored object.
            Imgproc.morphologyEx(merged, morphed, Imgproc.MORPH_CLOSE, kernel);
            Imgproc.morphologyEx(morphed, morphed, Imgproc.MORPH_OPEN, kernel);

            // Detect the contours found on the screen, which are the connected white pixels in the mask.
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(morphed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            // Convert the OpenCv contours into color detections.
            List<ColorDetection> detections = new ArrayList<>();
            for (int i = 0; i < contours.size(); i++) {
                detections.add(createDetection(i, contours.get(i)));
            }
            ColorDetector.this.detections = detections;

            if (webCam.isStreaming()) {
                // If streaming, draw the contours to the output image
                for (int i = 0; i < contours.size(); i++) {
                    Imgproc.drawContours(output, contours, i, Color.BLUE.toRGBA(), 2, Imgproc.LINE_8);
                }
            }
        }

        private ColorDetection createDetection(int id, MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);
            Moments moments = Imgproc.moments(contour);
            Position centroid = new Position(
                    moments.m10 / moments.m00,
                    moments.m01 / moments.m00
            );
            return new ColorDetection(
                    id,
                    new Rectangle(
                            boundingRect.y,
                            boundingRect.x + boundingRect.width,
                            boundingRect.y + boundingRect.height,
                            boundingRect.x
                    ),
                    area,
                    centroid
            );
        }
    }
}
