package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

@Disabled
@TeleOp
public class HardwareCheck extends OpMode {
    private Robot robot;
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor arm;
    public CRServo intakeWheel;
    public Servo capper;

    public DcMotor rightDuckWheel;
    public DcMotor leftDuckWheel;

    public List<DcMotor> duckWheels;

    public ElapsedTime intakeTime;
    public ElapsedTime wheelCheckTime;

    public boolean testing;

    public static final double YELLOW_JACKET = 537.6;

    @Override
    public void init() {
        robot = new Robot(this, false);
        robot.init();

        frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        frontRight = hardwareMap.dcMotor.get("FrontRight");
        backLeft = hardwareMap.dcMotor.get("BackLeft");
        backRight = hardwareMap.dcMotor.get("BackRight");

        arm = hardwareMap.dcMotor.get("arm");
        intakeWheel = hardwareMap.crservo.get("intakeWheel");
        capper = hardwareMap.servo.get("capServo");

        rightDuckWheel = hardwareMap.dcMotor.get("duckSpinnerR");
        leftDuckWheel = hardwareMap.dcMotor.get("duckSpinnerL");

        duckWheels = new ArrayList<>();
        duckWheels.add(rightDuckWheel);
        duckWheels.add(leftDuckWheel);

        intakeTime = new ElapsedTime();
        wheelCheckTime = new ElapsedTime();

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection((DcMotorSimple.Direction.REVERSE));

        leftDuckWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDuckWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        testing = true;
    }

    @Override
    public void loop() {
        //Will spin each wheel for 3 seconds
        if (gamepad1.a && wheelCheckTime.seconds() < 12 && testing) {
            robot.getDriveTrain().runEachMotor(.5, 3);
        }

        //Will open and close door as needed
        if (gamepad1.b && intakeTime.milliseconds() > 1500 && testing) {
            if (robot.getIntake().getDoor().getPosition().equals(Door.Position.CLOSED)) {
                robot.getIntake().getDoor().openDoor();
                telemetry.addLine("Door is open");
            } else {
                robot.getIntake().getDoor().closeDoor();
                telemetry.addLine("Door is closed");
            }
            telemetry.update();
            intakeTime.reset();
        }

        double drive = gamepad1.left_stick_y;
        //double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;

        //frontLeft.setPower(drive - turn - strafe);
        //frontRight.setPower(drive + turn + strafe);
        //backRight.setPower(drive + turn - strafe);
        //backLeft.setPower(drive - turn + strafe);
        telemetry.addData("Gamepad X val",gamepad1.left_stick_x);
        telemetry.addData("Gamepad Y val", gamepad1.left_stick_y);
        telemetry.addData("frontLeft",drive-strafe);
        telemetry.addData("frontRight", drive + strafe);
        telemetry.addData("backLeft", drive + strafe);
        telemetry.addData("backRight", drive - strafe);
        telemetry.update();
    }
}
