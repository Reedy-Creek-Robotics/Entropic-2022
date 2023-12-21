package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LinearSlide extends BaseComponent {

    public static final int TICKS_PER_STACKED_CONE = 109; // ticks to lower before delivery //435/4
    public static final int DELIVER_OFFSET = 185;

    public static final int TARGET_REACHED_THRESHOLD = 5;

    public static final int MAX_HEIGHT = SlideHeight.TOP_POLE.ticks + 100;
    public static final int MIN_HEIGHT = SlideHeight.INTAKE.ticks;
    public static final double MIN_POWER = 0.01;
    public static final int TURRET_SAFETY_LEEWAY = 100;
    public static final int DELIVER_OFFSET_LEEWAY = 300;

    public enum SlideHeight {
        TOP_POLE(2870),
        MEDIUM_POLE(2050),
        SMALL_POLE(1175),
        GROUND_LEVEL(95),
        TRAVEL(350),
        INTAKE(0);

        private final int ticks;

        SlideHeight(int ticks) {
            this.ticks = ticks;
        }
    }

    private double idlePower = 0.4;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;
    private double manualPower = 0.5;
    private double manualPowerOveride = .2;

    private boolean moveDeliverOffsetDown = false;
    private boolean manualControl = false;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;

    /**
     * The target position in motor ticks that the slide is trying to achieve.
     */
    private int targetPosition;

    public LinearSlide(RobotContext context) {
        super(context);
        leftMotor = (DcMotorEx) hardwareMap.dcMotor.get("LeftSlide");
        rightMotor = (DcMotorEx) hardwareMap.dcMotor.get("RightSlide");
    }

    @Override
    public void init() {
        targetPosition = SlideHeight.INTAKE.ticks;

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }



    /**
     * Resets the slide ticks to 0
     */
    public void resetSlideTicks() {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Moves the slide with the given power, in the range (-1, 1).
     */
    public void manualSlideMove(double power) {
        stopAllCommands();

        if (Math.abs(power) > manualPower) {
            power = manualPower * Math.signum(power);
        }

        if (Math.abs(power) < MIN_POWER ||
                (power < 0.0 && getPosition() <= MIN_HEIGHT) ||
                (power > 0.0 && getPosition() >= MAX_HEIGHT)
        ) {
            if (manualControl) {
                manualControl = false;
                stopMotor();
            }
        } else {
            manualControl = true;
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftMotor.setPower(power);

            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setPower(power);
        }
    }

    /**
     * Moves the slide with the given power, works regardless of minimum and maximum height
     */
    public void manualSlideOverride(double power) {
        stopAllCommands();

        if (Math.abs(power) > manualPowerOveride) {
            power = manualPowerOveride * Math.signum(power);
        }

        if(Math.abs(power) < MIN_POWER) {
            if (manualControl) {
                manualControl = false;
                stopMotor();
            }
        } else {
            manualControl = true;
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftMotor.setPower(power);

            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setPower(power);
        }
    }

    //todo: move it lower
    public void moveDeliverOffset() {
        if (getPosition() >= (SMALL_POLE.ticks - DELIVER_OFFSET_LEEWAY)) {
            if (moveDeliverOffsetDown) {
                moveToTicks(targetPosition -= DELIVER_OFFSET);
            } else {
                moveToTicks(targetPosition += DELIVER_OFFSET);
            }
            moveDeliverOffsetDown = !moveDeliverOffsetDown;
        }
    }

    /**
     * Adjusts the position of the slide by the given number of ticks.  Positive ticks moves the
     * slide up, negative ticks moves the slide down.
     */
    public void manualSlideAdjustPosition(int ticks) {
        moveToTicks(targetPosition + ticks);
    }

    /**
     * Returns the current position of the slide
     */
    public double getPosition() {
        return (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition())/2;
    }

    public void stopMotor() {
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition());
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(idlePower);

        rightMotor.setTargetPosition(leftMotor.getCurrentPosition());
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setPower(idlePower);
    }

    @Override
    public void updateStatus() {
        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Leeway position", TRAVEL.ticks - TURRET_SAFETY_LEEWAY);

        super.updateStatus();
    }

    /**
     * Indicates whether the slide is currently at or above the given known position.
     */
    public boolean isAtOrAbove(SlideHeight position) {
        return getPosition() >= (position.ticks - TURRET_SAFETY_LEEWAY - TARGET_REACHED_THRESHOLD);
    }

    /**
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        // If the height is at the small pole or above, also reset the deliver offset to move down.
        moveDeliverOffsetDown = position.ticks > (SMALL_POLE.ticks - DELIVER_OFFSET_LEEWAY);

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
        this.manualControl = false;
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

    public double getIdlePower() {
        return idlePower;
    }

    public void setIdlePower(double idlePower) {
        this.idlePower = idlePower;
    }

    public double getAscendingPower() {
        return ascendingPower;
    }

    public void setAscendingPower(double ascendingPower) {
        this.ascendingPower = ascendingPower;
    }

    public double getDescendingPower() {
        return descendingPower;
    }

    public void setDescendingPower(double descendingPower) {
        this.descendingPower = descendingPower;
    }

    public double getManualPower() {
        return manualPower;
    }

    public void setManualPower(double manualPower) {
        this.manualPower = manualPower;
    }

    private class MoveToTicks implements Command {
        private int ticks;

        public MoveToTicks(int ticks) {
            this.ticks = ticks;
        }

        @Override
        public void start() {
            leftMotor.setTargetPosition(ticks);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            rightMotor.setTargetPosition(ticks);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double power = ticks > getPosition() ?
                    ascendingPower :
                    descendingPower;

            leftMotor.setPower(power);
            rightMotor.setPower(power);
        }

        @Override
        public void stop() {
            stopMotor();
        }

        @Override
        public boolean updateStatus() {
            return Math.abs(getPosition() - ticks) <= TARGET_REACHED_THRESHOLD;
        }
    }
}
