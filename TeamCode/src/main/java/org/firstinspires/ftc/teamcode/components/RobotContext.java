package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDescriptor;

public class RobotContext {

    private OpMode opMode;

    private RobotDescriptor robotDescriptor;

    private Telemetry telemetry;

    public RobotContext(OpMode opMode, RobotDescriptor robotDescriptor) {
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
        this.robotDescriptor = robotDescriptor;
    }

    public OpMode getOpMode() {
        return opMode;
    }

    public RobotDescriptor getRobotDescriptor() {
        return robotDescriptor;
    }

}
