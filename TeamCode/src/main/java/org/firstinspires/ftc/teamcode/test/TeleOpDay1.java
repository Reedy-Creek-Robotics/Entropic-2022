package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class TeleOpDay1 extends OpMode {
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;

    public DcMotor arm;
    public Servo intakeWheel;

    public static final double YELLOW_JACKET = 537.6;
    @Override
    public void init()
    {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");

        arm = hardwareMap.dcMotor.get("arm");
        intakeWheel = hardwareMap.servo.get("intakeWheel");
        //intake = hardwareMap.dcMotor.get("intake");

        FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD );
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BackRight.setDirection((DcMotorSimple.Direction.REVERSE));
    }
    @Override
    public void loop() {
        double D = gamepad1.left_stick_y ;
        double T = -gamepad1.right_stick_x ;
        double S = gamepad1.left_stick_x;


        FrontLeft.setPower(+D + T - S);
        FrontRight.setPower(+D - T + S );
        BackRight.setPower(+D - T - S);
        BackLeft.setPower(+D + T + S);

        //arm ticks per revolution 537.6
        double A = gamepad2.right_stick_y/2;
        //arm.setPower(A);

        if (gamepad1.a){
            move(0.7,10,arm);
        }
        if (gamepad1.y){
            move(0.7,-10,arm);
        }

        if(gamepad1.b) {
            intakeWheel.setPosition(.75);
        }else {
            intakeWheel.setPosition(.5);
        }


    }
    public void move(double speed,int move,DcMotor motor){
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(speed);
        int strtPos = motor.getCurrentPosition();
        motor.setTargetPosition(strtPos + move);
    }
}