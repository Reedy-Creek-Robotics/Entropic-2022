package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LinearSlide extends BaseComponent {

    private static final int TICKS_PER_STACKED_CONE = 20; // todo: calibrate

    private static final int THRESHOLD = 5;

    private static final double ASCENDING_ARM_POWER = 0.5;
    private static final double DESCENDING_ARM_POWER = 0.2;
    private static final double IDLE_ARM_POWER = 0.25;

    public enum SlideHeight {
        TOP_POLE(4125),
        MEDIUM_POLE(2950),
        SMALL_POLE(1800),
        GROUND_LEVEL(200),
        TRAVEL(500),
        INTAKE(0),
        CUSTOM(-100);

        private final int ticks;

        SlideHeight(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }

    private DcMotorEx motor;

    /**
     * The target position that the slide is trying to achieve.
     */
    private SlideHeight targetPosition;

    public LinearSlide(RobotContext context) {
        super(context);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("Slide");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        targetPosition = SlideHeight.INTAKE;
    }

    @Override
    public void init() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Returns the current position of the slide
     */
    public double getMotorPosition() {
        return motor.getCurrentPosition();
    }

    public boolean isAtOrAbove(SlideHeight position) {
        return getMotorPosition() >= (position.ticks - THRESHOLD);
    }

    public SlideHeight getTargetPosition() {
        return targetPosition;
    }

    /**
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        stopAllCommands();
        executeCommand(new MoveToTicks(position.ticks));
    }

    /**
     * Move to the intake position, with the intake aligned to the top cone in a stack with the given number of cones.
     */
    public void moveToIntake(int conesInStack) {
        int offsetTicks = (conesInStack - 1) * TICKS_PER_STACKED_CONE;
        this.targetPosition = SlideHeight.INTAKE;
        stopAllCommands();
        executeCommand(new MoveToTicks(SlideHeight.INTAKE.ticks + offsetTicks));
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
        this.targetPosition = SlideHeight.CUSTOM;
        stopAllCommands();
        executeCommand(new MoveToTicks(ticks));
    }

    private class MoveToTicks implements Command {
        private int ticks;

        public MoveToTicks(int ticks) {
            this.ticks = ticks;
        }

        @Override
        public void start() {
            motor.setTargetPosition(ticks);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double power = ticks > getMotorPosition() ?
                    ASCENDING_ARM_POWER :
                    DESCENDING_ARM_POWER;

            motor.setPower(power);
        }

        @Override
        public void stop() {
            motor.setPower(IDLE_ARM_POWER);
        }

        @Override
        public boolean updateStatus() {
            return Math.abs(motor.getCurrentPosition() - ticks) <= THRESHOLD;
        }
    }
}
