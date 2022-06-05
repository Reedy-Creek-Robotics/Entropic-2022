package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;
import java.util.List;

public class DuckSpinner extends BaseComponent {

    private DcMotorEx duckSpinnerLeft;
    private DcMotorEx duckSpinnerRight;
    private List<DcMotorEx> motors;

    public DuckSpinner(OpMode opMode) {
        super(opMode);

        duckSpinnerLeft = (DcMotorEx) hardwareMap.dcMotor.get("duckSpinnerL");
        duckSpinnerRight = (DcMotorEx) hardwareMap.dcMotor.get("duckSpinnerR");
        motors = new ArrayList<>();
        motors.add(duckSpinnerLeft);
        motors.add(duckSpinnerRight);
    }

    /**
     * @param power
     * @param time time to spin in seconds
     */
    public void spinWheels(double power,double time) {
        executeCommand(new SpinWheels(power,time));
    }

    public void stopMotors() {
        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
    }

    public class SpinWheels implements Command {
        double power;
        //in seconds
        double timeToSpin;

        public SpinWheels(double power, double timeToSpin) {
            this.power = power;
            this.timeToSpin = timeToSpin;
        }

        @Override
        public void stop() {
            stopMotors();
        }

        @Override
        public void start() {
            duckSpinnerLeft.setPower(power);
            duckSpinnerRight.setPower(-power);
            time.seconds();
        }

        @Override
        public boolean updateStatus() {
            return time.seconds() > timeToSpin;
        }
    }
}
