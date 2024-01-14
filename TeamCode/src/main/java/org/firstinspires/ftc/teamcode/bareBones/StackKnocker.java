package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.RobotContext;

public class StackKnocker extends BaseComponent {

    // todo: tune positions
    private static final double UP_POSITION = 0.5;
    private static final double DOWN_POSITION = 0.6;

    private boolean isDown = false;

    private Servo servo;

    public StackKnocker(RobotContext context) {
        super(context);

        this.servo = hardwareMap.get(Servo.class, "StackKnocker");
    }

    public void toggle() {
        if (isDown) {
            knockUp();
        } else {
            knockDown();
        }
    }

    public void knockUp() {
        servo.setPosition(UP_POSITION);
        isDown = false;
    }

    public void knockDown() {
        servo.setPosition(DOWN_POSITION);
        isDown = true;
    }

}
