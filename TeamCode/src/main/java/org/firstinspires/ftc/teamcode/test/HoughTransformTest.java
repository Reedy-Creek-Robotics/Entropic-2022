package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inches;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp
public class HoughTransformTest extends OpMode {

    private Robot robot;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private ElapsedTime webcamLastFrameSave = new ElapsedTime();
    private ElapsedTime waitTime = new ElapsedTime();

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();
        frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        frontRight = hardwareMap.dcMotor.get("FrontRight");
        backLeft = hardwareMap.dcMotor.get("BackLeft");
        backRight = hardwareMap.dcMotor.get("BackRight");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection((DcMotorSimple.Direction.REVERSE));
    }

    @Override
    public void loop() {

        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;

        //sets the power to the drivetrain
        robot.getDriveTrain().drive(drive, turn, strafe);

        if (gamepad1.a && waitTime.seconds() > 1) {
            waitTime.reset();

            robot.getDriveTrain().moveForward(inches(12),.5);
            robot.waitForCommandsToFinish(1);

            robot.getFrontWebCam().saveLastFrame();
            robot.waitForCommandsToFinish(1);

            robot.getFrontWebCam().saveLastFrame();
            robot.waitForCommandsToFinish(1);

            robot.getFrontWebCam().saveLastFrame();
            robot.waitForCommandsToFinish();


        }
    }
}
