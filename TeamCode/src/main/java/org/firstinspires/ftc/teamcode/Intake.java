package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;

public class Intake extends BaseComponent {
    private CRServo intakeServo;
    private Door door;

    //todo look at the ElapsedTime time object

    public Intake(OpMode opMode) {
        super(opMode);
        intakeServo = hardwareMap.crservo.get("intakeWheel");
        this.door = new Door(opMode);
        addSubComponents(door);
    }

    /**
     * @param timeToSpin in seconds
     */
    public void intakeForward(double timeToSpin) {
        executeCommand(new IntakeForward(timeToSpin));
    }

    public void test(int timeToSpin) {
        intakeServo.setPower(0.5);
        stopIntake();
    }

    public void intakeBackward(int timeToSpin) {
        executeCommand(new IntakeBackward(timeToSpin));
    }

    public Door getDoor() {
        return door;
    }

    /**
     * Stops the intake
     */
    public void stopIntake() {
        intakeServo.setPower(0);
    }

    private abstract class BaseCommand implements Command {
        //time in seconds
        double timeToSpin;

        public BaseCommand(double timeToSpin) {
            this.timeToSpin = timeToSpin;
        }

        @Override
        public void stop() {
            stopIntake();
        }

        @Override
        public boolean updateStatus() {
            return time.seconds() > timeToSpin;
        }
    }

    private class IntakeForward extends BaseCommand {

        public IntakeForward(double timeToSpin) {
            super(timeToSpin);
        }

        @Override
        public void start() {
            time.reset();
            intakeServo.setPower(0.5);

        }
    }

    private class IntakeBackward extends BaseCommand {
        public IntakeBackward(double timeToSpin) {
            super(timeToSpin);
        }

        @Override
        public void start() {
            time.reset();
            intakeServo.setPower(-0.5);
        }
    }
}
