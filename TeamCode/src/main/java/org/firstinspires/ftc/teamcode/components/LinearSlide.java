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
        INTAKE(0);
        private int ticks;

        SlideHeight(int ticks) {
            this.ticks = ticks;
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
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        executeCommand(new MoveToTicks(position.ticks));
    }

    /**
     * Move the slide to the set amount of ticks
     */
    public void moveToTicks(int ticks) {
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
