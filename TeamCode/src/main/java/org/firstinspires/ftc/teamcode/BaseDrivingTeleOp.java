package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;

public abstract class BaseDrivingTeleOp extends BaseTeleOp {

    protected Controller driver;

    protected double limiter = 0.7;

    @Override
    public void init() {
        super.init();

        driver = controller;
    }

    /**
     * Should be called in the child class loop method to apply basic driving functionality.
     */
    public void drive() {

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
        }

        robot.updateStatus();
    }

}
