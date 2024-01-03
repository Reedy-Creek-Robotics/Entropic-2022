package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;

public class RobotContext{

    public OpMode opMode;

    public RobotDescriptor descriptor;

    public Localizer localizer;

    public DriveUtil driveUtil;

    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
    }

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */


}
