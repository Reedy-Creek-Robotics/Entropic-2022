package org.firstinspires.ftc.teamcode.components;


import static org.firstinspires.ftc.teamcode.util.DistanceUtil.tilesToInches;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import org.firstinspires.ftc.teamcode.components.WebCam.FrameContext;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver.PoleContour;
import org.firstinspires.ftc.teamcode.util.PoleDetectionSolver.PoleDetection;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class PoleDetector extends BaseComponent {

    /**
     * The webcam to use for detection.
     */
    private WebCam webCam;

    private FrameProcessor frameProcessor;

    /**
     * The parameters to use when doing pole detection.
     */
    private PoleDetectionParameters parameters;

    /**
     * The most recent pole detection.
     */
    private PoleDetection detection;

    public PoleDetector(RobotContext context, WebCam webCam) {
        super(context);
        this.webCam = webCam;
        this.parameters = new PoleDetectionParameters();
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

    public static class PoleDetectionParameters {

        // Base pole color HSV (20 deg, 255, 3)
        // todo: calibrate this using the robot webcam

        /**
         * The lower bound for the pole color detection.
         */
        public Scalar poleColorLowerBound = new Scalar(15, 0, 0);

        /**
         * The upper bound for the pole color detection.
         */
        public Scalar poleColorUpperBound = new Scalar(25, 255, 255);

    }

    public PoleDetectionParameters getParameters() {
        return parameters;
    }

    private class FrameProcessor implements WebCam.FrameProcessor {
        private Mat hsv = new Mat();
        private Mat threshold = new Mat();
        private Mat morphed = new Mat();
        private Mat kernel = Mat.ones(7, 7, CvType.CV_8U);
        private Mat hierarchy = new Mat();

        @Override
        public void processFrame(Mat input, Mat output, FrameContext frameContext) {
            // Attempt to locate junction poles, by isolating their color.
            // https://techvidvan.com/tutorials/detect-objects-of-similar-color-using-opencv-in-python/

            // First, convert to HSV format.  We have to do this detection in HSV space to better handle variance
            // in lighting in the room.
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGBA2RGB);
            Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2HSV);

            // Create a mask with only the pixels that are in the given color range.
            Core.inRange(hsv, parameters.poleColorLowerBound, parameters.poleColorUpperBound, threshold);

            // Remove noise in the form of small patches of white or black pixels, ideally leaving us with
            // only a single large contour per pole.
            Imgproc.morphologyEx(threshold, morphed, Imgproc.MORPH_CLOSE, kernel);
            Imgproc.morphologyEx(morphed, morphed, Imgproc.MORPH_OPEN, kernel);

            // Detect the contours found on the screen, which are the connected white pixels in the mask.
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(morphed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            // Convert the OpenCv contours into pole contours.
            List<PoleContour> poleContours = new ArrayList<>();
            for (int i = 0; i < contours.size(); i++) {
                poleContours.add(createPoleContour(i, contours.get(i)));
            }

            // Now use the solver to convert from contours to an actual pole distance.
            PoleDetectionSolver solver = new PoleDetectionSolver(
                    robotDescriptor,
                    new PoleDetectionSolver.Parameters(webCam.getResolution())
            );
            PoleDetection detection = solver.solve(poleContours);
            if (detection != null) {
                detection.observationTime = frameContext.frameTime;
                PoleDetector.this.detection = detection;
            }

            if (webCam.isStreaming()) {
                // If streaming, draw the contours to the output image
                for (int i = 0; i < contours.size(); i++) {
                    Color color = (detection != null && i == detection.observedContour.id) ?
                            Color.GREEN :
                            Color.BLUE;
                    Imgproc.drawContours(output, contours, i, color.toRGBA(), 2, Imgproc.LINE_8);
                }

                if (detection != null) {
                    PoleContour contour = detection.observedContour;
                    double area = contour.area;
                    double fill = area / (contour.boundingRect.getWidth() * contour.boundingRect.getHeight()) * 100;

                    Vector2 distance = detection.distance;
                    Vector2 distanceInches = new Vector2(
                            tilesToInches(distance.getX()),
                            tilesToInches(distance.getY())
                    );

                    DrawUtil.drawRect(output, contour.boundingRect, Color.ORANGE);

                    DrawUtil.drawText(output, "Area: " + format(contour.area, 0), linePos(0), Color.ORANGE);
                    DrawUtil.drawText(output, "Distance: " + distanceInches.toString(1) + " inches", linePos(1), Color.ORANGE);
                    DrawUtil.drawText(output, "Fill: " + format(fill, 1), linePos(2), Color.GREEN);
                }
            }
        }

        private PoleContour createPoleContour(int id, MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);
            return new PoleContour(
                    id,
                    new Rectangle(
                            boundingRect.y,
                            boundingRect.x + boundingRect.width,
                            boundingRect.y + boundingRect.height,
                            boundingRect.x
                    ),
                    area
            );
        }

        private Position linePos(int number) {
            int textStart = 30;
            int textHeight = 25;
            return new Position(10, textStart + (textHeight * number));
        }
    }

}
