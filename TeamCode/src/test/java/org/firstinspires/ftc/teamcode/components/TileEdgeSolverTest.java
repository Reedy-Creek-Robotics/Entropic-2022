package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.util.TileEdgeSolver.TileEdgeObservation;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.util.TileEdgeSolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TileEdgeSolverTest {

    private static RobotDescriptor descriptor = new RobotDescriptor();

    private static TileEdgeSolver solver = new TileEdgeSolver(descriptor);

    @Test
    public void convertToObservation_noLinesFound() {
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                // no lines
        ));

        Assert.assertNull(observation.distanceFront);
        Assert.assertNull(observation.distanceRight);
        Assert.assertNull(observation.headingOffset);
    }

    @Test
    public void convertToObservation_rightLineFound() {
        TileEdgeObservation observation = solver.solve(Arrays.asList(
                new Line(new Position(0, 180), new Position(640, 180))
        ));

        Assert.assertNull(observation.distanceFront);

        Assert.assertNotNull(observation.distanceRight);
        Assert.assertNotNull(observation.headingOffset);
    }

}
