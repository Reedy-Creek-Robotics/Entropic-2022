package org.firstinspires.ftc.teamcode.geometry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.geometry.Viewport;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

public class TileEdgeSolver {

    private RobotDescriptor descriptor;

    public TileEdgeSolver(RobotDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * todo: document that this returns null
     *
     * @param webCamLines
     */
    public TileEdgeObservation solve(List<Line> webCamLines) {

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
    /*private Position convertFromImageCalibrationSpaceToRobotCenterSpace(
            RobotDescriptor descriptor, Position position
    ) {
        // Translate the coordinates to robot center.
        Vector2 robotCenterToRobotRight = new Position(

        )
        descriptor.robotDimensionsInInches.width
    }*/

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
