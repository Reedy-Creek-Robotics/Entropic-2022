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

    private static final int MAX_HEIGHT = SlideHeight.TOP_POLE.ticks + 100;
    private static final int MIN_HEIGHT = SlideHeight.INTAKE.ticks;

    public enum SlideHeight {
        TOP_POLE(4125),
        MEDIUM_POLE(2950),
        SMALL_POLE(1800),
        GROUND_LEVEL(200),
        TRAVEL(500),
        INTAKE(0);

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
     * The target position in motor ticks that the slide is trying to achieve.
     */
    private int targetPosition;

    public LinearSlide(RobotContext context) {
        super(context);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("Slide");
    }

    @Override
    public void init() {
        targetPosition = SlideHeight.INTAKE.ticks;

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Moves the motor by the given number of ticks, relative to the current target position.
     */
    public void manualSlideMovement(int ticks) {
        moveToTicks(targetPosition + ticks);
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

    public int getTargetPosition() {
        return targetPosition;
    }

    /**
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        moveToTicks(position.ticks);
    }

    /**
     * Move to the intake position.
     */
    public void moveToIntake() {
        moveToIntake(1);
    }

    /**
     * Move to the intake position, with the intake aligned to the top cone in a stack with the given number of cones.
     */
    public void moveToIntake(int conesInStack) {
        int coneOffsetTicks = (conesInStack - 1) * TICKS_PER_STACKED_CONE;
        int ticks = SlideHeight.INTAKE.ticks + coneOffsetTicks;
        moveToTicks(ticks);
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
        ticks = ensureSafeTicks(ticks);
        this.targetPosition = ticks;
        stopAllCommands();
        executeCommand(new MoveToTicks(ticks));
    }

    private int ensureSafeTicks(int ticks) {
        if (ticks > MAX_HEIGHT) {
            ticks = MAX_HEIGHT;
        } else if (ticks < MIN_HEIGHT) {
            ticks = MIN_HEIGHT;
        }

        return ticks;
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
