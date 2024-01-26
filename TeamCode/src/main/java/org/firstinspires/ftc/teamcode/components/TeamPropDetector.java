package org.firstinspires.ftc.teamcode.components;

import android.annotation.SuppressLint;
import android.util.Pair;

import org.firstinspires.ftc.teamcode.components.ColorDetector.ColorDetection;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class TeamPropDetector extends BaseComponent {

    public enum TargetColor {
        RED,
        BLUE
    }

    private TargetColor targetColor;

    public enum TeamPropPosition {
        LEFT(
                new Rectangle(new Position(100, 100), 75, 75),
                3000, 15000, 0.75
        ),
        MIDDLE(
                new Rectangle(new Position(750, 75), 550, 425),
                3000, 15000, 0.9
        ),
        RIGHT(
                new Rectangle(new Position(500, 100), 75, 75),
                3000, 15000, 0.75
        ),
        NOTFOUND(
                new Rectangle(new Position(0, 0), 0, 0),
                0, 0, 0
        );

        public Rectangle rectangle;

                // For each position, the min and max expected area for the detected team prop contour
        public int minArea, maxArea;
        // For each position, how square does the team prop look in that position? This is contour area / bounding rect
        public double minSquarenessRatio;


        TeamPropPosition(Rectangle rectangle, int minArea, int maxArea, double minSquarenessRatio) {
            this.rectangle = rectangle;
            this.minArea = minArea;
            this.maxArea = maxArea;
            this.minSquarenessRatio = minSquarenessRatio;
        }
    }

    /**
     * The color detector to use to find the team prop.
     */
    private ColorDetector colorDetector;

    private WebCam webCam;

    public TeamPropDetector(RobotContext context, WebCam webCam) {
        this(context, webCam, TargetColor.BLUE);
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
            ranges.add(new Pair<>(
                    new Scalar(240, 100, 50),
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

    public Mat drawDetectionRegions(Mat output) {
        Point bottom_left = new Point(TeamPropPosition.LEFT.rectangle.getLeft(),TeamPropPosition.LEFT.rectangle.getBottom());
        Point top_right = new Point(TeamPropPosition.LEFT.rectangle.getRight(),TeamPropPosition.LEFT.rectangle.getTop());
        Imgproc.rectangle(output,bottom_left,top_right,new Scalar(255, 0, 255),5);
        bottom_left = new Point(TeamPropPosition.MIDDLE.rectangle.getLeft(),TeamPropPosition.MIDDLE.rectangle.getBottom());
        top_right = new Point(TeamPropPosition.MIDDLE.rectangle.getRight(),TeamPropPosition.MIDDLE.rectangle.getTop());
        Imgproc.rectangle(output,bottom_left,top_right,new Scalar(255, 0, 255),5);
        bottom_left = new Point(TeamPropPosition.RIGHT.rectangle.getLeft(),TeamPropPosition.RIGHT.rectangle.getBottom());
        top_right = new Point(TeamPropPosition.RIGHT.rectangle.getRight(),TeamPropPosition.RIGHT.rectangle.getTop());
        Imgproc.rectangle(output,bottom_left,top_right,new Scalar(255, 0, 255),5);
        return output;
    }

    TeamPropPosition isInRegion(ColorDetection detection) {
        double x = detection.centroid.getX();
        double y = detection.centroid.getY();

        if(x > TeamPropPosition.LEFT.rectangle.getLeft() && x < TeamPropPosition.LEFT.rectangle.getRight() &&
            y > TeamPropPosition.LEFT.rectangle.getBottom() && y < TeamPropPosition.LEFT.rectangle.getTop()) {
            return TeamPropPosition.LEFT;
        }
        if(x > TeamPropPosition.MIDDLE.rectangle.getLeft() && x < TeamPropPosition.MIDDLE.rectangle.getRight() &&
                y < TeamPropPosition.MIDDLE.rectangle.getBottom() && y > TeamPropPosition.MIDDLE.rectangle.getTop()) {
            return TeamPropPosition.MIDDLE;
        }
        if(x > TeamPropPosition.RIGHT.rectangle.getLeft() && x < TeamPropPosition.RIGHT.rectangle.getRight() &&
                y > TeamPropPosition.RIGHT.rectangle.getBottom() && y < TeamPropPosition.RIGHT.rectangle.getTop()) {
            return TeamPropPosition.RIGHT;
        }

        return TeamPropPosition.NOTFOUND;
    }

    public void activate() {
        colorDetector.activate();
        WebCam.FrameProcessor colorDetectorFrameProcessor = webCam.getFrameProcessor();
        webCam.setFrameProcessor(new WebCam.FrameProcessor() {
            @Override
            @SuppressLint("DefaultLocale")
            public void processFrame(Mat input, Mat output, WebCam.FrameContext frameContext) {
                colorDetectorFrameProcessor.processFrame(input, output, frameContext);

                if (webCam.isStreaming()) {
                    // Print team prop data about each detection
                    output = drawDetectionRegions(output);

                    List<ColorDetection> detections = colorDetector.getDetections();
                    telemetry.addLine("THIS DETECTOR TEST");
                    for(ColorDetection detection : detections) {

                        if(detection.area > 10000) {
                            telemetry.addData("test",detection.area);
                            telemetry.addData("x1",TeamPropPosition.MIDDLE.rectangle.getLeft());
                            telemetry.addData("x2",TeamPropPosition.MIDDLE.rectangle.getRight());
                            sleep(1000);
                            switch (isInRegion(detection)) {
                                case LEFT:
                                    telemetry.addLine("Found LEFT");
                                    break;
                                case MIDDLE:
                                    telemetry.addLine("Found MIDDLE");
                                    break;
                                case RIGHT:
                                    telemetry.addLine("Found RIGHT");
                                    break;
                                case NOTFOUND:
                                    telemetry.addData("x",detection.centroid.getX());
                                    telemetry.addLine("NOT FOUND");
                                    break;
                            }
                            telemetry.update();
                        }
                    }


                    //telemetry.addLine("****HERE****");telemetry.update();



                    /*for (ColorDetection detection : detections) {
                        if (detection.area < 100) continue;

                        double squareness = detection.area / detection.boundingRect.getArea();
                        DrawUtil.drawText(output, String.format("Sq [%.2f]", squareness), detection.centroid.add(new Vector2(-50, -30)), Color.GREEN, 0.5, 1);
                        DrawUtil.drawText(output, detection.centroid.toString(0), detection.centroid.add(new Vector2(-50, 0)), Color.GREEN, 0.5, 1);
                        DrawUtil.drawText(output, String.format("Area [%.0f])", detection.area), detection.centroid.add(new Vector2(-50, 30)), Color.GREEN, 0.5, 1);
                        Imgproc.boundingRect(detection.boundingRect);
                    }*/
                }
            }
        });
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
    public Enum<TargetColor> getColor() {
        return(targetColor);

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

        //best guess

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
        telemetry.addLine("results of detection");
        for (ColorDetection detection : detections) {
            if(detection.area > 5000) {
                telemetry.addData("area", detection.area);
            }
            // Check if this detection is in the correct location on screen for this team prop position
            Rectangle rectangleL = TeamPropPosition.LEFT.rectangle;
            if (TeamPropPosition.LEFT.rectangle.contains(detection.centroid)) {
                telemetry.addData("Left ",detection.area);
            }
            if (TeamPropPosition.MIDDLE.rectangle.contains(detection.centroid)) {
                telemetry.addData("Middle ",detection.area);
            }

            if (TeamPropPosition.RIGHT.rectangle.contains(detection.centroid)) {
                telemetry.addData("Right ",detection.area);
            }

        }
        telemetry.update();

        for (TeamPropPosition position : TeamPropPosition.values()) {

            for (ColorDetection detection : detections) {
                // Check if this detection is in the correct location on screen for this team prop position
                Rectangle rectangle = position.rectangle;
                if (!rectangle.contains(detection.centroid)) {
                    continue;
                }

                // See if this detection is approximately a square
                double squarenessRatio = detection.area / detection.boundingRect.getArea();
                if (squarenessRatio > position.minSquarenessRatio) {
                    continue;
                }

                // Make sure the detection is approximately the correct size
                double smallestArea = position.minArea;
                double largestArea = position.maxArea;
                if (smallestArea > detection.area || detection.area > largestArea) {
                    continue;
                }

                // This detection fits all the criteria, so let's use it.
                /*telemetry.addData("Position", position);
                telemetry.update();*/
                return position;
            }
        }

        // We didn't detect any color objects with the right parameters, so return null
        return null;
    }

}
