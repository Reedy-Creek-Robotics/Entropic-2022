package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HeadingTest {
    
    @Test
    public void getValue() {

        assertEquals(0.0, new Heading(0).getValue(), E);
        assertEquals(0.0, new Heading(360).getValue(), E);
        assertEquals(0.0, new Heading(-360).getValue(), E);

        assertEquals(1.0, new Heading(-359).getValue(), E);
        assertEquals(359.0, new Heading(-1).getValue(), E);

        assertEquals(0.1, new Heading(720.1).getValue(), E);

    }

    @Test
    public void alignToRightAngle() {

        assertEquals(0.0, new Heading(0).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(1).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(-1).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(-44).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(44).alignToRightAngle().getValue(), E);

        assertEquals(90.0, new Heading(45).alignToRightAngle().getValue(), E);
        assertEquals(90.0, new Heading(90).alignToRightAngle().getValue(), E);
        assertEquals(90.0, new Heading(134).alignToRightAngle().getValue(), E);

        assertEquals(180.0, new Heading(135).alignToRightAngle().getValue(), E);
        assertEquals(180.0, new Heading(180).alignToRightAngle().getValue(), E);
        assertEquals(180.0, new Heading(224).alignToRightAngle().getValue(), E);

        assertEquals(270, new Heading(225).alignToRightAngle().getValue(), E);
        assertEquals(270, new Heading(270).alignToRightAngle().getValue(), E);
        assertEquals(270, new Heading(314).alignToRightAngle().getValue(), E);

        assertEquals(0.0, new Heading(315).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(359).alignToRightAngle().getValue(), E);
        assertEquals(0.0, new Heading(360).alignToRightAngle().getValue(), E);

    }

    @Test
    public void add() {

        assertEquals(0.0, new Heading(0).add(0.0).getValue(), E);
        assertEquals(1.0, new Heading(0).add(1.0).getValue(), E);
        assertEquals(359.0, new Heading(0).add(-1.0).getValue(), E);

    }

    @Test
    public void delta() {

        assertEquals(0.0, new Heading(0).delta(new Heading(0)), E);
        assertEquals(0.0, new Heading(360).delta(new Heading(0)), E);
        assertEquals(0.0, new Heading(720).delta(new Heading(0)), E);

        assertEquals(180.0, new Heading(180).delta(new Heading(0)), E);
        assertEquals(180.0, new Heading(540).delta(new Heading(0)), E);
        assertEquals(180.0, new Heading(900).delta(new Heading(0)), E);

        assertEquals(-179.9, new Heading(900.1).delta(new Heading(0)), E);

        assertEquals(1.0, new Heading(2).delta(new Heading(1)), E);
        assertEquals(-1.0, new Heading(1).delta(new Heading(2)), E);

    }

}
