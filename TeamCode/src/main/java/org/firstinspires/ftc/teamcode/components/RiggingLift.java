package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class RiggingLift extends BaseComponent {

    private static final double RIGGING_LIFT_POWER = 1.0;

    private DcMotorEx motor;

    public RiggingLift(RobotContext context) {
        super(context);

        motor = hardwareMap.get(DcMotorEx.class, "RiggingLift");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void moveUp() {
        setMotorPower(RIGGING_LIFT_POWER);
    }

    public void moveDown() {
        setMotorPower(-RIGGING_LIFT_POWER);
    }

    private void setMotorPower(double power) {
        motor.setPower(power);
    }

    public void stop() {
        setMotorPower(0);
    }
}
