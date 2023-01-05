package org.firstinspires.ftc.teamcode.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Intake extends BaseComponent {

    private CRServo intakeServo;
    private TouchSensor touchSensor;

    public Intake(RobotContext context) {
        super(context);
        intakeServo = hardwareMap.crservo.get("Intake");
        touchSensor = hardwareMap.touchSensor.get("IntakeTouchSensor");
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
        telemetry.addData("Next Commands",getNextCommands());
    }

    public void intakeManual() {
        stopAllCommands();
        intakeServo.setPower(1);
    }

    public void outakeManual() {
        stopAllCommands();
        intakeServo.setPower(-1);
    }

    /**
     * @param power of the servo
     */
    public void intake(double power, double time) {
        executeCommand(new IntakeCone(power,time));
    }

    public void outake(double power, double time) {
        executeCommand(new OutakeCone(-power,time));
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
            telemetry.addData("Am I beeping and booping:","yes I am");
            return commandTime.seconds() > getTime();//|| (touchSensor != null ? touchSensor.isPressed() : false);
        }
    }

    private class OutakeCone extends BaseCommand {
        public OutakeCone(double power, double time) {
            super(power, time);
        }

        @Override
        public boolean updateStatus() {
            telemetry.addData("Am I beeping and booping:","yes I am");
            return commandTime.seconds() > getTime();
        }
    }

}
