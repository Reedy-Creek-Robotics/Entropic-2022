package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertVector;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorNTest {

    @Test
    public void magnitude() {
        assertEquals(1.0, new VectorN(1.0).magnitude(), E);
        assertEquals(Math.sqrt(2), new VectorN(1.0, 1.0).magnitude(), E);
        assertEquals(Math.sqrt(3), new VectorN(1.0, 1.0, 1.0).magnitude(), E);
        assertEquals(2.0, new VectorN(1.0, 1.0, 1.0, 1.0).magnitude(), E);
    }

    @Test
    public void withMagnitude() {
        assertVector(
                new VectorN(1.0),
                new VectorN(0.5).withMagnitude(1.0)
        );
        assertVector(
                new VectorN(Math.sqrt(2) / 2.0, Math.sqrt(2) / 2.0),
                new VectorN(1.0, 1.0).withMagnitude(1.0)
        );
        assertVector(
                new VectorN(0.5, 0.5, 0.5, 0.5),
                new VectorN(1.0, 1.0, 1.0, 1.0).withMagnitude(1.0)
        );
    }

    @Test
    public void withMaxComponent() {
        assertVector(
                new VectorN(1.0, 1.0, 1.0, 1.0),
                new VectorN(0.5, 0.5, 0.5, 0.5).withMaxComponent(1.0)
        );
        assertVector(
                new VectorN(1.0, 0.5, 1.0, 0.5),
                new VectorN(0.6, 0.3, 0.6, 0.3).withMaxComponent(1.0)
        );
        assertVector(
                new VectorN(-1.0, -0.5, 1.0, 0.5),
                new VectorN(-0.6, -0.3, 0.6, 0.3).withMaxComponent(1.0)
        );
    }

    @Test
    public void clampToMax() {
        assertVector(
                new VectorN(0.5, 0.5, 0.5, 0.5),
                new VectorN(0.5, 0.5, 0.5, 0.5).clampToMax(1.0)
        );
        assertVector(
                new VectorN(-0.3, -0.3, -0.3, -0.3),
                new VectorN(-0.5, -0.5, -0.5, -0.5).clampToMax(0.3)
        );
        assertVector(
                new VectorN(-0.3, 0.15, -0.3, 0.3),
                new VectorN(-0.5, 0.25, -0.5, 0.5).clampToMax(0.3)
        );
    }

}
