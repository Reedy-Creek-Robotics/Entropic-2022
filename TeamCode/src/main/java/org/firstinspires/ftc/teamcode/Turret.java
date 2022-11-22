package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Turret extends BaseComponent{

    private static double MAXIMUM = 1.0;
    private static double MINIMUM = 0.0;
    private static double THRESHOLD = 0.05;

    private Servo servo;

    public Turret(OpMode opMode) {
        super(opMode);
        servo = hardwareMap.servo.get("turret");
    }

    private class MoveToPosition implements Command {

        private double desiredPosition;

        public MoveToPosition(double position) {
            this.desiredPosition = position;
        }

        @Override
        public void start() {
            if(desiredPosition > MAXIMUM)
                desiredPosition = MAXIMUM;
            else if(desiredPosition < MINIMUM)
                desiredPosition = MINIMUM;

            servo.setPosition(desiredPosition);
            servo.getController().pwmEnable();
        }

        @Override
        public void stop() {
            servo.setPosition(servo.getPosition());
            servo.getController().pwmEnable();
        }

        @Override
        public boolean updateStatus() {
            return Math.abs(servo.getPosition() - desiredPosition) < THRESHOLD;
        }
    }
}
