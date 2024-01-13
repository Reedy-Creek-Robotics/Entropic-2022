package org.firstinspires.ftc.teamcode.components;

import android.util.Pair;

import org.firstinspires.ftc.teamcode.components.ColorDetector.ColorDetection;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

public class TeamPropDetector extends BaseComponent {

    public enum TargetColor {
        RED,
        BLUE
    }

    private TargetColor targetColor;

    public enum TeamPropPosition {
        LEFT(new Rectangle(new Position(100, 100), 75, 75)),
        MIDDLE(new Rectangle(new Position(300, 100), 75, 75)),
        RIGHT(new Rectangle(new Position(500, 100), 75, 75));

        public Rectangle rectangle;

        TeamPropPosition(Rectangle rectangle) {
            this.rectangle = rectangle;
        }
    }

    /**
     * The color detector to use to find the team prop.
     */
    private ColorDetector colorDetector;

    private WebCam webCam;

    public TeamPropDetector(RobotContext context, WebCam webCam) {
        this(context, webCam, TargetColor.RED);
    }

    public TeamPropDetector(RobotContext context, WebCam webCam, TargetColor targetColor) {
        super(context);

        this.colorDetector = new ColorDetector(context, webCam);
        addSubComponents(colorDetector);

        this.targetColor = targetColor;
        this.webCam = webCam;
    }

    public void setTargetColor(TargetColor targetColor) {
        this.targetColor = targetColor;
        List<Pair<Scalar, Scalar>> ranges = new ArrayList<>();
        if (targetColor == TargetColor.RED) {
            // Special case - red needs to loop over the zero value for hue, so we need to detect it in two ranges
            parameters.ranges.add(new Pair<>(
                    new Scalar(245, 100, 100),
                    new Scalar(255, 255, 255)
            ));
            ranges.add(new Pair<>(
                    new Scalar(0, 100, 50),
                    new Scalar(10, 255, 255)
            ));
        } else if (targetColor == TargetColor.BLUE) {
            int hue = 0x6e;
            ranges.add(new Pair<>(
                    new Scalar(hue - 15, 100, 50),
                    new Scalar(hue + 15, 255, 255)
            ));
        }
        colorDetector.getParameters().ranges = ranges;
    }

    public void activate() {
        colorDetector.activate();
    }

    public boolean isActive() {
        return colorDetector.isActive();
    }

    public void deactivate() {
        colorDetector.deactivate();
    }

    public void init() {
        setTargetColor(targetColor);
    }

    public ColorDetector getColorDetector() {
        return colorDetector;
    }

    /**
     * Repeatedly attempts to detect the team prop for up to the given timeout in seconds, and returns its position as
     * soon as it is detected.
     */
    public TeamPropPosition waitForDetection(double timeOutInSeconds) {
        if (!isActive()) activate();

        long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1e9 < timeOutInSeconds) {
            TeamPropPosition detectedPosition = getDetectedPosition();
            if (detectedPosition != null) {
                // The team prop was detected, so return its position.
                return detectedPosition;
            }
        }

        // We didn't detect the team prop after the given amount of seconds, so return null.
        return null;
    }

    /**
     * Returns the detected position of the team prop from the latest frame available, or null if there is no
     * detected position.
     */
    public TeamPropPosition getDetectedPosition() {
        // Get the latest detections from the color detector
        List<ColorDetection> detections = this.colorDetector.getDetections();

        // Find which of the detections is most likely to be a team prop
        // To determine this, look at which color detection is:
        // 1. Approximately the expected size
        // 2. Mostly square (team prop is a cube)
        // 3. In the correct position

        for (ColorDetection detection : detections) {
            // See if this detection is approximately a square
            if (!isSquareDetection(detection)) {
                continue;
            }

            // Make sure the detection is approximately the correct size
            // todo: determine threshold for size
            double smallestSize = 1000;
            double largestSize = 10000;
            if (smallestSize > detection.area || detection.area > largestSize) {
                continue;
            }

            // Check which position the detection is at
            for (TeamPropPosition position : TeamPropPosition.values()) {
                Rectangle rectangle = position.rectangle;
                if (rectangle.contains(detection.centroid)) {
                    return position;
                }
            }
        }

        // We didn't detect any color objects with the right parameters, so return null
        return null;
    }

    private boolean isSquareDetection(ColorDetection detection) {
        double percentFilled = detection.area / detection.boundingRect.getArea();

        // todo: determine "square" threshold for filled percent
        double threshold = 0.9;

        return percentFilled > threshold;
    }

}
