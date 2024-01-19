package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake extends BaseComponent {

    private DcMotorEx intakeMotor;
    private Servo flicker;


    public Intake(RobotContext context) {
        super(context);
        intakeMotor = (DcMotorEx) hardwareMap.dcMotor.get("Intake");
        //flicker = hardwareMap.servo.get("flicker"); //TODO: figure out what this is supposed to do and bring back
    }

    @Override
    public void update() {
        super.update();
        telemetry.addData("Next Commands", getNextCommands());
    }

    public void intakeManual() {
        stopAllCommands();
        intakeMotor.setPower(1);
    }

    public void outtakeManual() {
        stopAllCommands();
        intakeMotor.setPower(-1);
    }

    /**
     * Stops the intake
     */
    public void stopIntake() {
        intakeMotor.setPower(0);
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
            intakeMotor.setPower(power);
        }

        @Override
        public void stop() {
            stopIntake();
        }

        public double getTime() {
            return time;
        }
    }
}
