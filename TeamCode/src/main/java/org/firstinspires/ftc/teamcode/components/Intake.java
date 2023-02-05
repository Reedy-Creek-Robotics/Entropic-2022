package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Intake extends BaseComponent {

    private CRServo intakeServo;

    public Intake(RobotContext context) {
        super(context);
        intakeServo = hardwareMap.crservo.get("Intake");
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
        telemetry.addData("Next Commands", getNextCommands());
    }

    public void intakeManual() {
        stopAllCommands();
        intakeServo.setPower(1);
    }

    public void outakeManual() {
        stopAllCommands();
        intakeServo.setPower(-1);
    }

    public void intake(double power, double time) {
        executeCommand(new IntakeCone(power, time));
    }

    public void intake(double time) {
        intake(1.0, time);
    }

    public void outtake(double power, double time) {
        executeCommand(new OuttakeCone(-power, time));
    }

    public void outtake(double time) {
        outtake(1.0, time);
    }

    /**
     * Stops the intake
     */
    public void stopIntake() {
        intakeServo.setPower(0);
    }

    private abstract class BaseCommand implements Command {
        private double power;
        private double time;

        public BaseCommand(double power, double time) {
            this.power = power;
            this.time = time;
        }

        @Override
        public void start() {
            intakeServo.setPower(power);
        }

        @Override
        public void stop() {
            stopIntake();
        }

        public double getTime() {
            return time;
        }
    }

    private class IntakeCone extends BaseCommand {

        public IntakeCone(double power, double time) {
            super(power, time);
        }

        @Override
        public boolean updateStatus() {
            return commandTime.seconds() > getTime();
        }
    }

    private class OuttakeCone extends BaseCommand {
        public OuttakeCone(double power, double time) {
            super(power, time);
        }

        @Override
        public boolean updateStatus() {
            return commandTime.seconds() > getTime();
        }
    }

}
