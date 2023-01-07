package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button;
import static org.firstinspires.ftc.teamcode.Controller.Button.BACK;
import static org.firstinspires.ftc.teamcode.Controller.Button.CIRCLE;
import static org.firstinspires.ftc.teamcode.Controller.Button.CROSS;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.SQUARE;
import static org.firstinspires.ftc.teamcode.Controller.Button.TRIANGLE;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Turret.Orientation;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@TeleOp
public class TeleOpMain extends BaseTeleOp {

    private Controller driver;
    private Controller deliverer;

    private double limiter = 0.7;
    private int slideTicks;
    double turretPosition = Orientation.FRONT.getServoPosition();

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        driver = controller;
        deliverer = new Controller(gamepad2);
    }

    @Override
    public void loop() {
        // todo: manual control of the slide
        // todo: manual control of the turret

        // Driving
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !robot.getDriveTrain().isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            robot.getDriveTrain().driverRelative(drive, turn, strafe, limiter);
        }

        // Hough assisted driving
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

        // Intake
        if (driver.isPressed(RIGHT_TRIGGER)) {
            if (robot.getSlide().getTargetPosition() != INTAKE) {
                robot.getSlide().moveToIntake(1);
            }
            robot.getIntake().intakeManual();
        } else if (driver.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().outakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        // Lift
        if (deliverer.isPressed(LEFT_STICK_Y)) {
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

        // Turret
        if (deliverer.isPressed(RIGHT_STICK_X)) {
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
