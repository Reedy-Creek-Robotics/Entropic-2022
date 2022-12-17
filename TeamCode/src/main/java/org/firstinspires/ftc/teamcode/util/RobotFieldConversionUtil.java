package org.firstinspires.ftc.teamcode.util;

import java.util.Arrays;
import java.util.List;

public class RobotFieldConversionUtil {


    public static FieldSpaceCoordinates convertToFieldSpace(RobotSpaceCoordinates robotSpaceCoordinates) {

        //get the offset between two neighboring points
        Vector2 offsetFrontNeighbor = robotSpaceCoordinates.frontLeftTileVertex.offset(robotSpaceCoordinates.backLeftTileVertex);
        Vector2 offsetRightNeighbor = robotSpaceCoordinates.backRightTileVertex.offset(robotSpaceCoordinates.backLeftTileVertex);

        Heading heading = null;
        double yPos = -1000;
        double xPos = -1000;

        if(offsetFrontNeighbor.getY() != 0) {
            //y runs from the front to back / back to front
            heading = offsetFrontNeighbor.toHeading().add(robotSpaceCoordinates.offsetHeading);

            if(offsetFrontNeighbor.getY() < 0) {
                //y runs front to back
                yPos = robotSpaceCoordinates.frontLeftTileVertex.getY() + robotSpaceCoordinates.offsetFront;

            }else {
                //y runs back to front
                yPos = robotSpaceCoordinates.frontLeftTileVertex.getY() - robotSpaceCoordinates.offsetFront;

            }

            if(offsetRightNeighbor.getX() < 0) {
                //x runs right to left
                xPos = robotSpaceCoordinates.backRightTileVertex.getX() + robotSpaceCoordinates.offsetRight;

            }else {
                //x runs left to right
                xPos = robotSpaceCoordinates.backRightTileVertex.getX() - robotSpaceCoordinates.offsetRight;

            }

        }else if(offsetRightNeighbor.getY() != 0) {
            //y runs from right to left / left to right
            heading = offsetRightNeighbor.toHeading().add(robotSpaceCoordinates.offsetHeading + 90);

            if(offsetRightNeighbor.getY() < 0) {
                //y runs right to left
                yPos = robotSpaceCoordinates.frontRightTileVertex.getY() + robotSpaceCoordinates.offsetRight;

            }else {
                //y runs left to right
                yPos = robotSpaceCoordinates.frontRightTileVertex.getY() - robotSpaceCoordinates.offsetRight;

            }

            if(offsetFrontNeighbor.getX() < 0) {
                //x runs back to front
                xPos = robotSpaceCoordinates.frontLeftTileVertex.getX() + robotSpaceCoordinates.offsetFront;

            }else {
                //x runs front to back
                xPos = robotSpaceCoordinates.frontLeftTileVertex.getX() - robotSpaceCoordinates.offsetFront;

            }
        }

        assert heading != null;
        assert yPos != -1000;
        assert xPos != -1000;


        return new FieldSpaceCoordinates(
                heading,
                new Position(xPos,yPos)
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
            Vector2 offset = vertex.offset(tileMiddle);
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

        double offsetHeading = frontLeft.offset(backLeft).toHeading().delta(heading);
        double offsetFront = position.distance(frontLeft, frontRight);
        double offsetRight = position.distance(backRight, frontRight);

        return new RobotSpaceCoordinates(
                offsetHeading,
                offsetFront,
                offsetRight,
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
        public double offsetHeading;
        public double offsetFront;
        public double offsetRight;

        // Field position of the vertices that surround the robot
        public Position frontLeftTileVertex;
        public Position frontRightTileVertex;
        public Position backLeftTileVertex;
        public Position backRightTileVertex;

        public RobotSpaceCoordinates(
                double offsetHeading, double offsetFront, double offsetRight,
                Position frontLeftTileVertex, Position frontRightTileVertex,
                Position backLeftTileVertex, Position backRightTileVertex
        ) {
            this.offsetHeading = offsetHeading;
            this.offsetFront = offsetFront;
            this.offsetRight = offsetRight;
            this.frontLeftTileVertex = frontLeftTileVertex;
            this.frontRightTileVertex = frontRightTileVertex;
            this.backLeftTileVertex = backLeftTileVertex;
            this.backRightTileVertex = backRightTileVertex;
        }
    }

}