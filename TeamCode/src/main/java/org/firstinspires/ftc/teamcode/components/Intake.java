package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;

public class Intake extends BaseComponent {
    private CRServo intakeServo;

    public Intake(RobotContext context) {
        super(context);
        intakeServo = hardwareMap.crservo.get("IntakeWheel");
    }

    /**
     * @param power of the servo
     */
    public void intake(double power, double time) {
        executeCommand(new BaseCommand(power,time));
    }

    public void outake(double power, double time) {
        executeCommand(new BaseCommand(-power,time));
    }

    /**
     * Stops the intake
     */
    public void stopIntake() {
        intakeServo.setPower(0);
    }

    private class BaseCommand implements Command {
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

        @Override
        public boolean updateStatus() {
            return time > commandTime.seconds();
        }
    }

}
