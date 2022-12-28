package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver.TileEdgeObservation;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Size;

import java.util.Arrays;

public class TileEdgeSolverTest {

    private static final double E = 1e-6;

    private static RobotDescriptor descriptor = new RobotDescriptor();

    private static TileEdgeSolver solver = new TileEdgeSolver(descriptor);

    @Before
    public void setUp() {
        descriptor.webCamImageTopLeftCornerCoordinates = new Position(0, 9);
        descriptor.webCamImageBottomLeftCornerCoordinates = new Position(1, 1);
        descriptor.webCamImageTopRightCornerCoordinates = new Position(12, 9);
        descriptor.webCamImageBottomRightCornerCoordinates = new Position(11, 1);
        descriptor.robotDimensionsInInches = new Size(10.0, 12.0);
    }

    @Test
    public void convertToObservation_noLinesFound() {
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                // no lines
        ));

        assertNull(observation);
    }

    @Test
    public void convertToObservation_rightLineFound() {
        // One line, horizontal across the middle of the image.
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                new Line(new Position(0, 180), new Position(640, 180))
        ));

        assertNotNull(observation);

        // 10 inches from the robot center (line 5 inches from the right side of the robot, and robot is 10 wide)
        assertEquals(inchesToTiles(10), observation.distanceRight, E);

        // Perfectly aligned with right tile edge
        assertEquals(0.0, observation.headingOffset, E);

        // No data about the distance to the tile in front
        assertNull(observation.distanceFront);
    }

    @Test
    public void convertToObservation_frontLineFound() {
        // One line, vertical across the middle of the image.
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                new Line(new Position(320, 0), new Position(320, 360))
        ));

        assertNotNull(observation);

        // Right on top of the tile boundary (line 6 inches from front of robot, robot is 12 inches long)
        assertEquals(0.0, observation.distanceFront, E);

        // Perfectly aligned with right tile edge
        assertEquals(0.0, observation.headingOffset, E);

        // No data about the distance to the tile in front
        assertNull(observation.distanceRight);
    }

    @Test
    public void convertFromImageCalibrationSpaceToRobotCenterSpace() {
        assertPosEquals(
                new Position(inchesToTiles(5), inchesToTiles(6)),
                solver.convertFromImageCalibrationSpaceToRobotCenterSpace(new Position(0, 0))
        );

        assertPosEquals(
                new Position(inchesToTiles(13), inchesToTiles(1)),
                solver.convertFromImageCalibrationSpaceToRobotCenterSpace(new Position(5, 8))
        );
    }

    private void assertPosEquals(Position expected, Position actual) {
        assertEquals("x", expected.getX(), actual.getX(), E);
        assertEquals("y", expected.getY(), actual.getY(), E);
    }

}
