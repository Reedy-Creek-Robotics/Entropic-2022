package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.Door.Position.CLOSED;
import static org.firstinspires.ftc.teamcode.Door.Position.OPEN;

public class Door extends BaseComponent{

    private static final double THRESHOLD = 0.02;

    private Position state;

    public enum Position{
        //todo adjust
        OPEN(.3),
        CLOSED(0.0);

        double position;

        Position(double position) {
            this.position = position;
        }
    }


    private Servo servo;

    public Door(OpMode opMode) {
        super(opMode);
        this.servo = hardwareMap.servo.get("intakeDoor");
        state = CLOSED;
    }

    public Position getPosition() {
        return state;
    }

    public void openDoor() {
        time.reset();
        state = OPEN;
        servo.setPosition(state.position);
        servo.getController().pwmEnable();
    }

    public void closeDoor() {
        time.reset();
        state = CLOSED;
        servo.setPosition(state.position);
        servo.getController().pwmEnable();
    }

    @Override
    public void updateStatus() {
        //Do nothing
    }

    @Override
    public boolean isBusy() {
        return time.milliseconds() < 1500;
    }
}
