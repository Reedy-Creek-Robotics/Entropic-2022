package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class Turret extends BaseComponent {

    private static double MAXIMUM = .7;
    private static double MINIMUM = .5;
    private static double THRESHOLD = 0.05;

    private Servo servo;

    public enum Orientation {
        FRONT(.3),
        BACK(.7),
        LEFT_SIDE(.4),
        START(.4);

        private double servoPosition;

        Orientation(double servoPostion) {
            this.servoPosition = servoPostion;
        }

        public double getServoPosition() {
            return servoPosition;
        }

    }

    public Turret(RobotContext context) {
        super(context);
        servo = hardwareMap.servo.get("Turret");
    }

    public double getTurretPosition() {
        return servo.getPosition();
    }

    public void stopTurret() {
        servo.getController().pwmDisable();
    }

    public void moveToOrientation(Orientation orientation) {
        executeCommand(new MoveToOrientation(orientation));
    }

    public void moveToPosition(double position) {
        executeCommand(new MoveToPosition(position));
    }

    private abstract class BaseCommand implements Command {
        private double servoPosition;

        public BaseCommand(double servoPosition) {
            this.servoPosition = servoPosition;
        }

        @Override
        public void start() {
            if (servoPosition > MAXIMUM) {
                servoPosition = MAXIMUM;
            } else if (servoPosition < MINIMUM) {
                servoPosition = MINIMUM;
            }

            servo.setPosition(servoPosition);
            servo.getController().pwmEnable();
        }

        @Override
        public void stop() {

        }

        @Override
        public boolean updateStatus() {
            return true;
        }
    }

    private class MoveToOrientation extends BaseCommand {
        public MoveToOrientation(Orientation orientation) {
            super(orientation.getServoPosition());
        }
    }

    private class MoveToPosition extends BaseCommand {
        public MoveToPosition(double servoPosition) {
            super(servoPosition);
        }
    }
}
