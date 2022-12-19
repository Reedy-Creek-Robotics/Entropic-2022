package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class Turret extends BaseComponent {

    private static double MAXIMUM = 1.0;
    private static double MINIMUM = 0.0;
    private static double THRESHOLD = 0.05;

    private Servo servo;

    public enum Orientation {
        FRONT(0.0),
        BACK(0.5),
        LEFT_SIDE(1.0);

        private double servoPosition;

        Orientation(double servoPostion) {
            this.servoPosition = servoPostion;
        }

        public double getServoPosition(){
            return servoPosition;
        }

    }

    public Turret(RobotContext context) {
        super(context);
        servo = hardwareMap.servo.get("Turret");
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
            servo.setPosition(servoPosition);
            servo.getController().pwmEnable();
        }

        @Override
        public void stop() {
            servo.setPosition(servo.getPosition());
            servo.getController().pwmEnable();
        }

        @Override
        public boolean updateStatus() {
            return Math.abs(servo.getPosition() - servoPosition) < THRESHOLD;
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
