package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.Button.LEFT_STICK_BUTTON;

import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.game.Controller;

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
    }

    public void applyBasicDriving() {

        DriveTrain driveTrain = robot.getDriveTrain();

        // Basic driving
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !driveTrain.isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            driveTrain.drive(drive, strafe, -turn, -limiter);
        }
        //TODO: Add a way for the drive train to reset its orientation
        /*if (driver.isPressed(START)) {
            Position position = driveTrain.getPosition();
            driveTrain.setPosition(position.alignToTileMiddle());
        } else if (driver.isPressed(BACK)) {
            driveTrain.setHeading(new Heading(90));
        }*/

        // Slow mode
        if (driver.isPressed(LEFT_STICK_BUTTON)) {
            toggleDriveMode();
        }
    }

    public void toggleDriveMode() {
        this.movementMode = (movementMode == MovementMode.FAST) ?
                MovementMode.SLOW : MovementMode.FAST;
        this.limiter = movementMode.power;
    }

    public enum MovementMode {
        FAST(1),
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
