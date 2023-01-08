package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.CIRCLE;
import static org.firstinspires.ftc.teamcode.Controller.Button.CROSS;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.SQUARE;
import static org.firstinspires.ftc.teamcode.Controller.Button.TRIANGLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.components.LinearSlide;

@TeleOp(group = "Calibration")
public class SlideCalibration extends BaseTeleOp {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {

        LinearSlide slide = robot.getSlide();

        // Manual slide movement
        if (controller.isPressed(LEFT_STICK_Y) || !robot.getSlide().isBusy()) {
            controller.analogConfig(LEFT_STICK_Y).withMaxValue(slide.getAscendingPower());
            slide.manualSlideMove(controller.leftStickY());
        }

        // Adjust slide power
        if (controller.isPressed(DPAD_UP)) {
            double power = slide.getAscendingPower();
            slide.setAscendingPower(power + 0.05);
        } else if (controller.isPressed(DPAD_DOWN)) {
            double power = slide.getAscendingPower();
            slide.setAscendingPower(power - 0.05);
        }
        if (controller.isPressed(DPAD_RIGHT)) {
            double power = slide.getDescendingPower();
            slide.setDescendingPower(power + 0.05);
        } else if (controller.isPressed(DPAD_LEFT)) {
            double power = slide.getDescendingPower();
            slide.setDescendingPower(power - 0.05);
        }
        if (controller.isPressed(RIGHT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setIdlePower(power + 0.05);
        } else if (controller.isPressed(LEFT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setIdlePower(power - 0.05);
        }

        // Lift
        if (controller.isPressed(LEFT_STICK_BUTTON)) {
            robot.getSlide().moveToHeight(INTAKE);
        } else if (controller.isPressed(SQUARE)) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
        } else if (controller.isPressed(TRIANGLE)) {
            robot.getSlide().moveToHeight(TOP_POLE);
        } else if (controller.isPressed(CIRCLE)) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
        } else if (controller.isPressed(CROSS)) {
            robot.getSlide().moveToHeight(SMALL_POLE);
        }

        if (controller.isPressed(B)) {
            slide.stopAllCommands();
            slide.stopMotor();
        }

        telemetry.addData("Slide Position", format(slide.getPosition()));

        telemetry.addData("Slide Ascending Power", format(slide.getAscendingPower()));
        telemetry.addData("Slide Descending Power", format(slide.getDescendingPower()));
        telemetry.addData("Slide Idle Power", format(slide.getIdlePower()));

        robot.updateStatus();
    }
}
