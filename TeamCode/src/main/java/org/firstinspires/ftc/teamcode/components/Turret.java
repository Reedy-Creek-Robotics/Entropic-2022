package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class Turret extends BaseComponent {

    private static double MAXIMUM = 1;
    private static double MINIMUM = .3;
    private static double THRESHOLD = 0.05;

    private Servo servo;
    private SafetyCheck safetyCheck;

    public enum Orientation {
        FRONT(.31),
        BACK(.95),
        LEFT_SIDE(.64),
        START(.4);

        private double servoPosition;

        Orientation(double servoPostion) {
            this.servoPosition = servoPostion;
        }

        public double getServoPosition() {
            return servoPosition;
        }

    }

    public Turret(RobotContext context, SafetyCheck safetyCheck) {
        super(context);
        servo = hardwareMap.servo.get("Turret");
        this.safetyCheck = safetyCheck;
    }

    public double getTurretPosition() {
        return servo.getPosition();
    }

    public void stopTurret() {
        servo.getController().pwmDisable();
    }

    public void moveToOrientation(Orientation orientation) {
        if (isSafeToMove()) {
            executeCommand(new MoveToOrientation(orientation));
        }
    }

    public void moveToPosition(double position) {
        if (isSafeToMove()) {
            executeCommand(new MoveToPosition(position));
        }
    }

    public boolean isSafeToMove() {
        return safetyCheck == null || safetyCheck.isSafeToMove();
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

    interface SafetyCheck {

        /**
         * Indicates if its currently safe for the turret to move to the given position.
         */
        boolean isSafeToMove();

    }

}
