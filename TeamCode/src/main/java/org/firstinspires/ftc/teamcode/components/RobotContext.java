package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotDescriptor;

public class RobotContext {

    public OpMode opMode;

    public RobotDescriptor descriptor;

    public RobotPositionProvider robotPositionProvider;

    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
    }

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */


}
