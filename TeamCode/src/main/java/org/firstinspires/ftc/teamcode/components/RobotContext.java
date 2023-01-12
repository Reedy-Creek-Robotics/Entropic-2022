package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

public class RobotContext {

    public OpMode opMode;

    public RobotDescriptor robotDescriptor;

    public RobotPositionProvider robotPositionProvider;

    public RobotContext(OpMode opMode, RobotDescriptor robotDescriptor) {
        this.opMode = opMode;
        this.robotDescriptor = robotDescriptor;
    }

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */
    public interface RobotPositionProvider {

        Position getPosition();

        Heading getHeading();

    }

}
