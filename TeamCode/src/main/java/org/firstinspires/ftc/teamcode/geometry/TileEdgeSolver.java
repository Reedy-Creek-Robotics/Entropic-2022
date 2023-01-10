package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.FormatUtil.*;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.util.DistanceUtil;
import org.firstinspires.ftc.teamcode.util.FormatUtil;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

public class TileEdgeSolver {

    private RobotDescriptor descriptor;

    public TileEdgeSolver(RobotDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * Examines lines that were found in a webcam image and, if possible, calculates the distance
     * from the robot to the tile edges, along with the heading offset between the robot and the
     * tile edge.
     *
     * If no observation can be made based on the given lines, then this returns null.
     *
     * @param webCamLines the list of lines, in pixel coordinates of the webcam image.
     */
    public TileEdgeObservation solve(List<Line> webCamLines) {

        Size resolution = descriptor.webCamResolution;

        // Convert the webcam corners to coordinates that are relative to the center of the robot.
        Position topLeft = convertFromImageCalibrationSpaceToRobotCenterSpace(descriptor.webCamImageTopLeftCornerCoordinates);
        Position topRight = convertFromImageCalibrationSpaceToRobotCenterSpace(descriptor.webCamImageTopRightCornerCoordinates);
        Position bottomLeft = convertFromImageCalibrationSpaceToRobotCenterSpace(descriptor.webCamImageBottomLeftCornerCoordinates);
        Position bottomRight = convertFromImageCalibrationSpaceToRobotCenterSpace(descriptor.webCamImageBottomRightCornerCoordinates);

        Viewport viewport = new Viewport(
                resolution.width, resolution.height,
                topLeft, topRight,
                bottomLeft, bottomRight
        );

        // Convert the lines from webcam coordinates into coordinates relative to the robot.
        List<Line> robotLines = new ArrayList<>();
        for (Line line : webCamLines) {
            robotLines.add(new Line(
                    viewport.convertViewToExternal(line.getP1()),
                    viewport.convertViewToExternal(line.getP2())
            ));
        }

        // todo: use the robot's current pose and heuristics to determine whether a line is a
        // todo: likely tile edge or just noise.
        // todo: another idea is to use the color around the detected line (tile edges should be
        // todo: dark gray, while posts and other robots will be different colors).

        if (!robotLines.isEmpty()) {
            TileEdgeObservation observation = new TileEdgeObservation(
                    null,
                    null,
                    null
            );

            // todo: figure out how to handle more than two lines.

            for (Line line : robotLines) {
                // Figure out if the line is the front edge or the right edge
                Position robotCenter = new Position(0, 0);
                double angle = line.getAngleToX();

                if (Math.abs(angle) > 45) {
                    // Right edge
                    line = line.normalizeY();
                    observation.distanceRight = robotCenter.distance(line);
                    if (!line.isLeft(robotCenter)) {
                        // If the robot center is to the right of the line, this is actually
                        // distanceLeft, so shift over one tile to compute distanceRight.
                        observation.distanceRight = -observation.distanceRight + 1.0;
                    }
                    observation.headingOffset = line.getAngleToY();

                } else {
                    // Front edge
                    line = line.normalizeX();
                    observation.distanceFront = robotCenter.distance(line);
                    if (line.isLeft(robotCenter)) {
                        // If the robot center is in front of the line, this is actually
                        // distanceBack, so shift forward one tile to compute distanceFront.
                        observation.distanceFront = -observation.distanceFront + 1.0;
                    }
                    observation.headingOffset = line.getAngleToX();
                }
            }

            return observation;

        } else {
            return null;
        }
    }

    /**
     * Converts a position from the image calibration coordinate space to robot center coordinate space.
     *
     * Image calibration space is convenient for measuring the bounds of the webcam.  It has its origin at the
     * right-front corner of the robot, is oriented with the y-axis pointing out to the right side of the robot,
     * and is measured in inches.  WebCam coordinates found in RobotDescriptor are specified in Image Calibration Space.
     *
     * Robot center space has its origin at the center of the robot, is oriented with the y-axis pointing toward the
     * front of the robot, and is measured in tiles.
     */
    public Position convertFromImageCalibrationSpaceToRobotCenterSpace(Position position) {
        // Rotate the coordinates so they are facing the front of the robot instead of to the right.
        Position rotated = new Position(
                position.getY(),
                -position.getX()
        );

        // Translate the coordinates to robot center.
        Vector2 robotCenterToRobotRight = new Vector2(
                descriptor.robotDimensionsInInches.width / 2,
                descriptor.robotDimensionsInInches.height / 2
        );

        // New coordinates in inches from robot center.
        Position fromRobotCenter = rotated.add(robotCenterToRobotRight);

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
        public ElapsedTime observationTime;

        public TileEdgeObservation(Double distanceFront, Double distanceRight, Double headingOffset) {
            this.distanceFront = distanceFront;
            this.distanceRight = distanceRight;
            this.headingOffset = headingOffset;
            this.observationTime = new ElapsedTime();
        }

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
