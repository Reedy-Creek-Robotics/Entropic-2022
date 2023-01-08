package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

import java.util.Arrays;
import java.util.List;

public class RobotFieldConversionUtil {


    public static FieldSpaceCoordinates convertToFieldSpace(RobotSpaceCoordinates robotSpaceCoordinates) {

        //get the offset between two neighboring points
        Vector2 backToFront = robotSpaceCoordinates.frontLeftTileVertex.minus(robotSpaceCoordinates.backLeftTileVertex);
        Vector2 leftToRight = robotSpaceCoordinates.backRightTileVertex.minus(robotSpaceCoordinates.backLeftTileVertex);

        Heading heading = null;
        Double xPos = null;
        Double yPos = null;

        if (backToFront.getY() != 0) {
            //y runs from the front to back / back to front
            heading = backToFront.toHeading().add(robotSpaceCoordinates.headingOffset);

            if (backToFront.getY() < 0) {
                //y runs front to back
                yPos = robotSpaceCoordinates.frontLeftTileVertex.getY() + robotSpaceCoordinates.distanceFront;

            } else {
                //y runs back to front
                yPos = robotSpaceCoordinates.frontLeftTileVertex.getY() - robotSpaceCoordinates.distanceFront;
            }

            if (leftToRight.getX() < 0) {
                //x runs right to left
                xPos = robotSpaceCoordinates.backRightTileVertex.getX() + robotSpaceCoordinates.distanceRight;

            } else {
                //x runs left to right
                xPos = robotSpaceCoordinates.backRightTileVertex.getX() - robotSpaceCoordinates.distanceRight;
            }

        } else if (leftToRight.getY() != 0) {
            //y runs from right to left / left to right
            heading = leftToRight.toHeading().add(robotSpaceCoordinates.headingOffset + 90);

            if (leftToRight.getY() < 0) {
                //y runs right to left
                yPos = robotSpaceCoordinates.frontRightTileVertex.getY() + robotSpaceCoordinates.distanceRight;

            } else {
                //y runs left to right
                yPos = robotSpaceCoordinates.frontRightTileVertex.getY() - robotSpaceCoordinates.distanceRight;
            }

            if (backToFront.getX() < 0) {
                //x runs back to front
                xPos = robotSpaceCoordinates.frontLeftTileVertex.getX() + robotSpaceCoordinates.distanceFront;

            } else {
                //x runs front to back
                xPos = robotSpaceCoordinates.frontLeftTileVertex.getX() - robotSpaceCoordinates.distanceFront;
            }
        }

        assert heading != null;
        assert yPos != null;
        assert xPos != null;

        return new FieldSpaceCoordinates(
                heading,
                new Position(xPos, yPos)
        );
    }

    public static RobotSpaceCoordinates convertToRobotSpace(FieldSpaceCoordinates fieldSpaceCoordinates) {

        Position position = fieldSpaceCoordinates.position;
        Heading heading = fieldSpaceCoordinates.heading;

        // Enumerate the vertices of tiles that surround the robot.
        List<Position> vertices = Arrays.asList(
                new Position(Math.floor(position.getX()), Math.floor(position.getY())),
                new Position(Math.floor(position.getX()), Math.ceil(position.getY())),
                new Position(Math.ceil(position.getX()), Math.floor(position.getY())),
                new Position(Math.ceil(position.getX()), Math.ceil(position.getY()))
        );

        // Figure out which vertex is which, in robot space.
        Position frontLeft = null, frontRight = null, backLeft = null, backRight = null;
        Position tileMiddle = position.alignToTileMiddle();
        for (Position vertex : vertices) {
            Vector2 offset = vertex.minus(tileMiddle);
            Heading vertexHeading = offset.toHeading().minus(heading).add(90);

            if (between(vertexHeading.getValue(), 0, 90)) {
                frontRight = vertex;
            } else if (between(vertexHeading.getValue(), 90, 180)) {
                frontLeft = vertex;
            } else if (between(vertexHeading.getValue(), 180, 270)) {
                backLeft = vertex;
            } else if (between(vertexHeading.getValue(), 270, 360)) {
                backRight = vertex;
            }
        }

        assert frontLeft != null;
        assert frontRight != null;
        assert backLeft != null;
        assert backRight != null;

        double headingOffset = heading.delta(frontLeft.minus(backLeft).toHeading());
        double distanceFront = position.distance(new Line(frontLeft, frontRight));
        double distanceRight = position.distance(new Line(backRight, frontRight));

        return new RobotSpaceCoordinates(
                headingOffset,
                distanceFront,
                distanceRight,
                frontLeft,
                frontRight,
                backLeft,
                backRight
        );
    }

    private static boolean between(double value, double min, double max) {
        return value >= min && value < max;
    }

    public static class FieldSpaceCoordinates {
        public Heading heading;
        public Position position;

        public FieldSpaceCoordinates(Heading heading, Position position) {
            this.heading = heading;
            this.position = position;
        }
    }

    public static class RobotSpaceCoordinates {
        public double headingOffset;
        public double distanceFront;
        public double distanceRight;

        // Field position of the vertices that surround the robot
        public Position frontLeftTileVertex;
        public Position frontRightTileVertex;
        public Position backLeftTileVertex;
        public Position backRightTileVertex;

        public RobotSpaceCoordinates(
                double headingOffset, double distanceFront, double distanceRight,
                Position frontLeftTileVertex, Position frontRightTileVertex,
                Position backLeftTileVertex, Position backRightTileVertex
        ) {
            this.headingOffset = headingOffset;
            this.distanceFront = distanceFront;
            this.distanceRight = distanceRight;
            this.frontLeftTileVertex = frontLeftTileVertex;
            this.frontRightTileVertex = frontRightTileVertex;
            this.backLeftTileVertex = backLeftTileVertex;
            this.backRightTileVertex = backRightTileVertex;
        }
    }

}
