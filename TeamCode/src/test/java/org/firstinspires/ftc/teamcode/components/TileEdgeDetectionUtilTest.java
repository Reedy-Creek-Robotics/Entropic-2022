package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.util.TileEdgeDetectionUtil.TileEdgeObservation;
import static org.firstinspires.ftc.teamcode.util.TileEdgeDetectionUtil.convertToObservation;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TileEdgeDetectionUtilTest {

    private static RobotDescriptor descriptor = new RobotDescriptor();

    @Test
    public void convertToObservation_noLinesFound() {
        TileEdgeObservation observation = convertToObservation(descriptor, Arrays.asList(
                // no lines
        ));

        Assert.assertNull(observation.distanceFront);
        Assert.assertNull(observation.distanceRight);
        Assert.assertNull(observation.headingOffset);
    }

    @Test
    public void convertToObservation_rightLineFound() {
        TileEdgeObservation observation = convertToObservation(descriptor, Arrays.asList(
                new Line(new Position(0, 180), new Position(640, 180))
        ));

        Assert.assertNull(observation.distanceFront);

        Assert.assertNotNull(observation.distanceRight);
        Assert.assertNotNull(observation.headingOffset);
    }

}
