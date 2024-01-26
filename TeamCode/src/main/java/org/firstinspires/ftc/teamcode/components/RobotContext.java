package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;

import java.util.ArrayList;
import java.util.List;

public class RobotContext{

    public OpMode opMode;

    public RobotDescriptor descriptor;

    public Localizer localizer;

    public DriveUtil driveUtil;

    List<Integer> lastTrackingEncPositions = new ArrayList<>();
    List<Integer> lastTrackingEncVels = new ArrayList<>();
    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
        this.localizer = new StandardTrackingWheelLocalizer(opMode.hardwareMap,
                lastTrackingEncPositions,
                lastTrackingEncVels,
                this.descriptor.ODOMETRY_TUNER
        );
    }

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */


}
