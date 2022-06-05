package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
@Disabled
@Autonomous
public class AutoDay1 extends LinearOpMode{
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;
    public DigitalChannel beamBreaker;
    public DcMotor arm;
    double right;
    double left;
    public static final double YELLOW_JACKET = 537.6;
    @Override

    public void runOpMode() throws InterruptedException {
        /*
        //assigns motors to variables
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");
        beamBreaker = hardwareMap.digitalChannel.get("beamBreaker");
        //set direction of motors
        FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD );
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BackRight.setDirection((DcMotorSimple.Direction.REVERSE));
        */

        arm = hardwareMap.dcMotor.get("arm");
        //End of setup

        //movement
        waitForStart();
        test();

    }
    public void move(double speed,int move,DcMotor motor){
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(speed);
        int strtPos = motor.getCurrentPosition();
        motor.setTargetPosition(strtPos + move);
    }

    //Where we write the tester code
    public void test() {
        while (opModeIsActive()){
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //telemetry.addData("beamBreaker: ", beamBreaker.getState());
            telemetry.addData("ticks: ", arm.getCurrentPosition());
            telemetry.update();

            arm.setPower(-0.005);

        }
        /*moveForward(2,0.2);
        moveTurn(1,0.2);
        strafe(1,0.2);
        moveForward(2,-0.2);
        moveTurn(1,-0.2);
        strafe(1,-0.2);*/
    }

    /** Method moveForward()
     * Will move the robot forward at the given power for the given time
     * Forward is positive speed
     * Backward is negative speed
     * @param time The time in seconds the robot is supposed to move forward
     * @param speed The power the motors are set to
     */
    public void moveForward(double time, double speed){
        FrontLeft.setPower(speed);
        FrontRight.setPower(speed);
        BackRight.setPower(speed);
        BackLeft.setPower(speed);
        sleep((int)(time*1000));
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackRight.setPower(0);
        BackLeft.setPower(0);
    }

    /** Method strafe()
     * Will move the robot left and right for the given time at the given speed
     * Positive speed goes right
     * Negative speed goes left
     * @param time The time the robot strafes
     * @param speed The speed the robot strafes at
     */
    public void strafe(double time, double speed){
        FrontLeft.setPower(speed);
        FrontRight.setPower(-speed);
        BackRight.setPower(speed);
        BackLeft.setPower(-speed);
        sleep((int)(time*1000));
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackRight.setPower(0);
        BackLeft.setPower(0);
    }

    /** Method moveTurn()
     *  Will turn the robot in the given direction for the given time
     *  Positive speed turns the robot right
     *  Negative speed turns the robot left
     * @param time The time the robot moves
     * @param speed The speed at which the robot turns
     */
    public void moveTurn(double time, double speed) {
        FrontLeft.setPower(speed);
        FrontRight.setPower(-speed);
        BackRight.setPower(-speed);
        BackLeft.setPower(speed);
        sleep((int)(time*1000));
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackRight.setPower(0);
        BackLeft.setPower(0);
    }
    //turn method, WiP
    public void stationaryTurn(double time, double speed) {
        left = Math.abs((speed-Math.abs(speed))/2);
        right = (speed+Math.abs(speed))/2;
        FrontLeft.setPower(left);
        FrontRight.setPower(right);
        BackLeft.setPower(left);
        BackRight.setPower(right);
        sleep((int)(time*1000));
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackRight.setPower(0);
        BackLeft.setPower(0);
    }
}
