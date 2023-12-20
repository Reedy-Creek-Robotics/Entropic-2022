package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamOrientation.FRONT_FIELD;
import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamOrientation.RIGHT_SIDE_FIELD;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamDescriptor;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.util.DistanceUtil;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEdgeSolver {

    private RobotContext context;
    private RobotDescriptor descriptor;
    private WebCamDescriptor webCamDescriptor;

    /**
     * The maximum angle between an observed line and the expected location of that line.  If the
     * line falls outside this boundary then we will discard it.
     */
    public double expectedTileEdgeAngleThreshold = 15;

    public TileEdgeSolver(RobotContext context, WebCamDescriptor webCamDescriptor) {
        this.context = context;
        this.descriptor = context.descriptor;
        this.webCamDescriptor = webCamDescriptor;
    }

    /**
     * Examines lines that were found in a webcam image and, if possible, calculates the distance
     * from the robot to the tile edges, along with the heading offset between the robot and the
     * tile edge.
     * <p>
     * If no observation can be made based on the given lines, then this returns null.
     *
     * @param webCamLines the list of lines, in pixel coordinates of the webcam image.
     */
    public TileEdgeObservation solve(List<Line> webCamLines) {

        Size resolution = webCamDescriptor.resolution;

        // Convert the webcam corners to coordinates that are relative to the center of the robot.
        Position topLeft = convertFromWebCamFieldSpaceToRobotSpace(webCamDescriptor.topLeft.robot);
        Position topRight = convertFromWebCamFieldSpaceToRobotSpace(webCamDescriptor.topRight.robot);
        Position bottomLeft = convertFromWebCamFieldSpaceToRobotSpace(webCamDescriptor.bottomLeft.robot);
        Position bottomRight = convertFromWebCamFieldSpaceToRobotSpace(webCamDescriptor.bottomRight.robot);

        Viewport viewport = new Viewport(
                resolution.width, resolution.height,
                topLeft, topRight,
                bottomLeft, bottomRight
        );


        // Convert the lines from webcam coordinates into coordinates relative to the robot.
        List<Line> robotLines = new ArrayList<>();
        Map<Line, Line> originalLines = new HashMap<>();
        for (Line line : webCamLines) {
            Line robotLine = new Line(
                    viewport.convertViewToExternal(line.getP1()),
                    viewport.convertViewToExternal(line.getP2())
            );
            robotLines.add(robotLine);
            originalLines.put(robotLine, line);
        }

        if (!robotLines.isEmpty()) {
            TileEdgeObservation observation = new TileEdgeObservation();

            List<Line> filteredLines = filterTileEdgeLines(robotLines, observation, originalLines);

            for (Line line : filteredLines) {
                Line originalLine = originalLines.get(line);

                // Figure out if the line is the front edge or the right edge
                Position robotCenter = new Position(0, 0);
                double angle = line.getAngleToX();

                if (Math.abs(angle) > 45 && observation.distanceRight == null) {
                    // Right edge
                    line = line.normalizeY();
                    observation.distanceRight = robotCenter.distance(line);
                    if (!line.isLeft(robotCenter)) {
                        // If the robot center is to the right of the line, this is actually
                        // distanceLeft, so shift over one tile to compute distanceRight.
                        observation.distanceRight = -observation.distanceRight + 1.0;
                    }
                    observation.headingOffset = line.getAngleToY();
                    observation.observedRightEdge = originalLine;

                } else if (Math.abs(angle) < 45 && observation.distanceFront == null) {
                    // Front edge
                    line = line.normalizeX();
                    observation.distanceFront = robotCenter.distance(line);
                    if (line.isLeft(robotCenter)) {
                        // If the robot center is in front of the line, this is actually
                        // distanceBack, so shift forward one tile to compute distanceFront.
                        observation.distanceFront = -observation.distanceFront + 1.0;
                    }
                    observation.headingOffset = line.getAngleToX();
                    observation.observedFrontEdge = originalLine;

                } else {
                    observation.unusedLines.add(originalLine);
                }
            }

            return observation;

        } else {
            return null;
        }
    }

    private List<Line> filterTileEdgeLines(List<Line> lines, TileEdgeObservation observation, Map<Line, Line> originalLines) {
        // todo: another idea is to use the color around the detected line (tile edges should be
        // todo: dark gray, while posts and other robots will be different colors).

        if (context.robotPositionProvider != null) {
            List<Line> filteredLines = new ArrayList<>();
            Heading robotHeading = context.robotPositionProvider.getHeading();

            Position origin = new Position(0, 0);

            Line expectedRightEdge = new Line(origin, origin.add(new Vector2(0, 1).rotate(robotHeading.getValue())));
            Line expectedFrontEdge = new Line(origin, origin.add(new Vector2(1, 0).rotate(robotHeading.getValue())));

            for (Line line : lines) {
                Line originalLine = originalLines.get(line);
                if (Math.abs(line.getAngleToLine(expectedFrontEdge)) < expectedTileEdgeAngleThreshold ||
                        Math.abs(line.getAngleToLine(expectedRightEdge)) < expectedTileEdgeAngleThreshold
                ) {
                    filteredLines.add(line);
                } else {
                    observation.badLines.add(originalLine);
                }
            }

            return filteredLines;

        } else {
            // We don't have access to the robot position, so just return the full list.
            return lines;
        }
    }

    /**
     * Converts a position from the webcam field coordinate space to robot center coordinate space.
     * <p>
     * Webcam field space is convenient for measuring the bounds of the webcam.  It has its origin at the
     * right-front corner of the robot, is oriented with the y-axis pointing out to the right side of the robot,
     * and is measured in inches.  WebCam coordinates found in RobotDescriptor are specified in Image Calibration Space.
     * <p>
     * Robot center space has its origin at the center of the robot, is oriented with the y-axis pointing toward the
     * front of the robot, and is measured in tiles.
     */
    public Position convertFromWebCamFieldSpaceToRobotSpace(Position position) {
        // Rotate the coordinates so they are facing the front of the robot instead of to the right.
        Position fromRobotCenter;
        if (webCamDescriptor.orientation == RIGHT_SIDE_FIELD) {
            Position rotated = new Position(
                    position.getY(),
                    -position.getX()
            );

            // Translate the coordinates to robot center.
            Vector2 robotCenterToRobotFrontRight = new Vector2(
                    descriptor.ROBOT_DIMENSIONS_IN_INCHES.width / 2,
                    descriptor.ROBOT_DIMENSIONS_IN_INCHES.height / 2
            );

            // New coordinates in inches from robot center.
            fromRobotCenter = rotated.add(robotCenterToRobotFrontRight);

        } else if (webCamDescriptor.orientation == FRONT_FIELD) {
            // The coordinates of the front right wheel when placing down the calibration mat.
            Vector2 calibrationMatOffset = new Vector2(-8, -1);

            // Translate the coordinates to robot center.
            Vector2 robotCenterToRobotFrontRight = new Vector2(
                    descriptor.ROBOT_DIMENSIONS_IN_INCHES.width / 2,
                    descriptor.ROBOT_DIMENSIONS_IN_INCHES.height / 2
            );

            // New coordinates in inches from robot center.
            fromRobotCenter = position.add(calibrationMatOffset).add(robotCenterToRobotFrontRight);

        } else {
            throw new IllegalArgumentException();
        }

        // Convert from inches to tiles.
        Position fromRobotCenterInTiles = new Position(
                DistanceUtil.inchesToTiles(fromRobotCenter.getX()),
                DistanceUtil.inchesToTiles(fromRobotCenter.getY())
        );

        return fromRobotCenterInTiles;
    }

    public static class TileEdgeObservation {
        public Double distanceFront;
        public Double distanceRight;
        public Double headingOffset;

        public Line observedFrontEdge;
        public Line observedRightEdge;
        public List<Line> unusedLines = new ArrayList<>();
        public List<Line> badLines = new ArrayList<>();

        public ElapsedTime observationTime = new ElapsedTime();

        public void setObservationTime(ElapsedTime time) {
            this.observationTime = time;
        }

        public String toString() {
            return "DR " + format(distanceRight) + ", DF " + format(distanceFront) + "\n" +
                    "Heading " + format(headingOffset, 1) + " deg\n" +
                    format(observationTime.seconds()) + " sec";
        }
    }

}
