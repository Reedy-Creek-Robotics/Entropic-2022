package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearSlide extends BaseComponent {

    public static final double ARMPOWER = 0.4;
    public static final double IDLEARMPOWER = 0.25;
    public SlideHeight position;
    public static final int THRESHOLD = 5;

    public enum SlideHeight {
        TOP_POLE(0),
        MEDIUM_POLE(0),
        SMALL_POLE(0),
        GROUND_LEVEL(0),
        TRAVEL(0),
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

    public LinearSlide(RobotContext context) {
        super(context);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("Slide");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        position = SlideHeight.INTAKE;
    }

    @Override
    public void init() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Returns the current position of the slide
     */
    public double getMotorPosition() {
        return motor.getCurrentPosition();
    }

    /**
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        this.position = position;
        executeCommand(new MoveToTicks(position.ticks));
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
        this.position = SlideHeight.CUSTOM;
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
            motor.setPower(ARMPOWER);
        }

        @Override
        public void stop() {
            motor.setPower(IDLEARMPOWER);
        }

        @Override
        public boolean updateStatus() {
            return Math.abs(motor.getCurrentPosition() - ticks) <= THRESHOLD;
        }
    }
}
