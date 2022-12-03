package org.firstinspires.ftc.teamcode.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector2Test {

    private static final double E = 1e-6;

    @Test
    public void rotate() {

        assertVectorEquals(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(0.0));
        assertVectorEquals(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(-90));
        assertVectorEquals(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(90));
        assertVectorEquals(new Vector2(0.0, 0.0), new Vector2(0.0, 0.0).rotate(45));

        assertVectorEquals(new Vector2(1.0, 0.0), new Vector2(1.0, 0.0).rotate(0.0));
        assertVectorEquals(new Vector2(0.0, 1.0), new Vector2(1.0, 0.0).rotate(90));
        assertVectorEquals(new Vector2(0.0, -1.0), new Vector2(1.0, 0.0).rotate(-90));
        assertVectorEquals(new Vector2(-1.0, 0.0), new Vector2(1.0, 0.0).rotate(180));

        assertVectorEquals(new Vector2(3.0, 0.0), new Vector2(3.0, 0.0).rotate(0.0));
        assertVectorEquals(new Vector2(0.0, 3.0), new Vector2(3.0, 0.0).rotate(90));
        assertVectorEquals(new Vector2(0.0, -3.0), new Vector2(3.0, 0.0).rotate(-90));
        assertVectorEquals(new Vector2(-3.0, 0.0), new Vector2(3.0, 0.0).rotate(180));

        assertVectorEquals(new Vector2(Math.sqrt(2), 0.0), new Vector2(1.0, 1.0).rotate(-45));
        assertVectorEquals(new Vector2(0.0, Math.sqrt(2)), new Vector2(1.0, 1.0).rotate(45));

        assertVectorEquals(new Vector2(0.0, Math.sqrt(2)), new Vector2(1.0, 0).rotate(30));
        assertVectorEquals(new Vector2(0.0, Math.sqrt(2)), new Vector2(0, 1.0).rotate(30));
        assertVectorEquals(new Vector2(0.0, Math.sqrt(2)), new Vector2(1.0, 1.0).rotate(30));
        assertVectorEquals(new Vector2(0.0, Math.sqrt(2)), new Vector2(1.0, 1.0).rotate(30));

    }

    private void assertVectorEquals(Vector2 expected, Vector2 actual) {
        assertEquals(expected.getX(), actual.getX(), E);
        assertEquals(expected.getY(), actual.getY(), E);
    }

}