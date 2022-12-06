package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TileEdgeDetector extends BaseComponent {

    /**
     * The maximum time in seconds that we will use a previous frame's detection before discarding it.  This can make
     * the detection more reliable in case we miss detection on a few frames for some reason.
     */
    private static final double PREVIOUS_DETECTION_THRESHOLD = 0.4;

    /**
     * The number of vertical pixels in the camera's field of view that corresponds to an inch of field space.
     */
    private static final double VERTICAL_PIXELS_PER_INCH = 52.8;

    /**
     * The vertical pixel position of the wheel edge in pixels.
     */
    private static final int VERTICAL_PIXELS_WHEEL_EDGE_POSITION = 145;

    /**
     * The expected height of the webcam image from top to bottom.
     */
    private static final int IMAGE_HEIGHT = 360;

    /**
     * The theta at which we consider the robot to be aligned with the tile edge.
     */
    private static final int THETA_ALIGNED_TO_TILE = 90;

    /**
     * The theta at which we consider the robot to be aligned with the tile edge horizontally.
     */
    private static final int THETA_ALIGNED_TO_TILE_VERTICAL = 180;


    /**
     * The webcam to use for observations
     */
    private WebCam webCam;

    /**
     * The observed angle to the tile (in degrees)
     */
    private double angleToTile;

    /**
     * The observed distance to the next tile edge that is in front of the robot (in feet)
     */
    private double distanceToTileHorizontal;

    /**
     * Observed distance to the next tile edge in the horizontal direction. (in feet)
     */
    private double distanceToTileVertical;

    /**
     * The time since the observed frame
     */
    private ElapsedTime observationTime;

    public TileEdgeDetector(OpMode opMode, WebCam webCam) {
        super(opMode);
        this.webCam = webCam;
        reset();
    }

    public void activate() {
        webCam.setFrameProcessor(new TileEdgeDetector.FrameProcessor());
    }

    /**
     * Indicates whether the tile edge is currently detected.
     */
    public boolean isDetected() {
        return observationTime != null;
    }

    /**
     * Waits for some amount of time for a successful tile edge detection.
     * @param maxTime the maximum time to wait in seconds.
     * @return true if an edge was detected, false if maxTime was exceeded without detection.
     */
    public boolean waitForDetection(double maxTime) {
        ElapsedTime time = new ElapsedTime();
        while (!isDetected() && time.seconds() < maxTime && !isStopRequested()) {
            sleep(25);
        }
        return isDetected();
    }

    public void reset() {
        observationTime = null;
        distanceToTileHorizontal = Double.NaN;
        distanceToTileHorizontal = Double.NaN;
        angleToTile = Double.NaN;
    }

    /**
     * Returns the angle to the closest tile edge to the right of the robot, in degrees.
     */
    public double getAngleToTile() {
        return angleToTile;
    }

    /**
     * Returns the distance to the closest tile edge to the right of the robot, in feet.
     */
    public double getDistanceToTileHorizontal() {
        return distanceToTileHorizontal;
    }

    /**
     * Returns the distance to the closest tile edge in the vertical direction, in feet.
     */
    public double getDistanceToTileVertical() {
        return distanceToTileVertical;
    }

    /**
     * Returns the elapsed time since the last observation, or null if there is no observation.
     */
    public ElapsedTime getObservationTime() {
        return observationTime;
    }

    private class FrameProcessor implements WebCam.FrameProcessor {

        private Mat gray = new Mat();
        private Mat edges = new Mat();

        private Mat edgesRgba = new Mat();

        @SuppressLint("DefaultLocale")
        @Override
        public void processFrame(Mat input, Mat output) {

            List<Line> lines = detectLines(input, output);
            for (Line line : lines) {
                drawOutputLine(output, line, Color.BLUE.toRGBA());
            }

            // The near and far edges of the gap between tiles are about 0.8 inches apart.  We set the threshold
            // slightly higher than this so that If we detect both of these lines we will group them together.  Their
            // weighted average will fall in the middle.
            double rhoThreshold = 0.9 * VERTICAL_PIXELS_PER_INCH;

            // Group lines that are very similar together
            lines = groupSimilarLines(lines, 2, rhoThreshold);
            for (Line line : lines) {
                drawOutputLine(output, line, Color.ORANGE.toRGBA());
            }

            // Sorts the lines by angle closest to 90 degrees.
            Collections.sort(lines, new Comparator<Line>() {
                @Override
                public int compare(Line a, Line b) {
                    return Double.compare(delta(a.theta, THETA_ALIGNED_TO_TILE), delta(b.theta, THETA_ALIGNED_TO_TILE));
                }

                private double delta(double angle, double targetAngle) {
                    return Math.abs(angle - targetAngle);
                }
            });

            if (!lines.isEmpty()) {
                Line line = lines.get(0);
                drawOutputLine(output, line, Color.GREEN.toRGBA());

                distanceToTileHorizontal = convertRhoToTileDistance(line.rho);
                angleToTile = convertThetaToTileAngle(line.theta);

                // Print out rho, theta, and count for the detected line.
                DrawUtil.drawText(
                        output,
                        String.format("(<%.2f, %.2f in) [%d]", angleToTile, distanceToTileHorizontal * 12.0, line.count),
                        new Point(100, 50),
                        Color.GREEN.toRGBA()
                );

                observationTime = new ElapsedTime();

            } else {
                // Keep the previous detection results if they are still within the previous detection threshold,
                // This helps in case we skip a frame or two for some reason.
                // Otherwise, discard the result so we no longer have a detection.
                if (observationTime != null && observationTime.seconds() > PREVIOUS_DETECTION_THRESHOLD) {
                    reset();
                }
            }
        }

        private double convertRhoToTileDistance(double rho) {
            double pixelsFromBottom = IMAGE_HEIGHT - rho;
            double pixelsFromWheelEdge = pixelsFromBottom - VERTICAL_PIXELS_WHEEL_EDGE_POSITION;
            return inchesToTiles(pixelsFromWheelEdge / VERTICAL_PIXELS_PER_INCH);
        }

        private double convertThetaToTileAngle(double theta) {
            return theta - THETA_ALIGNED_TO_TILE;
        }

        private List<Line> groupSimilarLines(List<Line> lines, double angleThreshold, double rhoThreshold) {

            List<Line> result = new ArrayList<>();

            for (Line line : lines) {
                // First, check if we have an existing line that is similar to this one (within the threshold).
                Line similarLine = findSimilarLine(line, result, angleThreshold, rhoThreshold);
                if (similarLine == null) {
                    // No lines similar to this one have been found, so add it to the result
                    result.add(line);
                } else {
                    // Do a weighted Average with the existing line
                    similarLine.count++;
                    similarLine.theta = ((similarLine.theta * (similarLine.count - 1)) + line.theta) / similarLine.count;
                    similarLine.rho = ((similarLine.rho * (similarLine.count - 1)) + line.rho) / similarLine.count;
                }
            }

            return result;
        }

        private Line findSimilarLine(Line line, List<Line> linesToSearch, double angleThreshold, double rhoThreshold) {
            for (Line candidate : linesToSearch) {
                if (Math.abs(candidate.theta - line.theta) < angleThreshold && Math.abs(candidate.rho - line.rho) < rhoThreshold) {
                    return candidate;
                }
            }
            return null;
        }

        private List<Line> detectLines(Mat input, Mat output) {

            Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2RGBA);

            Imgproc.cvtColor(input, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.Canny(gray, edges, 60, 60 * 3, 3, false);

            Mat houghLines = new Mat();
            Imgproc.HoughLines(edges, houghLines, 1, Math.PI / 180, 125);

            Imgproc.cvtColor(edges, edgesRgba, Imgproc.COLOR_GRAY2RGBA);
            //edgesRgba.copyTo(output);

            List<Line> lines = new ArrayList<>();
            for (int i = 0; i < houghLines.rows(); i++) {
                double[] data = houghLines.get(i, 0);
                double rho = data[0];
                double theta = Math.toDegrees(data[1]);
                lines.add(new Line(rho, theta));
            }

            // todo: figure out how to save the image to disk?  maybe if a button is pressed?
            Imgcodecs.imwrite("some-filename.jpg", output);

            return lines;
        }

        private void drawOutputLine(Mat output, Line line, Scalar color) {
            double theta = Math.toRadians(line.theta);
            double rho = line.rho;

            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a * rho;
            double y0 = b * rho;

            //Drawing lines on the image
            Point pt1 = new Point();
            Point pt2 = new Point();
            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * (a));
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * (a));

            Imgproc.line(output, pt1, pt2, color, 3);
        }

    }

    private static class Line {
        public double rho;
        public double theta;
        public int count;

        public Line(double rho, double theta) {
            this.rho = rho;
            this.theta = theta;
            this.count = 1;
        }
    }

}
