package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.util.HoughLineDetector.*;
import static org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver.*;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.HoughLineDetector;
import org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TileEdgeDetector extends BaseComponent {

    /**
     * The maximum time in seconds that we will use a previous frame's detection before discarding it.  This can make
     * the detection more reliable in case we miss detection on a few frames for some reason.
     */
    private static final double PREVIOUS_DETECTION_THRESHOLD = 0.2;

    /**
     * The webcam to use for observations
     */
    private WebCam webCam;

    /**
     * The detector for finding lines in the webcam image.
     */
    private HoughLineDetector houghLineDetector;

    /**
     * The solver that converts the tile edges found in the webcam image into an observation of the robot's position.
     */
    private TileEdgeSolver tileEdgeSolver;

    /**
     * The most recently acquired observation, or null if nothing has been observed yet.
     */
    private TileEdgeObservation observation;

    public TileEdgeDetector(RobotContext context, WebCam webCam) {
        super(context);
        this.webCam = webCam;
        reset();
    }

    @Override
    public void init() {
        double verticalInches = robotDescriptor.webCamImageTopLeftCornerCoordinates.minus(
                robotDescriptor.webCamImageBottomLeftCornerCoordinates).getY();
        double verticalPixelsPerInch = robotDescriptor.webCamResolution.height / verticalInches;

        HoughParameters parameters = new HoughParameters(
                0.9 * verticalPixelsPerInch,
                2.0
        );

        this.houghLineDetector = new HoughLineDetector(parameters);
        this.tileEdgeSolver = new TileEdgeSolver(robotDescriptor);
    }

    public void activate() {
        webCam.setFrameProcessor(new TileEdgeDetector.FrameProcessor());
    }

    /**
     * Indicates whether the tile edge is currently detected.
     */
    public boolean isDetected() {
        return observation != null;
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
        observation = null;
    }

    public TileEdgeObservation getObservation() {
        return observation;
    }

    private class FrameProcessor implements WebCam.FrameProcessor {

        @SuppressLint("DefaultLocale")
        @Override
        public void processFrame(Mat input, Mat output) {

            List<HoughLine> houghLines = houghLineDetector.detectLines(input);

            List<Line> lines = new ArrayList<>();
            for (HoughLine houghline : houghLines) {
                lines.add(houghline.toLine(robotDescriptor.webCamResolution));
            }

            TileEdgeObservation observation = tileEdgeSolver.solve(lines);

            for (Line line : lines) {
                drawOutputLine(output, line, Color.ORANGE.toRGBA());
            }

            if (observation != null) {
                // Remember the observation so that it can be used by the drivetrain.
                TileEdgeDetector.this.observation = observation;

            } else {
                // Keep the previous detection results if they are still within the previous detection threshold,
                // This helps in case we skip a frame or two for some reason.
                // Otherwise, discard the result so we no longer have a detection.
                TileEdgeObservation previousObservation = TileEdgeDetector.this.observation;
                if (previousObservation != null && previousObservation.observationTime.seconds() > PREVIOUS_DETECTION_THRESHOLD) {
                    reset();
                }
            }
        }

        private void drawOutputLine(Mat output, Line line, Scalar color) {
            Imgproc.line(output, toPoint(line.getP1()), toPoint(line.getP2()), color, 3);
        }

    }

    private Point toPoint(Position position) {
        return new Point(position.getX(), position.getY());
    }

}
