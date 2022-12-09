package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotDescriptor;

public class RobotContext {

    public OpMode opMode;

    public RobotDescriptor robotDescriptor;

    public RobotContext(OpMode opMode, RobotDescriptor robotDescriptor) {
        this.opMode = opMode;
        this.robotDescriptor = robotDescriptor;
    }

}
