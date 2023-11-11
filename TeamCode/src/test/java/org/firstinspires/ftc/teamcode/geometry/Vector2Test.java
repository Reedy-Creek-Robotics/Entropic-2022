package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertVector;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Vector2Test {

    @Test
    public void rotate() {

        assertVector(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(0.0));
        assertVector(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(-90));
        assertVector(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(90));
        assertVector(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(45));

        assertVector(new Vector2(1.0, 0.0), new Vector2(1.0, 0.0).rotate(0.0));
        assertVector(new Vector2(0.0, 1.0), new Vector2(1.0, 0.0).rotate(90));
        assertVector(new Vector2(0.0, -1.0), new Vector2(1.0, 0.0).rotate(-90));
        assertVector(new Vector2(-1.0, 0.0), new Vector2(1.0, 0.0).rotate(180));

        assertVector(new Vector2(3.0, 0.0), new Vector2(3.0, 0.0).rotate(0.0));
        assertVector(new Vector2(0.0, 3.0), new Vector2(3.0, 0.0).rotate(90));
        assertVector(new Vector2(0.0, -3.0), new Vector2(3.0, 0.0).rotate(-90));
        assertVector(new Vector2(-3.0, 0.0), new Vector2(3.0, 0.0).rotate(180));

        assertVector(new Vector2(Math.sqrt(2), 0.0), new Vector2(1.0, 1.0).rotate(-45));
        assertVector(new Vector2(0.0, Math.sqrt(2)), new Vector2(1.0, 1.0).rotate(45));

    }

    @Test
    public void toHeading() {
        assertEquals(0.0, new Vector2(0,0).toHeading().getValue(), E);
        assertEquals(0.0, new Vector2(1.0, 0.0).toHeading().getValue(), E);
        assertEquals(90.0, new Vector2(0.0, 1.0).toHeading().getValue(), E);
        assertEquals(180.0, new Vector2(-1.0, 0.0).toHeading().getValue(), E);
        assertEquals(270.0, new Vector2(0.0, -1.0).toHeading().getValue(), E);
    }

    @Test
    public void magnitude() {
        assertEquals(0.0, new Vector2(0.0, 0.0).magnitude(), E);
        assertEquals(1.0, new Vector2(1.0, 0.0).magnitude(), E);
        assertEquals(Math.sqrt(2), new Vector2(1.0, 1.0).magnitude(), E);
        assertEquals(Math.sqrt(2), new Vector2(-1.0, 1.0).magnitude(), E);
    }

    @Test
    public void withMagnitude() {
        assertVector(
                new Vector2(Math.sqrt(2) / 2, Math.sqrt(2) / 2),
                new Vector2(1.0, 1.0).withMagnitude(1.0)
        );
    }

}
