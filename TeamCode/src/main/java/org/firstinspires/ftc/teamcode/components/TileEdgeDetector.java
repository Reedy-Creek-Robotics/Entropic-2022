package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver.TileEdgeObservation;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.tilesToInches;
import static org.firstinspires.ftc.teamcode.util.HoughLineDetector.HoughLine;
import static org.firstinspires.ftc.teamcode.util.HoughLineDetector.HoughParameters;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DistanceUtil;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.firstinspires.ftc.teamcode.util.HoughLineDetector;
import org.opencv.core.Mat;
import org.opencv.core.Size;

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
    private HoughLineDetector houghLineDetectorHorizontal;

    /**
     * The detector for finding lines in the webcam image.
     */
    private HoughLineDetector houghLineDetectorVertical;

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
        Size resolution = robotDescriptor.webCamResolution;

        double verticalInches = Math.abs(robotDescriptor.webCamImageTopLeftCornerCoordinates.minus(
                robotDescriptor.webCamImageBottomLeftCornerCoordinates).getY());
        double horizontalInches = Math.abs(robotDescriptor.webCamImageTopRightCornerCoordinates.minus(
                robotDescriptor.webCamImageTopLeftCornerCoordinates).getX());
        double verticalPixelsPerInch = resolution.height / verticalInches;
        double horizontalPixelsPerInch = resolution.width / horizontalInches;

        HoughParameters horizontalParameters = new HoughParameters();
        horizontalParameters.similarLineRhoThreshold = 1.2 * horizontalPixelsPerInch;
        horizontalParameters.similarLineThetaThreshold = 4.0;  // degrees
        horizontalParameters.minTheta = 45;
        horizontalParameters.maxTheta = 135;
        horizontalParameters.pixelVoterThreshold = (int) (resolution.width * (1.0 / 4.0));
        this.houghLineDetectorHorizontal = new HoughLineDetector(horizontalParameters);

        HoughParameters verticalParameters = new HoughParameters();
        verticalParameters.similarLineRhoThreshold = 1.2 * verticalPixelsPerInch;
        verticalParameters.similarLineThetaThreshold = 4.0;  // degrees
        verticalParameters.minTheta = -45;
        verticalParameters.maxTheta = 45;
        verticalParameters.pixelVoterThreshold = (int) (resolution.height * (1.0 / 4.0));
        this.houghLineDetectorVertical = new HoughLineDetector(verticalParameters);

        this.tileEdgeSolver = new TileEdgeSolver(robotDescriptor);
    }

    public HoughLineDetector getHoughLineDetectorHorizontal() {
        return houghLineDetectorHorizontal;
    }

    public HoughLineDetector getHoughLineDetectorVertical() {
        return houghLineDetectorVertical;
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
     *
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

            // Remember the time of the current frame capture as early as possible (before all the math).
            ElapsedTime beginFrameTime = new ElapsedTime();

            List<Line> lines = new ArrayList<>();
            for (HoughLine houghLine : houghLineDetectorHorizontal.detectLines(input)) {
                Line line = houghLine.toLine(robotDescriptor.webCamResolution);
                lines.add(line);
                DrawUtil.drawLine(output, line, Color.ORANGE);
            }
            for (HoughLine houghLine : houghLineDetectorVertical.detectLines(input)) {
                Line line = houghLine.toLine(robotDescriptor.webCamResolution);
                lines.add(line);
                DrawUtil.drawLine(output, line, Color.BLUE);
            }

            TileEdgeObservation observation = tileEdgeSolver.solve(lines);

            if (observation != null) {
                // Remember the observation so that it can be used by the drivetrain.
                TileEdgeDetector.this.observation = observation;
                observation.setObservationTime(beginFrameTime);

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

    }

}
