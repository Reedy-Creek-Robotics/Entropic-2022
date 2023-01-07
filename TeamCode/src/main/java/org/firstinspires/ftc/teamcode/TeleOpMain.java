package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.*;
import static org.firstinspires.ftc.teamcode.Controller.Button.*;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.*;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.components.Turret.Orientation;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@TeleOp
public class TeleOpMain extends OpMode {

    private Robot robot;
    private Controller driver;
    private Controller deliverer;
    private double limiter;
    double INTAKE_POWER = .3;
    public static final double INTAKE_TIME = 1;
    public int slideTicks;
    double turretPosition = Orientation.FRONT.getServoPosition();

    @Override
    public void init() {
        robot = new Robot(this, Robot.CameraMode.ENABLED_AND_STREAMING_SIDE);
        robot.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        driver = new Controller(gamepad1);
        deliverer = new Controller(gamepad2);
        INTAKE_POWER = 0.5;

        limiter = 0.7;
    }

    @Override
    public void loop() {
        //@todo: manual control of the slide
        //@todo: manual control of the turret

        RobotDescriptor robotDescriptor = robot.getRobotContext().robotDescriptor;

        //Driving
        double drive = driver.leftStickY();
        double strafe = driver.leftStickX();
        double turn = driver.rightStickX();

        if (nonZero(drive, turn, strafe) || !robot.getDriveTrain().isBusy()) {
            robot.getDriveTrain().driverRelative(drive, turn, strafe, limiter);
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
            limiter += 0.1;
        } else if (driver.isPressed(LEFT_BUMPER) && limiter > 0) {
            limiter -= 0.1;
        } else if (driver.isPressed(Button.START)) {
            robot.getDriveTrain().setPosition(new Position(.5, .5));
        } else if (driver.isPressed(BACK)) {
            robot.getDriveTrain().setHeading(new Heading(90));
        }

        //Intake
        if (nonZero(driver.rightTrigger())) {
            if (robot.getSlide().getTargetPosition() != INTAKE) {
                robot.getSlide().moveToIntake(1);
            }
            robot.getIntake().intakeManual();
        } else if (nonZero(driver.leftTrigger())) {
            robot.getIntake().outakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        //Lift
        if (nonZero(deliverer.leftStickY())) {
            slideTicks += 20 * deliverer.leftStickY();
            robot.getSlide().manualSlideMovement(slideTicks);
        } else if (driver.isPressed(RIGHT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(TRAVEL);
        } else if (driver.isPressed(LEFT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(INTAKE);
        } else if (deliverer.isPressed(SQUARE)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        } else if (deliverer.isPressed(TRIANGLE)) {
            robot.getSlide().moveToHeight(TOP_POLE);
        } else if (deliverer.isPressed(CIRCLE)) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
        } else if (deliverer.isPressed(CROSS)) {
            robot.getSlide().moveToHeight(SMALL_POLE);
        }
        slideTicks = robot.getSlide().getTargetPosition().getTicks();

        //Turret
        if (nonZero(deliverer.rightStickX())) {
            turretPosition += deliverer.rightStickX() * 0.05;
        } else if (deliverer.isPressed(DPAD_UP)) {
            turretPosition = Orientation.FRONT.getServoPosition();
        } else if (deliverer.isPressed(DPAD_LEFT) || deliverer.isPressed(DPAD_RIGHT)) {
            turretPosition = Orientation.LEFT_SIDE.getServoPosition();
        } else if (deliverer.isPressed(DPAD_DOWN)) {
            turretPosition = Orientation.BACK.getServoPosition();
        }
        // todo: move manual based on deliverer right stick.
        robot.getTurret().moveToPosition(turretPosition);


        telemetry.addData("Turret Safe to Move", robot.getTurret().isSafeToMove() ? "yes" : "no");

        robot.updateStatus();
    }


}
