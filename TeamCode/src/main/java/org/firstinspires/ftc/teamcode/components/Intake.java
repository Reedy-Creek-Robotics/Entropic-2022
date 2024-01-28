package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends BaseComponent {

    private DcMotorEx intakeMotor;
    private Servo flicker;


    public Intake(RobotContext context) {
        super(context);
        intakeMotor = (DcMotorEx) hardwareMap.dcMotor.get("Intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
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

    public void autoOuttakeManual() {
        stopAllCommands();
        intakeMotor.setPower(-0.1);
    }

    /**
     * Stops the intake
     */
    public void stopIntake() {
        intakeMotor.setPower(0);
    }

    public void rollOut(double power){
        stopAllCommands();
        executeCommand(new Roll(-power,1000));
    }

    public void intake(double power){
        executeCommand(new Roll(power,3000));
    }



    private class Roll implements Command {
        private ElapsedTime timer = new ElapsedTime();

        private double power;
        private double time;

        public Roll(double power, double time) {
            this.power = power;
            this.time = time;
        }

        @Override
        public void start() {
            timer.reset();
            intakeMotor.setPower(power);
        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= time;
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
