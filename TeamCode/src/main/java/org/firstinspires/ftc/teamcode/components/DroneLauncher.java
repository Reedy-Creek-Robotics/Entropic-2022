package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class DroneLauncher extends BaseComponent {

    //TODO: Tuned positions needed
    public static final double LAUNCH_POSITION = 3;
    public static final int START_POSITION = 0;
    private Servo servo;

    public DroneLauncher(RobotContext context) {
        super(context);
        servo = hardwareMap.get(Servo.class, "droneLaunchServo");

    }

    public void launch() {
        servo.setPosition(LAUNCH_POSITION);
        servo.setPosition(START_POSITION);
    }
}
