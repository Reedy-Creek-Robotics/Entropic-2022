package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.HoughUtil.HoughLine;
import static org.firstinspires.ftc.teamcode.util.HoughUtil.detectLines;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Viewport;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

public class TileEdgeDetectionUtil {

    /**
     * Detects tile edges in the given image, and derives an observation about the position of the robot within
     * the tile.
     *
     * @param descriptor the description of the robot's physical characteristics
     * @param image the image of the tiles to use for detection
     *
     * @return the observation of the robot's position.
     */
    public static TileEdgeObservation detectTileEdges(
            RobotDescriptor descriptor,
            Mat image
    ) {
        List<HoughLine> houghLines = detectLines(image, 0.0, 0.0);
        List<Line> lines = new ArrayList<>();
        for (HoughLine houghLine : houghLines) {
            lines.add(houghLine.toLine(descriptor.webCamResolution));
        }

        TileEdgeObservation observation = convertToObservation(descriptor, lines);

        return observation;
    }

    public static TileEdgeObservation convertToObservation(
            RobotDescriptor descriptor,
            List<Line> webCamLines
    ) {

        Size resolution = descriptor.webCamResolution;

        // todo: convert to coordinates that are relative to the center of the robot

        Viewport viewport = new Viewport(
                resolution.width, resolution.height,
                descriptor.webCamImageTopLeftCornerCoordinates, descriptor.webCamImageTopRightCornerCoordinates,
                descriptor.webCamImageBottomLeftCornerCoordinates, descriptor.webCamImageBottomRightCornerCoordinates
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


        TileEdgeObservation observation = new TileEdgeObservation(
                null,
                null,
                null
        );

        if (!robotLines.isEmpty()) {
            Line line = robotLines.get(0);

            // todo: figure out if the line is the front edge or the right edge
            if (Math.abs(line.getAngle()) > 45) {
                observation.distanceRight = new Position(0, 0).distance(line);
                observation.headingOffset = line.getAngle(); // todo: should be offset 90 from this I think
            } else {

            }

            // todo: handle multiple lines
        }

        return observation;
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
    }

}
