package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@TeleOp
@Disabled
public class MechanismTest extends OpMode{
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;

    public DcMotor arm;
    public DcMotor intake;

    public static final double YELLOW_JACKET = 537.6;
    @Override
    public void init()
    {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");

        //arm = hardwareMap.dcMotor.get("arm");
        //intake = hardwareMap.dcMotor.get("intake");

        FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD );
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BackRight.setDirection((DcMotorSimple.Direction.REVERSE));


    }
    @Override
    public void loop() {
        motorTest(FrontLeft,gamepad1.x,1);
        motorTest(BackLeft,gamepad1.a,1);
        motorTest(FrontRight,gamepad1.y,1);
        motorTest(BackRight,gamepad1.b,1);
    }
    public void motorTest(DcMotor motor,Boolean pressed,double speed){
        double state = 1;
        if (pressed){
            state+=1;
            motor.setPower(((state%3)-1)*speed);
        }

    }
}
