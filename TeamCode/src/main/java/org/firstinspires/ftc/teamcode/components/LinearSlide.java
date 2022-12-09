package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearSlide extends BaseComponent {

    public static final double ARMPOWER = 0.4;
    public static final double IDLEARMPOWER = 0.25;
    public Position position;
    public static final int THRESHOLD = 5;

    public enum Position {
        TOP_POLE(0),
        MEDIUM_POLE(0),
        SMALL_POLE(0),
        GROUND_LEVEL(0),
        TRAVEL(0),
        INTAKE(0)
        ;
        private int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }
    }

    private DcMotorEx motor;

    public LinearSlide(RobotContext context) {
        super(context);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("Slide");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        position = Position.INTAKE;
    }

    @Override
    public void init() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Move the are to the desired position
     */
    public void moveToPosition(Position position) {
        executeCommand(new MoveToPosition(position));
    }

    private class MoveToPosition implements Command {
        private Position desiredPosition;

        public MoveToPosition(Position desiredPosition) {
            this.desiredPosition = desiredPosition;
        }

        @Override
        public void start() {
            position = desiredPosition;
            motor.setTargetPosition(desiredPosition.ticks);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(ARMPOWER);
        }

        @Override
        public void stop() {
            motor.setPower(IDLEARMPOWER);
        }

        @Override
        public boolean updateStatus() {
            // todo: try to maintain the desired position, fight against gravity
            // todo: ramping
            telemetry.addData("Slide encoder ticks", motor.getCurrentPosition());
            telemetry.addData("Slide encoder target ticks", motor.getTargetPosition());
            telemetry.update();

            if(isAtDesiredPosition()) {
                stop();
                return true;
            }
            return false;
        }

        private boolean isAtDesiredPosition() {
            return Math.abs(motor.getCurrentPosition() - desiredPosition.ticks) <= THRESHOLD;
        }
    }
}
