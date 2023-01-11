package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button.*;
import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.CIRCLE;
import static org.firstinspires.ftc.teamcode.Controller.Button.CROSS;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.SQUARE;
import static org.firstinspires.ftc.teamcode.Controller.Button.TRIANGLE;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
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

    double turretPosition = Orientation.FRONT.getServoPosition();

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        deliverer.analogConfig(LEFT_STICK_Y)
                .withMaxValue(robot.getSlide().getAscendingPower());
    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

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
            robot.getSlide().manualSlideMove(deliverer.leftStickY());
        } else if (deliverer.isPressed(RIGHT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(TRAVEL);
        } else if (deliverer.isPressed(LEFT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(INTAKE);
        } else if (deliverer.isPressed(X)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        } else if (deliverer.isPressed(Y)) {
            robot.getSlide().moveToHeight(TOP_POLE);
        } else if (deliverer.isPressed(B)) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
        } else if (deliverer.isPressed(A)) {
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
