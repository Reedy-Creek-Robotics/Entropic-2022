package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class DroneLauncher extends BaseComponent {

    //TODO: Tuned positions needed
    public static final double LAUNCH_POSITION = 0.55;
    public static final double START_POSITION = 1;
    private Servo servo;

    public DroneLauncher(RobotContext context) {
        super(context);
        servo = hardwareMap.get(Servo.class, "DroneLaunchServo");

    }

    @Override
    public void init() {
        super.init();
    }

    public void load(){
        servo.setPosition(START_POSITION);
    }

    public void launch() {
        servo.setPosition(LAUNCH_POSITION);
    }
}
