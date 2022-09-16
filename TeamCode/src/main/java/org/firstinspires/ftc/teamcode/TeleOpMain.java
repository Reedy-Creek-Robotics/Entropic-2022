/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

@TeleOp
public class TeleOpMain extends OpMode {

    private static final double DEADZONE = 0.1;

    private Robot robot;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor arm;
    private CRServo intakeWheel;
    private Servo capper;

    private DcMotor rightDuckWheel;
    private DcMotor leftDuckWheel;

    private List<DcMotor> duckWheels;

    private ElapsedTime doorTime;
    private ElapsedTime capperTime;
    //private ElapsedTime intakeTime;
    private ElapsedTime beamBreakTime;
    private ElapsedTime beamBreakDelay;

    private boolean intaken;
    private double capperPosition;

    private DigitalChannel beamBreak;

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
        capperPosition = 0;

        rightDuckWheel = hardwareMap.dcMotor.get("duckSpinnerR");
        leftDuckWheel = hardwareMap.dcMotor.get("duckSpinnerL");

        duckWheels = new ArrayList<>();
        duckWheels.add(rightDuckWheel);
        duckWheels.add(leftDuckWheel);

        doorTime = new ElapsedTime();
        capperTime = new ElapsedTime();
        //intakeTime = new ElapsedTime();
        beamBreakTime = new ElapsedTime();
        beamBreakDelay = new ElapsedTime();

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection((DcMotorSimple.Direction.REVERSE));

        leftDuckWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDuckWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        beamBreak = hardwareMap.digitalChannel.get("beamBreak");
        robot.getLedStrip().stopLedStrip();
    }

    @Override
    public void loop() {
        // Issue commands to the robot based on the current input
        // For example, if the button is pressed to make the arm go to the highest position,
        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;

        //sets the power to the drivetrain
        frontLeft.setPower(drive - turn - strafe);
        frontRight.setPower(drive + turn + strafe);
        backRight.setPower(drive + turn - strafe);
        backLeft.setPower(drive - turn + strafe);

        //arm
        if (gamepad2.right_stick_y != 0) {
            robot.getIntake().getDoor().closeDoor();
            arm.setPower(gamepad2.right_stick_y);
        } else if (gamepad2.a) {
            robot.getIntake().getDoor().closeDoor();
            robot.getArm().moveToPosition(Arm.Position.LOW);
        } else if (gamepad2.b) {
            robot.getIntake().getDoor().closeDoor();
            robot.getArm().moveToPosition(Arm.Position.MEDIUM);
        } else if (gamepad2.y) {
            robot.getIntake().getDoor().closeDoor();
            robot.getArm().moveToPosition(Arm.Position.HIGH);
        } else if (gamepad2.x) {
            robot.getIntake().getDoor().closeDoor();
            robot.getArm().moveToPosition(Arm.Position.INTAKE);
        } else if (gamepad2.dpad_down) {
            robot.getIntake().getDoor().closeDoor();
            robot.getArm().moveToPosition(Arm.Position.TRAVEL);
        }

        //intake
        if (gamepad1.left_trigger != 0 && !intaken) {
            intakeWheel.setPower(0.5);
            beamBreakTime.reset();
        } else if (gamepad1.left_bumper) {
            intakeWheel.setPower(-0.5);
        } else {
            intakeWheel.setPower(0);
        }

        if(gamepad1.left_trigger < DEADZONE) {
            intaken = false;
        }


        */
/*if(gamepad1.left_trigger != 0 && !flag1) {
            intakeWheel.setPower(.5);
            beamBreakTime.reset();
        }else if() {

        }*//*



        //door
        if (gamepad1.right_bumper && doorTime.milliseconds() > 1500) {
            if (robot.getIntake().getDoor().getPosition().equals(Door.Position.CLOSED)) {
                if (robot.getArm().getPosition() == Arm.Position.LOW ||
                        robot.getArm().getPosition() == Arm.Position.MEDIUM ||
                        robot.getArm().getPosition() == Arm.Position.HIGH) {
                    robot.getIntake().getDoor().openDoor();
                    intaken = false;
                    telemetry.addLine("Door is open");
                }
            } else {
                robot.getIntake().getDoor().closeDoor();
                telemetry.addLine("Door is closed");
            }
            telemetry.update();
            doorTime.reset();
        }


        //capper
        if (Math.abs(gamepad2.left_stick_y) > DEADZONE && capperTime.milliseconds() > 100) {
            capperPosition += 0.05 * -gamepad2.left_stick_y;
            capperTime.reset();
            if(capperPosition > 1) {
                capperPosition =1;
            }else if(capperPosition < 0) {
                capperPosition = 0;
            }
        }else if(gamepad2.right_trigger > 0) {
            capperPosition = 0.63;
        }else if(gamepad2.left_trigger > 0) {
            capperPosition = 0.9;
        }

        capper.setPosition(capperPosition);

        telemetry.addData("Capper Position: ", capperPosition);
        telemetry.addData("current capper pos", capper.getPosition());
        telemetry.addData("Left Stick y", -gamepad2.left_stick_y);

        //duck wheels
        if (gamepad1.right_trigger > DEADZONE) {
            for (DcMotor duckWheel : duckWheels) {
                duckWheel.setPower(gamepad1.right_trigger);
            }
        } else {
            for (DcMotor duckWheel : duckWheels) {
                duckWheel.setPower(0);
            }
        }

        //beam break
        if(!intaken && beamBreakTime.milliseconds() < 1000 && beamBreakDelay.milliseconds() > 200) {
            if(!beamBreak.getState()) {
                //intakeTime.reset();
                intaken = true;
                robot.getLedStrip().activateLed("green",3);
            }
            beamBreakDelay.reset();
        }else if(!intaken) {
            robot.getLedStrip().stopLedStrip();
        }
        telemetry.addData("Have intaked",intaken);
        telemetry.update();
    }
}
*/
