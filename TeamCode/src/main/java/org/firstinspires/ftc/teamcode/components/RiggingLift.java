package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class RiggingLift extends BaseComponent {

    private static final double RIGGING_LIFT_POWER = 1.0;

    public static final int MAX_HEIGHT = 4100 ;
    public static final int MIN_HEIGHT = 0 + 5;

    private DcMotorEx motor;

    public RiggingLift(RobotContext context) {
        super(context);

        motor = hardwareMap.get(DcMotorEx.class, "RiggingLift");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveUp() {
        setMotorPower(RIGGING_LIFT_POWER);
    }

    public void moveDown() {
        setMotorPower(-RIGGING_LIFT_POWER);
    }

    private int getMotorPosition(){
        return motor.getCurrentPosition();
    }
    private void setMotorPower(double power) {
        if(getMotorPosition() > MIN_HEIGHT && getMotorPosition() < MAX_HEIGHT){
            motor.setPower(power);
        } else if (getMotorPosition() < MIN_HEIGHT && power > 0) {
            motor.setPower(power);
        } else if (getMotorPosition() > MAX_HEIGHT && power < 0){
            motor.setPower(power);
        }
    }

    public void stop() {
        setMotorPower(0);
    }
}
