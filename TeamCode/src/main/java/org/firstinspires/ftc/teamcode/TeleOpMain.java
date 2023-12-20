package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.BACK;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.START;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.Controller.Button.Y;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    private int coneCount = 4;

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        deliverer.analogConfig(LEFT_STICK_Y)
                .withMaxValue(robot.getSlide().getManualPower());
    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

        // Intake

        // Lift
        if (deliverer.isPressed(RIGHT_STICK_BUTTON)) {
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
        } else if (deliverer.isPressed(RIGHT_STICK_Y, RIGHT_STICK_X)) {
            robot.getSlide().moveToIntake(coneCount);
        } else if (deliverer.isButtonDown(START)) {
            if (deliverer.isPressed(LEFT_STICK_Y) || !robot.getSlide().isBusy()) {
                robot.getSlide().manualSlideOverride(deliverer.leftStickY());
            }
        } else if (deliverer.isPressed(LEFT_STICK_Y) || !robot.getSlide().isBusy()) {
            robot.getSlide().manualSlideMove(deliverer.leftStickY());
        }

        if (deliverer.isPressed(RIGHT_BUMPER)) {
            coneCount++;
        } else if (deliverer.isPressed(LEFT_BUMPER)) {
            coneCount--;
        }

        if (deliverer.isPressed(BACK)) {
            robot.getSlide().resetSlideTicks();
        }

        if (driver.isPressed(RIGHT_STICK_BUTTON)) {
            robot.getSlide().moveDeliverOffset();
        }

        //telemetry.addData("Turret Safe to Move", robot.getTurret().isSafeToMove() ? "yes" : "no");
        telemetry.addData("Cone Count", coneCount);

        robot.updateStatus();
    }


}
