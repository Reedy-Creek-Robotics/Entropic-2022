package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamAnchorPoint.anchor;
import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamDescriptor;
import static org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamOrientation;
import static org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver.TileEdgeObservation;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertPosition;
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

    private static RobotDescriptor descriptor = new RobotDescriptor();
    private static RobotContext context = new RobotContext(null, descriptor);

    private static TileEdgeSolver solver;

    @Before
    public void setUp() {
        WebCamDescriptor webCamDescriptor = new WebCamDescriptor(
                "WebCamTest",
                WebCamOrientation.RIGHT_SIDE_FIELD,
                anchor(new Position(0, 0), new Position(0, 9)),
                anchor(new Position(640, 0), new Position(12, 9)),
                anchor(new Position(0, 360), new Position(0, 1)),
                anchor(new Position(640, 360), new Position(12, 1))
        );
        descriptor.ROBOT_DIMENSIONS_IN_INCHES = new Size(10.0, 12.0);

        solver = new TileEdgeSolver(context, webCamDescriptor);
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
    public void convertToObservation_frontLineFound_negativeDistanceFront() {
        // One line, vertical across the right side of the image
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                new Line(new Position(480, 0), new Position(480, 360))
        ));

        assertNotNull(observation);

        // Just crossed a tile boundary, distance front should be almost 1 tile
        // Robot center is 3 inches over the tile.
        assertEquals(1 - inchesToTiles(3), observation.distanceFront, E);

        // Perfectly aligned with right tile edge
        assertEquals(0.0, observation.headingOffset, E);

        // No data about the distance to the tile in front
        assertNull(observation.distanceRight);
    }

    @Test
    public void convertFromImageCalibrationSpaceToRobotCenterSpace() {
        assertPosition(
                new Position(inchesToTiles(5), inchesToTiles(6)),
                solver.convertFromWebCamFieldSpaceToRobotSpace(new Position(0, 0))
        );

        assertPosition(
                new Position(inchesToTiles(13), inchesToTiles(1)),
                solver.convertFromWebCamFieldSpaceToRobotSpace(new Position(5, 8))
        );
    }

}
