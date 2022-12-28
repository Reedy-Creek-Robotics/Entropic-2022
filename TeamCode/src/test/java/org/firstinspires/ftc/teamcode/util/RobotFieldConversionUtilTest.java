package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.FieldSpaceCoordinates;
import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.RobotSpaceCoordinates;
import static org.junit.Assert.assertEquals;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
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

    @Test
    public void convertToFieldSpace() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                0,.5,.5,
                new Position(0,1), new Position(1,1),
                new Position(0,0), new Position(1,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                    new Heading(90), new Position(.5,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Heading30() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                30,.5,.5,
                new Position(0,1), new Position(1,1),
                new Position(0,0), new Position(1,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(120), new Position(.5,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Heading330() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                -30,.5,.5,
                new Position(0,1), new Position(1,1),
                new Position(0,0), new Position(1,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(60), new Position(.5,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Rotated270() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                0,.5,.5,
                new Position(1,0), new Position(0,0),
                new Position(1,1), new Position(0,1)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(270), new Position(.5,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_OffsetFrontAndRight1() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                0,0,0,
                new Position(0,1), new Position(1,1),
                new Position(0,0), new Position(1,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(90), new Position(1,1)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_OffsetRight2() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                30,.5,.2,
                new Position(0,1), new Position(1,1),
                new Position(0,0), new Position(1,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(120), new Position(.8,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Rotated270_OffsetRight_Heading30() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                30,.5,.2,
                new Position(1,0), new Position(0,0),
                new Position(1,1), new Position(0,1)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(300), new Position(.2,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Rotated180_Heading30() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                30,.5,.5,
                new Position(0,0), new Position(0,1),
                new Position(1,0), new Position(1,1)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(210), new Position(.5,.5)
                ),
                result
        );
    }

    @Test
    public void convertToFieldSpace_Rotated0_Heading30() {
        RobotSpaceCoordinates robotSpaceCoordinates = new RobotSpaceCoordinates(
                30,.5,.5,
                new Position(1,1), new Position(1,0),
                new Position(0,1), new Position(0,0)
        );

        FieldSpaceCoordinates result = RobotFieldConversionUtil.convertToFieldSpace(robotSpaceCoordinates);

        assertFieldSpaceCoordinate(
                new FieldSpaceCoordinates(
                        new Heading(30), new Position(.5,.5)
                ),
                result
        );
    }

    private void assertRobotSpaceCoordinates(RobotSpaceCoordinates expected, RobotSpaceCoordinates actual) {
        assertPosition(expected.frontLeftTileVertex, actual.frontLeftTileVertex);
        assertPosition(expected.frontRightTileVertex, actual.frontRightTileVertex);
        assertPosition(expected.backLeftTileVertex, actual.backLeftTileVertex);
        assertPosition(expected.backRightTileVertex, actual.backRightTileVertex);

        assertEquals(expected.headingOffset, actual.headingOffset, E);
        assertEquals(expected.distanceRight, actual.distanceRight, E);
        assertEquals(expected.distanceFront, actual.distanceFront, E);
    }

    private void assertFieldSpaceCoordinate(FieldSpaceCoordinates expected, FieldSpaceCoordinates actual) {
        assertPosition(expected.position, actual.position);
        assertHeading(expected.heading, actual.heading);
    }

    private void assertPosition(Position expected, Position actual) {
        assertEquals(expected.getX(), actual.getX(), E);
        assertEquals(expected.getY(), actual.getY(), E);
    }

    private void assertHeading(Heading expected, Heading actual) {
        assertEquals(expected.getValue(), actual.getValue(), E);
    }

}
