package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_TRIGGER;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    private double slidePower = 0;

    DcMotor leftMotor, rightMotor;
    Servo leftRotator;


    @Override
    public void init() {
        super.init();

        leftMotor = hardwareMap.dcMotor.get("LeftSlide");
        rightMotor = hardwareMap.dcMotor.get("RightSlide");
        leftRotator = hardwareMap.servo.get("LeftRotator");

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void start() {
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();
        robot.getSlide().rotate(LinearSlide.RotationPoints.INTAKE);

        robot.getDroneLauncher().load();
    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

        // Intake
        if (controller.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().outtakeManual();
        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            robot.getIntake().intakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        // Slide
        if (controller.isPressed(Controller.Button.TRIANGLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.SECOND_LEVEL);
        } else if (controller.isPressed(Controller.Button.CIRCLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.THIRD_LEVEL);
        } else if (controller.isPressed(Controller.Button.CROSS)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.FIRST_LEVEL);
        }

        if (controller.isButtonDown(Controller.Button.DPAD_UP)) {
            robot.getSlide().manualSlideMove(1);
        } else if (controller.isButtonDown(Controller.Button.DPAD_DOWN)) {
            robot.getSlide().manualSlideMove(-1);
        } else if (!robot.getSlide().isBusy()){
            robot.getSlide().stopMotors();
        }


        // Outtake
        if (controller.isPressed(Controller.Button.LEFT_BUMPER)) {
            robot.getOuttake().toggleLeft();
        }
        if (controller.isPressed(Controller.Button.RIGHT_BUMPER)) {
            robot.getOuttake().toggleRight();
        }

        //Drone launcher
        if (controller.isPressed(Controller.Button.OPTIONS)) {
            robot.getDroneLauncher().launch();
        }

        //Stack Knocker
        if (controller.isPressed(Controller.Button.GUIDE)) {
            robot.getStackKnocker().toggle();
        }

        //TODO: Make trackpad disable Geofence!!!

        robot.update();
    }


}
