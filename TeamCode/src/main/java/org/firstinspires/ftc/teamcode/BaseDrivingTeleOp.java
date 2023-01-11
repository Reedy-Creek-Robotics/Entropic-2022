package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.Controller.Button.*;
import static org.firstinspires.ftc.teamcode.Controller.Button.BACK;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

public abstract class BaseDrivingTeleOp extends BaseTeleOp {

    protected Controller driver;
    protected Controller deliverer;

    private MovementMode movementMode;
    protected double limiter;

    @Override
    public void init() {
        super.init();

        driver = controller;
        deliverer = new Controller(gamepad2);

        setMovementMode(MovementMode.FAST);
    }

    /**
     * Should be called in the child class loop method to apply basic driving functionality.
     */
    public void applyDriving() {

        DriveTrain driveTrain = robot.getDriveTrain();

        // Basic driving
        applyBasicDriving();

        // Hough assisted driving
        double driverAssistDistance = driver.isButtonDown(Controller.Button.Y) ? 0.5 : 1.0;
        if (driver.isPressed(DPAD_UP)) {
            driveTrain.moveAlignedToTileCenter(driverAssistDistance, Y, limiter);
        } else if (driver.isPressed(DPAD_LEFT)) {
            driveTrain.moveAlignedToTileCenter(-driverAssistDistance, X, limiter);
        } else if (driver.isPressed(DPAD_DOWN)) {
            driveTrain.moveAlignedToTileCenter(-driverAssistDistance, Y, limiter);
        } else if (driver.isPressed(DPAD_RIGHT)) {
            driveTrain.moveAlignedToTileCenter(driverAssistDistance, X, limiter);
        } else if (driver.isPressed(LEFT_BUMPER)) {
            driveTrain.rotateAlignedToTile(90, limiter);
        } else if (driver.isPressed(RIGHT_BUMPER)) {
            driveTrain.rotateAlignedToTile(-90, limiter);
        } else if (driver.isPressed(A)) {
            driveTrain.centerInCurrentTile(limiter);
        }

        // Utility functions
        if (driver.isPressed(LEFT_STICK_BUTTON)) {
            toggleDriveMode();
        } else if (driver.isPressed(B)) {
            driveTrain.stopAllCommands();
        } else if (driver.isPressed(START)) {
            // todo: go to the middle of the current tile instead of this
            driveTrain.setPosition(new Position(.5, .5));
        } else if (driver.isPressed(BACK)) {
            driveTrain.setHeading(new Heading(90));
        }

        telemetry.addData("Limiter", format(limiter));
    }

    public void applyBasicDriving() {
        DriveTrain driveTrain = robot.getDriveTrain();

        // Basic driving
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !driveTrain.isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            driveTrain.driverRelative(drive, turn, strafe, limiter);
        }

        if (driver.isPressed(START)) {
            // todo: go to the middle of the current tile instead of this
            driveTrain.setPosition(new Position(.5, .5));
        } else if (driver.isPressed(BACK)) {
            driveTrain.setHeading(new Heading(90));
        }
    }

    public void toggleDriveMode() {
        this.movementMode = (movementMode == MovementMode.FAST) ?
                MovementMode.SLOW : MovementMode.FAST;
        this.limiter = movementMode.power;
    }

    public enum MovementMode {
        FAST(0.9),
        SLOW(.3);

        private final double power;

        MovementMode(double power) {
            this.power = power;
        }
    }

    public void setMovementMode(MovementMode movementMode) {
        this.movementMode = movementMode;
        limiter = movementMode.power;
    }

}
