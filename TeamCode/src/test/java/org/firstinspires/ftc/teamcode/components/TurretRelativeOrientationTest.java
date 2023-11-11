package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.LEFT_SIDE;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.RIGHT_SIDE;
import static org.firstinspires.ftc.teamcode.components.Turret.getFieldRelativeOrientation;
import static org.junit.Assert.assertEquals;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.junit.Test;

public class TurretRelativeOrientationTest {

    @Test
    public void getFieldRelativeOrientationForward() {
        assertEquals(FRONT, getFieldRelativeOrientation(FRONT, new Heading(90)));
        assertEquals(BACK, getFieldRelativeOrientation(BACK, new Heading(90)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(90)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(90)));

        assertEquals(FRONT, getFieldRelativeOrientation(FRONT, new Heading(120)));
        assertEquals(BACK, getFieldRelativeOrientation(BACK, new Heading(120)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(120)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(120)));

        assertEquals(FRONT, getFieldRelativeOrientation(FRONT, new Heading(56)));
        assertEquals(BACK, getFieldRelativeOrientation(BACK, new Heading(56)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(56)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(56)));
    }

    @Test
    public void getFieldRelativeOrientationBackward() {
        assertEquals(BACK, getFieldRelativeOrientation(FRONT, new Heading(270)));
        assertEquals(FRONT, getFieldRelativeOrientation(BACK, new Heading(270)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(270)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(270)));

        assertEquals(BACK, getFieldRelativeOrientation(FRONT, new Heading(240)));
        assertEquals(FRONT, getFieldRelativeOrientation(BACK, new Heading(240)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(240)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(240)));

        assertEquals(BACK, getFieldRelativeOrientation(FRONT, new Heading(310)));
        assertEquals(FRONT, getFieldRelativeOrientation(BACK, new Heading(310)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(310)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(LEFT_SIDE, new Heading(310)));
    }

    @Test
    public void getFieldRelativeOrientationLeftward() {
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(180)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(BACK, new Heading(180)));
        assertEquals(BACK, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(180)));
        assertEquals(FRONT, getFieldRelativeOrientation(LEFT_SIDE, new Heading(180)));

        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(140)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(BACK, new Heading(140)));
        assertEquals(BACK, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(140)));
        assertEquals(FRONT, getFieldRelativeOrientation(LEFT_SIDE, new Heading(140)));

        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(220)));
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(BACK, new Heading(220)));
        assertEquals(BACK, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(220)));
        assertEquals(FRONT, getFieldRelativeOrientation(LEFT_SIDE, new Heading(220)));
    }

    @Test
    public void getFieldRelativeOrientationRightward() {
        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(0)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(BACK, new Heading(0)));
        assertEquals(FRONT, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(0)));
        assertEquals(BACK, getFieldRelativeOrientation(LEFT_SIDE, new Heading(0)));

        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(33)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(BACK, new Heading(33)));
        assertEquals(FRONT, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(33)));
        assertEquals(BACK, getFieldRelativeOrientation(LEFT_SIDE, new Heading(33)));

        assertEquals(LEFT_SIDE, getFieldRelativeOrientation(FRONT, new Heading(330)));
        assertEquals(RIGHT_SIDE, getFieldRelativeOrientation(BACK, new Heading(330)));
        assertEquals(FRONT, getFieldRelativeOrientation(RIGHT_SIDE, new Heading(330)));
        assertEquals(BACK, getFieldRelativeOrientation(LEFT_SIDE, new Heading(330)));
    }
}