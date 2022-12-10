package org.firstinspires.ftc.teamcode.util;

import static org.junit.Assert.assertEquals;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.junit.Test;

public class RampUtilTest {

    private static final double E = 1e-6;

    private static RobotDescriptor descriptor = new RobotDescriptor();

    @Test
    public void calculateRampingFactor_minPower() {
        descriptor.rampingMinMotorPower = 0.25;

        double power = RampUtil.calculateRampingFactor(
                descriptor,
                new Position(0, 0), new Position(0, 10),
                new Vector2(0, 0),
                0.005
        );

        // In spite of very low speed factor, the min power applied shouldn't be less than the min
        assertEquals(0.25, power, E);
    }

}
