package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.*;
import static org.firstinspires.ftc.teamcode.Controller.Button.*;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Turret;

@TeleOp
public class TeleOpMain extends OpMode {

    private Robot robot;
    private Controller driver;
    private Controller deliverer;
    private double limiter;
    double intakePower;
    public static final double INTAKE_TIME = 3;


    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        driver = new Controller(gamepad1);
        deliverer = new Controller(gamepad2);
        intakePower = 0.5;


        limiter = 0.3;
    }

    @Override
    public void loop() {
        RobotDescriptor robotDescriptor = robot.getRobotContext().robotDescriptor;

        //Driving
        double drive = driver.leftStickY();
        double strafe = driver.leftStickX();
        double turn = driver.rightStickX();
        double turretPosition = FRONT.getServoPosition();

        if (nonZero(drive, turn, strafe) || !robot.getDriveTrain().isBusy()) {
            robot.getDriveTrain().drive(drive, turn, strafe, limiter);
        }

        //Hough assisted driving
        if (driver.isPressed(DPAD_UP)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
        } else if (driver.isPressed(DPAD_LEFT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
        } else if (driver.isPressed(DPAD_DOWN)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
        } else if (driver.isPressed(DPAD_RIGHT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
        } else if (driver.isPressed(RIGHT_BUMPER) && limiter<1){
            limiter+=0.05;
        }else if (driver.isPressed(RIGHT_BUMPER) && limiter>0){
            limiter-=0.05;
        }

        //Intake
        else if (driver.rightTrigger() > 0) {
            robot.getIntake().intake(intakePower, INTAKE_TIME);
        } else if (driver.isPressed(Button.Y) && intakePower < 1) {
            intakePower += 0.1;
        } else if (driver.isPressed(A) && intakePower > 0) {
            intakePower -= 0.1;
        }

        //Lift
        else if (driver.isPressed(RIGHT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(TRAVEL);
        } else if (driver.isPressed(Button.X)) {
            robot.getSlide().moveToHeight(INTAKE);
        }

        if (deliverer.isPressed(Button.Y)) {
            robot.getSlide().moveToHeight(TOP_POLE);
        } else if (deliverer.isPressed(B)) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
        } else if (deliverer.isPressed(Button.X)) {
            robot.getSlide().moveToHeight(SMALL_POLE);
        } else if (deliverer.isPressed(A)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        }

        //turret
        else if (deliverer.isPressed(DPAD_UP)) {
            turretPosition = FRONT.getServoPosition();
        }else if (deliverer.isPressed(DPAD_LEFT)) {
            turretPosition = LEFT_SIDE.getServoPosition();
        }else if (deliverer.isPressed(DPAD_DOWN)) {
            turretPosition = Turret.Orientation.BACK.getServoPosition();
        }else if (nonZero(deliverer.rightStickY()) && turretPosition>0 && turretPosition<1){
            turretPosition+=(deliverer.rightStickY()/20);
        }
        robot.getTurret().moveToPosition(turretPosition);




    }


}
