package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Capper extends BaseComponent {

    /**
     * The servo for the capper, range 0 - 270 degrees.
     */
    private Servo servo;

    public enum Position{
        //todo adjust
        START(0),
        TRAVEL(.2),
        ;

        double position;

        Position(double position) {
            this.position = position;
        }
    }

    public Capper(OpMode opMode) {
        super(opMode);
        servo = hardwareMap.servo.get("capServo");
    }

    public void goToPosition(Position position) {
        time.reset();
        servo.setPosition(position.position);
        servo.getController().pwmEnable();
    }

    @Override
    public void updateStatus() {
        //Do nothing
    }

    @Override
    public boolean isBusy() {
        return time.milliseconds() < 2000;
    }
}
