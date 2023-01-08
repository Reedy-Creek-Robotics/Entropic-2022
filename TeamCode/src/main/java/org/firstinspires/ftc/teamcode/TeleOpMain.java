package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
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
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Turret.Orientation;

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    private Controller driver;
    private Controller deliverer;

    private double limiter = 0.7;
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
        // Driving
        applyDriving();

        // Adjust the limiter
        // todo: replace this with slow mode?
        if (driver.isPressed(RIGHT_BUMPER) && limiter < 1) {
            limiter += 0.1;
        } else if (driver.isPressed(LEFT_BUMPER) && limiter > 0) {
            limiter -= 0.1;
        }

        // Intake
        if (driver.isPressed(RIGHT_TRIGGER)) {
            if (robot.getSlide().isAtOrAbove(GROUND_LEVEL)) {
                robot.getSlide().moveToIntake();
            }
            robot.getIntake().intakeManual();
        } else if (driver.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().outakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        // Lift
        if (deliverer.isPressed(LEFT_STICK_Y)) {
            int ticks = (int) Math.round(20 * deliverer.leftStickY());
            robot.getSlide().manualSlideMovement(ticks);
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
