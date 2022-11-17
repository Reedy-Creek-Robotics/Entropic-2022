package org.firstinspires.ftc.teamcode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HeadingTest {
    
    private static final double E = 1e-6; 

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
