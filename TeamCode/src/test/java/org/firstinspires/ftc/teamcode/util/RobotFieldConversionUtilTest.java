package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RobotFieldConversionUtilTest {

    private static final double E = 1e-6;

    @Test
    public void convertToRobotSpace() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(90), new Position(0.5, 0.5)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        0.0, 0.5, 0.5,
                        new Position(0, 1), new Position(1, 1),
                        new Position(0, 0), new Position(1, 0)
                ),
                result
        );
    }

    @Test
    public void convertToRobotSpace_heading60() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(60), new Position(0.5, 0.5)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        30.0, 0.5, 0.5,
                        new Position(0, 1), new Position(1, 1),
                        new Position(0, 0), new Position(1, 0)
                ),
                result
        );
    }

    @Test
    public void convertToRobotSpace_heading270() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(270), new Position(0.5, 0.5)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        0, 0.5, 0.5,
                        new Position(1, 0), new Position(0, 0),
                        new Position(1, 1), new Position(0, 1)
                ),
                result
        );
    }

    @Test
    public void convertToRobotSpace_heading90_offset() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(90), new Position(0.7, 0.3)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        0, 0.7, 0.3,
                        new Position(0, 1), new Position(1, 1),
                        new Position(0, 0), new Position(1, 0)
                ),
                result
        );
    }

    @Test
    public void convertToRobotSpace_heading270_offset() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(270), new Position(0.7, 0.3)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        0, 0.3, 0.7,
                        new Position(1, 0), new Position(0, 0),
                        new Position(1, 1), new Position(0, 1)
                ),
                result
        );
    }

    @Test
    public void convertToRobotSpace_heading30_offset() {
        FieldSpaceCoordinates fieldSpaceCoordinates = new FieldSpaceCoordinates(
                new Heading(30), new Position(0.7, 0.3)
        );

        RobotSpaceCoordinates result = RobotFieldConversionUtil.convertToRobotSpace(fieldSpaceCoordinates);

        assertRobotSpaceCoordinates(
                new RobotSpaceCoordinates(
                        -30, 0.3, 0.3,
                        new Position(1, 1), new Position(1, 0),
                        new Position(0, 1), new Position(0, 0)
                ),
                result
        );
    }

    private void assertRobotSpaceCoordinates(RobotSpaceCoordinates expected, RobotSpaceCoordinates actual) {
        assertPosition(expected.frontLeftTileVertex, actual.frontLeftTileVertex);
        assertPosition(expected.frontRightTileVertex, actual.frontRightTileVertex);
        assertPosition(expected.backLeftTileVertex, actual.backLeftTileVertex);
        assertPosition(expected.backRightTileVertex, actual.backRightTileVertex);

        assertEquals(expected.offsetHeading, actual.offsetHeading, E);
        assertEquals(expected.offsetRight, actual.offsetRight, E);
        assertEquals(expected.offsetFront, actual.offsetFront, E);
    }

    private void assertPosition(Position expected, Position actual) {
        assertEquals(expected.getX(), actual.getX(), E);
        assertEquals(expected.getY(), actual.getY(), E);
    }

}
