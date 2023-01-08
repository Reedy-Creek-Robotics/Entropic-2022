package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.Button.BACK;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.LEFT_SIDE;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;

@TeleOp(group = "Calibration")
public class TurretCalibration extends BaseTeleOp {

    private double turretPosition = LEFT_SIDE.getServoPosition();

    private double intakeTime = 1.0;
    private double intakePower = 1.0;

    @Override
    public void loop() {

        if (controller.isPressed(RIGHT_STICK_Y)) {
            turretPosition += controller.rightStickY() * .01;
        }

        if (controller.isPressed(DPAD_UP)) {
            intakeTime += .05;
        } else if (controller.isPressed(DPAD_DOWN)) {
            intakeTime -= .05;
        } else if (controller.isPressed(DPAD_RIGHT)) {
            intakePower += .05;
        } else if (controller.isPressed(DPAD_LEFT)) {
            intakePower -= .05;
        }

        if (controller.isPressed(X)) {
            robot.getTurret().moveToPosition(turretPosition);
        }

        if (controller.isPressed(BACK)) {
            robot.getTurret().stopTurret();
            robot.getIntake().stopIntake();
        }

        telemetry.addData("Target Turret Position", format(turretPosition));
        telemetry.addData("Turret Position", format(robot.getTurret().getTargetPosition()));

        telemetry.addData("Intake Power", format(intakePower));
        telemetry.addData("Intake Time", format(intakeTime));

        robot.updateStatus();
    }
}
