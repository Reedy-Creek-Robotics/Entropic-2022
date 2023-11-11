package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.B;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.Controller.Button.Y;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseDrivingTeleOp;
import org.firstinspires.ftc.teamcode.RobotDescriptor.EmpiricalStrafeCorrection;

import java.util.Collections;

@TeleOp(group = "Calibration")
public class RampingCalibration extends BaseDrivingTeleOp {

    private double strafeCorrection = 1.0;

    @Override
    public void loop() {
        // Changing ramping values
        if (controller.isPressed(LEFT_TRIGGER)) {
            // Ramp Up Motor values
            if (controller.isPressed(DPAD_UP) && limiter <= 1) {
                descriptor.rampingUpMinMotorPower += 0.01;
            } else if (controller.isPressed(DPAD_DOWN) && limiter >= 0) {
                descriptor.rampingUpMinMotorPower -= 0.01;
            } else if (controller.isPressed(DPAD_LEFT)) {
                descriptor.rampingUpEndSpeed += 0.01;
            } else if (controller.isPressed(DPAD_RIGHT)) {
                descriptor.rampingUpEndSpeed -= 0.01;
            }

            // Ramp Turn Power
            if (controller.isPressed(Y)) {
                descriptor.turnRampingDescriptor.minPower -= 0.01;
            } else if (controller.isPressed(X)) {
                descriptor.turnRampingDescriptor.minPower += 0.01;
            } else if (controller.isPressed(B)) {
                descriptor.turnRampingDescriptor.maxPower -= 0.01;
            } else if (controller.isPressed(A)) {
                descriptor.turnRampingDescriptor.maxPower += 0.01;
            }

        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            // Ramp down values
            if (controller.isPressed(DPAD_UP) && limiter <= 1) {
                descriptor.rampingDownMinMotorPower += 0.01;
            } else if (controller.isPressed(DPAD_DOWN) && limiter >= 0) {
                descriptor.rampingDownMinMotorPower -= 0.01;
            } else if (controller.isPressed(DPAD_LEFT)) {
                descriptor.rampingDownBeginDistance += 0.01;
            } else if (controller.isPressed(DPAD_RIGHT)) {
                descriptor.rampingDownBeginDistance -= 0.01;
            }

            // Ramp Turn Power
            if (controller.isPressed(Y)) {
                descriptor.turnRampingDescriptor.maxPower -= 0.5;
            } else if (controller.isPressed(X)) {
                descriptor.turnRampingDescriptor.maxPower += 0.5;
            } else if (controller.isPressed(B)) {
                descriptor.rampingTurnExponent -= 0.1;
            } else if (controller.isPressed(A)) {
                descriptor.rampingTurnExponent += 0.1;
            }

        } else {
            // Limiter and strafe correction
            if (controller.isPressed(RIGHT_BUMPER) && limiter <= 1) {
                limiter += 0.05;
            } else if (controller.isPressed(LEFT_BUMPER) && limiter >= 0) {
                limiter -= 0.05;
            } else if (controller.isPressed(X)) {
                strafeCorrection += 0.005;
            } else if (controller.isPressed(Y)) {
                strafeCorrection -= 0.005;
            }

            // Apply driving
            applyDriving();
        }

        // Overwrite the empirical strafe corrections with a single value to force it to be used.
        descriptor.empiricalStrafeCorrections = Collections.singletonList(
                new EmpiricalStrafeCorrection(limiter, strafeCorrection)
        );

        telemetry.addData("Strafe Correction", format(strafeCorrection));
        telemetry.addData("Ramp Down Min Power", format(descriptor.rampingDownMinMotorPower));
        telemetry.addData("Ramp Down Begin Distance", format(descriptor.rampingDownBeginDistance) + " tiles");
        telemetry.addData("Ramp Up Min Power", format(descriptor.rampingUpMinMotorPower));
        telemetry.addData("Ramp Up End Speed", format(descriptor.rampingUpEndSpeed) + " tiles/sec");
        telemetry.addData("Ramp Min Turn Power", format(descriptor.turnRampingDescriptor.minPower));
        telemetry.addData("Ramp Max Turn Power", format(descriptor.turnRampingDescriptor.maxPower));
        telemetry.addData("Ramp Max Turn Degrees", format(descriptor.turnRampingDescriptor.distanceStartRamping));
        telemetry.addData("Ramp Turn Exponent", format(descriptor.rampingTurnExponent));

        robot.updateStatus();
    }
}
