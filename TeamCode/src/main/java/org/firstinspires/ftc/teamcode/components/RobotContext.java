package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;

import java.util.ArrayList;
import java.util.List;

public class RobotContext{

    public OpMode opMode;

    public RobotDescriptor descriptor;

    public Localizer localizer;

    public DriveUtil driveUtil;

    public Alliance alliance;

    List<Integer> lastTrackingEncPositions = new ArrayList<>();
    List<Integer> lastTrackingEncVels = new ArrayList<>();
    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
        this.localizer = new TwoWheelTrackingLocalizer(opMode.hardwareMap,this.descriptor);
        this.alliance = Alliance.RED;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public enum Alliance{
        BLUE(1,-90),
        RED(-1,90);

        int value;
        int rotation;

        Alliance(int value, int rotation) {
            this.value = value;
            this.rotation = rotation;
        }

        public int getValue() {
            return value;
        }

        public int getRotation() {
            return rotation;
        }
    }

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */


}
