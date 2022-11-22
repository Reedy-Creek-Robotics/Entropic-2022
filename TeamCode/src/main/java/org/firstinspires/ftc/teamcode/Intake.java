package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;

public class Intake extends BaseComponent{
    private CRServo intakeServo;

    public Intake(OpMode opMode) {
        super(opMode);
        intakeServo = hardwareMap.crservo.get("intakeWheel");
    }

    /**
     * @param power of the servo
     */
    public void intake(double power) {
        executeCommand(new IntakeCone(power));
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

        double power;

        public BaseCommand(double timeToSpin, double power) {
            this.timeToSpin = timeToSpin;
            this.power = power;
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

    private class IntakeCone extends BaseCommand {

        public IntakeCone(double power) {
            super(.5,power);
        }

        @Override
        public void start() {
            time.reset();
            intakeServo.setPower(power);
        }
    }
}
