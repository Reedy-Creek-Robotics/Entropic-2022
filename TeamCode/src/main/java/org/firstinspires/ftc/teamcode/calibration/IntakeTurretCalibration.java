package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
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

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.geometry.Heading;

@TeleOp(group = "Calibration")
public class IntakeTurretCalibration extends BaseTeleOp {

    private double turretPosition = LEFT_SIDE.getServoPosition();

    private double intakeTime = 1.0;
    private double intakePower = 1.0;

    protected Controller deliverer;

    @Override
    public void start() {
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.SMALL_POLE);
    }

    @Override
    public void init() {
        super.init();

        deliverer = new Controller(gamepad2);

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();

        deliverer.analogConfig(LEFT_STICK_Y)
                .withMaxValue(robot.getSlide().getManualPower());
    }

    @Override
    public void loop() {

        if (controller.isPressed(RIGHT_STICK_Y)) {
            turretPosition += controller.rightStickY() * .001;
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

        Heading heading = robot.getDriveTrain().getHeading();
        if (deliverer.isPressed(DPAD_UP)) {
            turretPosition = Turret.getFieldRelativeOrientation(Turret.Orientation.FRONT, heading).getServoPosition();
        } else if (deliverer.isPressed(DPAD_LEFT)) {
            turretPosition = Turret.getFieldRelativeOrientation(Turret.Orientation.LEFT_SIDE, heading).getServoPosition();
        } else if(deliverer.isPressed(DPAD_RIGHT)) {
            turretPosition = Turret.getFieldRelativeOrientation(Turret.Orientation.RIGHT_SIDE, heading).getServoPosition();
        } else if (deliverer.isPressed(DPAD_DOWN)) {
            turretPosition = Turret.getFieldRelativeOrientation(Turret.Orientation.BACK, heading).getServoPosition();
        }

        telemetry.addData("Target Turret Position", format(turretPosition));
        telemetry.addData("Turret Position", format(robot.getTurret().getTargetPosition()));

        telemetry.addData("Intake Power", format(intakePower));
        telemetry.addData("Intake Time", format(intakeTime));

        robot.updateStatus();
    }
}
