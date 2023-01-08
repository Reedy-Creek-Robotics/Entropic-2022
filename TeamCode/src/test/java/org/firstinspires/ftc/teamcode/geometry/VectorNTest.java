package org.firstinspires.ftc.teamcode.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorNTest {

    private static final double E = 1e-6;

    @Test
    public void magnitude() {
        assertEquals(1.0, new VectorN(1.0).magnitude(), E);
        assertEquals(Math.sqrt(2), new VectorN(1.0, 1.0).magnitude(), E);
        assertEquals(Math.sqrt(3), new VectorN(1.0, 1.0, 1.0).magnitude(), E);
        assertEquals(2.0, new VectorN(1.0, 1.0, 1.0, 1.0).magnitude(), E);
    }

    @Test
    public void withMagnitude() {
        assertVectorEquals(
                new VectorN(1.0),
                new VectorN(0.5).withMagnitude(1.0)
        );
        assertVectorEquals(
                new VectorN(Math.sqrt(2) / 2.0, Math.sqrt(2) / 2.0),
                new VectorN(1.0, 1.0).withMagnitude(1.0)
        );
        assertVectorEquals(
                new VectorN(0.5, 0.5, 0.5, 0.5),
                new VectorN(1.0, 1.0, 1.0, 1.0).withMagnitude(1.0)
        );
    }

    @Test
    public void withMaxComponent() {
        assertVectorEquals(
                new VectorN(1.0, 1.0, 1.0, 1.0),
                new VectorN(0.5, 0.5, 0.5, 0.5).withMaxComponent(1.0)
        );
        assertVectorEquals(
                new VectorN(1.0, 0.5, 1.0, 0.5),
                new VectorN(0.6, 0.3, 0.6, 0.3).withMaxComponent(1.0)
        );
        assertVectorEquals(
                new VectorN(-1.0, -0.5, 1.0, 0.5),
                new VectorN(-0.6, -0.3, 0.6, 0.3).withMaxComponent(1.0)
        );
    }

    @Test
    public void clampToMax() {
        assertVectorEquals(
                new VectorN(0.5, 0.5, 0.5, 0.5),
                new VectorN(0.5, 0.5, 0.5, 0.5).clampToMax(1.0)
        );
        assertVectorEquals(
                new VectorN(-0.3, -0.3, -0.3, -0.3),
                new VectorN(-0.5, -0.5, -0.5, -0.5).clampToMax(0.3)
        );
        assertVectorEquals(
                new VectorN(-0.3, 0.15, -0.3, 0.3),
                new VectorN(-0.5, 0.25, -0.5, 0.5).clampToMax(0.3)
        );
    }

    private void assertVectorEquals(VectorN expected, VectorN actual) {
        assertEquals("vector size", expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals("vector[" + i + "]", expected.get(i), actual.get(i), E);
        }
    }

}
