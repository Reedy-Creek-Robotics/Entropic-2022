package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Arm extends BaseComponent {

    private static final double TICKS_PER_REVOLUTION = 537.6;
    private static final double GEAR_RATIO = 4.0;  // 80 : 20
    public static final double ARMPOWER = 0.4;
    public static final double IDLEARMPOWER = 0.25;
    public Position position;
    public static final int THRESHOLD = 5;

    public enum Position {

        INTAKE(0),
        LOW(1850),
        MEDIUM(1650),
        HIGH(1400),
        //the height the arm should be at while traveling
        TRAVEL(125),
        TEST(600);

        /**
         * The angle of the arm position, with the ground position at 0.
         */
        private int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }

    }

    private DcMotorEx motor;

    public Arm(OpMode opMode) {
        super(opMode);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("arm");
        //todo test brake behavior still works
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        position = Position.INTAKE;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void init() {
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
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
            telemetry.addData("Arm encoder ticks", motor.getCurrentPosition());
            telemetry.addData("Arm encoder target ticks", motor.getTargetPosition());
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
