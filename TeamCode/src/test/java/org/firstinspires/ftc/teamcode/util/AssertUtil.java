package org.firstinspires.ftc.teamcode.util;

import static org.junit.Assert.assertEquals;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.geometry.VectorN;

public class AssertUtil {

    public static final double E = 1e-6;

    public static void assertHeading(Heading expected, Heading actual) {
        assertEquals(expected.getValue(), actual.getValue(), E);
    }

    public static void assertVector(Vector2 expected, Vector2 actual) {
        assertEquals("x", expected.getX(), actual.getX(), E);
        assertEquals("y", expected.getY(), actual.getY(), E);
    }

    public static void assertVector(VectorN expected, VectorN actual) {
        assertEquals("vector size", expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals("vector[" + i + "]", expected.get(i), actual.get(i), E);
        }
    }

    public static void assertPosition(Position expected, Position actual) {
        assertPosition("position", expected, actual);
    }

    public static void assertPosition(String message, Position expected, Position actual) {
        assertEquals(message + ".x", expected.getX(), actual.getX(), E);
        assertEquals(message + ".y", expected.getY(), actual.getY(), E);
    }

    public static void assertLine(Line expected, Line actual) {
        assertPosition(expected.getP1(), actual.getP1());
        assertPosition(expected.getP2(), actual.getP2());
    }

}
