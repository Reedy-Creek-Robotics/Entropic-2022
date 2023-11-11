package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.AssertUtil.E;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScalingUtilTest {

    @Test
    public void scaleLinear() {
        assertEquals(0.0, ScalingUtil.scaleLinear(0.0, 0.0, 1.0, 0.0, 1.0), E);
        assertEquals(0.5, ScalingUtil.scaleLinear(0.5, 0.0, 1.0, 0.0, 1.0), E);
        assertEquals(1.0, ScalingUtil.scaleLinear(1.0, 0.0, 1.0, 0.0, 1.0), E);

        assertEquals(1.0, ScalingUtil.scaleLinear(0.25, 0.0, 1.0, 0.0, 4.0), E);
        assertEquals(4.0, ScalingUtil.scaleLinear(2.0, 0.0, 5.0, 0.0, 10.0), E);
        assertEquals(10.0, ScalingUtil.scaleLinear(2.0, -3.0, 7.0, -10.0, 30.0), E);

        assertEquals(0.0, ScalingUtil.scaleLinear(0.0, 1.0, -1.0, -1.0, 1.0), E);
        assertEquals(1.0, ScalingUtil.scaleLinear(-1.0, 1.0, -1.0, -1.0, 1.0), E);
        assertEquals(-1.0, ScalingUtil.scaleLinear(1.0, 1.0, -1.0, -1.0, 1.0), E);
    }

    @Test
    public void scaleQuadratic() {
        assertEquals(0.0, ScalingUtil.scaleQuadratic(0.0, 0.0, 1.0, 0.0, 1.0), E);
        assertEquals(0.25, ScalingUtil.scaleQuadratic(0.5, 0.0, 1.0, 0.0, 1.0), E);
        assertEquals(1.0, ScalingUtil.scaleQuadratic(1.0, 0.0, 1.0, 0.0, 1.0), E);

        assertEquals(0.25, ScalingUtil.scaleQuadratic(0.25, 0.0, 1.0, 0.0, 4.0), E);
        assertEquals(1.6, ScalingUtil.scaleQuadratic(2.0, 0.0, 5.0, 0.0, 10.0), E);
        assertEquals(9.6, ScalingUtil.scaleQuadratic(4.0, -3.0, 7.0, -10.0, 30.0), E);
    }


}
