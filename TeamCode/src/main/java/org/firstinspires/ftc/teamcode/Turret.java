package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Turret extends BaseComponent{

    private static double MAXIMUM = 1.0;
    private static double MINIMUM = 0.0;

    private Servo servo;

    public Turret(OpMode opMode) {
        super(opMode);
        servo = hardwareMap.servo.get("turret");
    }

    @Override
    public void init() {
        super.init();
    }

    private class MoveToPosition implements Command {

        private double position;

        public MoveToPosition(double position) {
            this.position = position;
        }

        @Override
        public void start() {
            if(position > MAXIMUM)
                position = MAXIMUM;
            else if(position < MINIMUM)
                position = MINIMUM;

            servo.setPosition(position);
            servo.getController().pwmEnable();
        }

        @Override
        public void stop() {
            servo.setPosition(servo.getPosition());
            servo.getController().pwmEnable();
        }

        @Override
        public boolean updateStatus() {
            //Do nothing
        }
    }
}
