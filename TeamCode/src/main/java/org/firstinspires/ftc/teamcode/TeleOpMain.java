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
import org.firstinspires.ftc.teamcode.components.Turret.Orientation;

@TeleOp
public class TeleOpMain extends OpMode {

    private Robot robot;
    private Controller driver;
    private Controller deliverer;
    private double limiter;
    double INTAKE_POWER = .3;
    public static final double INTAKE_TIME = 1;


    @Override
    public void init() {
        robot = new Robot(this, Robot.CameraMode.ENABLED_AND_STREAMING_SIDE);
        robot.init();

        driver = new Controller(gamepad1);
        deliverer = new Controller(gamepad2);
        INTAKE_POWER = 0.5;


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
        } else if (driver.isPressed(RIGHT_BUMPER) && limiter < 1) {
            limiter += 0.05;
        } else if (driver.isPressed(RIGHT_BUMPER) && limiter > 0) {
            limiter -= 0.05;
        }

        //Intake
        else if (driver.rightTrigger() > 0) {
            robot.getSlide().moveToHeight(INTAKE);
            robot.getIntake().intake(INTAKE_POWER, INTAKE_TIME);
        } else if (driver.leftTrigger() > 0) {
            robot.getIntake().outake(INTAKE_POWER, INTAKE_TIME);
        }

        //Lift
        else if (deliverer.isPressed(RIGHT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(TRAVEL);
        } else if (deliverer.isPressed(SQUARE)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        } else if (deliverer.isPressed(TRIANGLE)) {
            robot.getSlide().moveToHeight(TOP_POLE);
        } else if (deliverer.isPressed(CIRCLE)) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
        } else if (deliverer.isPressed(CROSS)) {
            robot.getSlide().moveToHeight(SMALL_POLE);
        } else if (deliverer.isPressed(A)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        }

        //Turret
        if (deliverer.isPressed(DPAD_UP)) {
            robot.getTurret().moveToOrientation(Orientation.FRONT);
        } else if (deliverer.isPressed(DPAD_LEFT) || deliverer.isPressed(DPAD_RIGHT)) {
            robot.getTurret().moveToOrientation(Orientation.LEFT_SIDE);
        } else if (deliverer.isPressed(DPAD_DOWN)) {
            robot.getTurret().moveToOrientation(Orientation.BACK);
        }


        robot.updateStatus();
    }


}
