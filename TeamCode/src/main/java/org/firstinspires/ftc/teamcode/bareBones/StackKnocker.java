package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.RobotContext;

//TODO: All of it

public class StackKnocker extends BaseComponent {

    public StackKnocker(RobotContext context) {
        super(context);
    }

    @Override
    public void init() {
        CRServo pinionMotor = hardwareMap.crservo.get("StackKnocker");
    }

    @Override
    public void updateStatus() {

    }

    @Override
    public boolean isBusy() {
        return false;

    }
}