package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.geometry.Heading;

import java.util.Arrays;

public class Turret extends BaseComponent {

    private static double MAXIMUM = 1;
    private static double MINIMUM = .3;

    private Servo servo;
    private SafetyCheck safetyCheck;

    private double targetPosition = Orientation.FRONT.servoPosition;

    /**
     * Indicates if the turret has been started, meaning its safety check has passed (i.e. the linear slide has raised
     * to the correct height), and a target position set.
     */
    private boolean turrentStarted = false;

    public enum Orientation {
        FRONT(0.313),
        RIGHT_SIDE(0.681),
        BACK(1.000),
        LEFT_SIDE(0.681);

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

    public double getTargetPosition() {
        return targetPosition;
    }

    public void stopTurret() {
        servo.getController().pwmDisable();
    }

    public void moveTurretManually(double targetPosition) {
        servo.setPosition(targetPosition);
    }

    public void moveToOrientation(Orientation orientation) {
        if (isSafeToMove()) {
            executeCommand(new MoveToOrientation(orientation));
        }
    }

    public static Orientation getFieldRelativeOrientation(Orientation orientation, Heading heading) {
        Heading heading1 = heading.minus(45);
        int rotation = (int) heading1.getValue() / 90;

        int currentIndex = orientation.ordinal();

        Orientation[] orientations = Orientation.values();
        return orientations[(currentIndex + rotation) % orientations.length];
    }

    public void moveToPosition(double position) {
        if (isSafeToMove()) {
            this.targetPosition = position;
            executeCommand(new MoveToPosition(position));
        }
    }

    public void updateStatus() {
        if (!turrentStarted && isSafeToMove()) {
            turrentStarted = true;
            moveToPosition(targetPosition);
        }

        super.updateStatus();
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
