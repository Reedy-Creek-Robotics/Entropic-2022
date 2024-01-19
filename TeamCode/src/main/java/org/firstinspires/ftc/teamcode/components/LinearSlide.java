package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearSlide extends BaseComponent {

    public static final int TICKS_PER_STACKED_CONE = 109; // ticks to lower before delivery //435/4
    public static final int DELIVER_OFFSET = 185;

    public static final int TARGET_REACHED_THRESHOLD = 5;

    public static final int MAX_HEIGHT = SlideHeight.THIRD_LEVEL.ticks + 100;
    public static final int MIN_HEIGHT = SlideHeight.TRANSFER.ticks;

    private static final int ROTATION_POINT = 1000;

    public static final double MIN_POWER = 0.01;

    public enum SlideHeight {
        THIRD_LEVEL(2000),
        SECOND_LEVEL(1291),
        FIRST_LEVEL(664),
        TRANSFER(0);

        private final int ticks;

        SlideHeight(int ticks) {
            this.ticks = ticks;
        }
    }

    public enum RotationPoints{
        OUTTAKE(0.36,1),
        INTAKE(0.15, 0);

        private final double right;

        private final double left;

        RotationPoints( double left,double right) {
            this.right = right;
            this.left = left;
        }
    }

    private double idlePower = 0.4;
    private double ascendingPower = 1.0;
    private double descendingPower = 1.0;
    private double manualPower = 0.5;
    private double manualPowerOveride = .2;

    private boolean manualControl = false;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;

    private Servo leftRotator, rightRotator;

    /**
     * The target position in motor ticks that the slide is trying to achieve.
     */
    private int targetPosition;

    public LinearSlide(RobotContext context) {
        super(context);
        leftMotor = (DcMotorEx) hardwareMap.dcMotor.get("LeftSlide");
        rightMotor = (DcMotorEx) hardwareMap.dcMotor.get("RightSlide");

        leftRotator = hardwareMap.servo.get("LeftRotator");
        //rightServo = hardwareMap.servo.get("RightRotator");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void init() {
        targetPosition = SlideHeight.TRANSFER.ticks;

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
                stopMotors();
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
                stopMotors();
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
        return (Math.abs(leftMotor.getCurrentPosition()) + Math.abs(rightMotor.getCurrentPosition()))/2;
    }

    public void stopMotors() {
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition());
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(idlePower);

        rightMotor.setTargetPosition(rightMotor.getCurrentPosition());
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setPower(idlePower);
    }

    @Override
    public void update() {
        telemetry.addData("Current Position", getPosition());

        intakeRotation(getPosition());

        super.update();
    }

    private void intakeRotation(double position){
        if(position > ROTATION_POINT ){
            rotate(RotationPoints.OUTTAKE);
        } else if (position < ROTATION_POINT) {
            rotate(RotationPoints.INTAKE);
        }

    }

    public void rotate(RotationPoints rotationPoint){
        leftRotator.setPosition(rotationPoint.left);
        rightRotator.setPosition(rotationPoint.right);
    }

    /**
     * Indicates whether the slide is currently at or above the given known position.
     */
    public boolean isAtOrAbove(SlideHeight position) {
        return getPosition() >= (position.ticks - TARGET_REACHED_THRESHOLD);
    }

    /**
     * Move the slide to the desired height
     */
    public void moveToHeight(SlideHeight position) {
        // If the height is at the small pole or above, also reset the deliver offset to move down.
        moveToTicks(position.ticks);
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
            stopMotors();
        }

        @Override
        public boolean update() {
            return Math.abs(getPosition() - ticks) <= TARGET_REACHED_THRESHOLD;
        }
    }
}
