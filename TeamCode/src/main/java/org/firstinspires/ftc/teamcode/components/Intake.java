package org.firstinspires.ftc.teamcode.components;

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
        double power;
        double time;

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
    }

    private class IntakeCone extends BaseCommand {
        public IntakeCone(double power, double time) {
            super(power, time);
        }

        @Override
        public boolean updateStatus() {
            return time > commandTime.seconds() || (touchSensor != null ? touchSensor.isPressed() : false);
        }
    }

    private class OutakeCone extends BaseCommand {
        public OutakeCone(double power, double time) {
            super(power, time);
        }

        @Override
        public boolean updateStatus() {
            return time > commandTime.seconds();
        }
    }

}
