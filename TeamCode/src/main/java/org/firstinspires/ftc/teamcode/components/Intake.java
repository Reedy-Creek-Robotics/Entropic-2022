package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Command;

public class Intake extends BaseComponent {
    private CRServo intakeServo;

    public Intake(OpMode opMode) {
        super(opMode);
        intakeServo = hardwareMap.crservo.get("IntakeWheel");
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
        double power;

        public BaseCommand(double power) {
            this.power = power;
        }

        @Override
        public void stop() {
            stopIntake();
        }

        @Override
        public boolean updateStatus() {
            return true;
        }
    }

    private class IntakeCone extends BaseCommand {

        public IntakeCone(double power) {
            super(power);
        }

        @Override
        public void start() {
            time.reset();
            intakeServo.setPower(power);
        }
    }
}
