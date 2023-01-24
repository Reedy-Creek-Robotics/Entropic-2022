package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.Controller.Button.Y;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseDrivingTeleOp;
import org.firstinspires.ftc.teamcode.components.LinearSlide;

@TeleOp(group = "Calibration")
public class SlideCalibration extends BaseDrivingTeleOp {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {

        applyBasicDriving();

        LinearSlide slide = robot.getSlide();

        //Intake
        if (driver.isPressed(RIGHT_TRIGGER)) {
            robot.getIntake().intakeManual();
        } else if (driver.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().outakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        // Manual slide movement
        if (deliverer.isPressed(LEFT_STICK_Y) || !robot.getSlide().isBusy()) {
            deliverer.analogConfig(LEFT_STICK_Y).withMaxValue(slide.getManualPower());
            slide.manualSlideMove(deliverer.leftStickY());
        }

        // Adjust slide power
        if (deliverer.isPressed(DPAD_UP)) {
            double power = slide.getAscendingPower();
            slide.setAscendingPower(power + 0.05);
        } else if (deliverer.isPressed(DPAD_DOWN)) {
            double power = slide.getAscendingPower();
            slide.setAscendingPower(power - 0.05);
        }
        if (deliverer.isPressed(DPAD_RIGHT)) {
            double power = slide.getDescendingPower();
            slide.setDescendingPower(power + 0.05);
        } else if (deliverer.isPressed(DPAD_LEFT)) {
            double power = slide.getDescendingPower();
            slide.setDescendingPower(power - 0.05);
        }
        if (deliverer.isPressed(RIGHT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setIdlePower(power + 0.05);
        } else if (deliverer.isPressed(LEFT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setIdlePower(power - 0.05);
        }
        if (driver.isPressed(RIGHT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setManualPower(power + 0.05);
        } else if (driver.isPressed(LEFT_BUMPER)) {
            double power = slide.getIdlePower();
            slide.setManualPower(power - 0.05);
        }

        // Lift
        if (deliverer.isPressed(LEFT_STICK_BUTTON)) {
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

        telemetry.addData("Slide Position", format(slide.getPosition()));

        telemetry.addData("Slide Ascending Power", format(slide.getAscendingPower()));
        telemetry.addData("Slide Descending Power", format(slide.getDescendingPower()));
        telemetry.addData("Slide Idle Power", format(slide.getIdlePower()));

        robot.updateStatus();
    }
}
