package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Controller.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.RobotDescriptor.EmpiricalStrafeCorrection;

import java.util.Collections;

@TeleOp(group = "Calibration")
public class RampingCalibration extends BaseTeleOp {

    private double strafeCorrection = 1.0;
    private double limiter = 0.3;

    @Override
    public void loop() {

        // Moving the robot
        if (controller.isPressed(DPAD_UP)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
        } else if (controller.isPressed(DPAD_LEFT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
        } else if (controller.isPressed(DPAD_DOWN)) {
            robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
        } else if (controller.isPressed(DPAD_RIGHT)) {
            robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
        } else if (controller.isPressed(RIGHT_BUMPER) && limiter < 1) {
            limiter += 0.05;
        } else if (controller.isPressed(LEFT_BUMPER) && limiter > 0) {
            limiter -= 0.05;
        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            strafeCorrection += 0.01 * controller.analogValue(RIGHT_TRIGGER);
        } else if (controller.isPressed(LEFT_TRIGGER)) {
            strafeCorrection -= 0.01 * controller.analogValue(LEFT_TRIGGER);
        }

        // Overwrite the empirical strafe corrections with a single value to force it to be used.
        descriptor.empiricalStrafeCorrections = Collections.singletonList(
                new EmpiricalStrafeCorrection(limiter, strafeCorrection)
        );

        //changing ramping values
        if (controller.isPressed(LEFT_STICK_Y)) {
            robot.getRobotContext().robotDescriptor.rampingUpMinMotorPower += controller.leftStickY() * 0.01;
        }

        telemetry.addData("Limiter", limiter);
        telemetry.addData("Strafe Correction", strafeCorrection);

        robot.updateStatus();
    }
}
