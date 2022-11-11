package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.DriveTrain.Direction.*;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inches;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.opencv.core.Point;

import java.util.Arrays;
import java.util.List;

public class DriveTrain extends BaseComponent {

    // todo: edge detection (can't rely solely on runToPosition)

    /**
     * Diameter of the wheel in tiles
     */
    private static final double WHEEL_SIZE = 0.164042;

    /**
     * The number of encoder ticks that pass in a complete revolution of the motor.
     */
    private static final double TICKS_PER_REVOLUTION = 537.6;

    /**
     * A factor used to fine-tune the robot's tick distance conversion.
     */
    private static final double TICK_CORRECTION_FACTOR = 0.98;

    /**
     * The software of the drivetrain
     */
    private TileEdgeDetector tileEdgeDetectorSide;

    /**
     * The hardware for the drive train
     */
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private List<DcMotorEx> motors;
    private BNO055IMU imu;

    /**
     * The total cumulative heading for the robot.  This value can go above 360 or below 0 and will not wrap around.
     */
    private double cumulativeHeading;

    /**
     * The last orientation obtained from the IMU.
     */
    private Orientation lastOrientation;

    protected Point currentPosition;


    public DriveTrain(OpMode opMode, WebCam webCamSide) {
        super(opMode);

        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get("FrontLeft");
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get("FrontRight");
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get("BackLeft");
        backRight = (DcMotorEx) hardwareMap.dcMotor.get("BackRight");
        motors = Arrays.asList(frontLeft, frontRight, backLeft, backRight);

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        tileEdgeDetectorSide = new TileEdgeDetector(opMode, webCamSide);
        addSubComponents(tileEdgeDetectorSide);

        cumulativeHeading = 90.0;

        //todo: Decide how we are going to determine starting position
        //For now starting position is to be assumed 0,0
        currentPosition = new Point(0, 0);
    }

    @Override
    public void init() {
        super.init();

        initIMU();

        this.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        this.backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Activate the side tile edge detector immediately
        tileEdgeDetectorSide.activate();
    }

    private void initIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu.initialize(parameters);

        telemetry.addData("IMU", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
        }

        telemetry.addData("IMU", "waiting for start...");
        telemetry.addData("IMU Calibration Status", imu.getCalibrationStatus().toString());
        telemetry.update();

        // Start integration background thread, so we can get updated position in a loop.
        //imu.startAccelerationIntegration(null, null, 5);
    }

    public BNO055IMU getImu() {
        return imu;
    }

    /**
     * @return the actual heading of the robot(0 - 360)
     */
    public double getHeading() {
        double heading = cumulativeHeading % 360;
        if (heading < 0) {
            heading += 360;
        }
        return heading;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateStatus() {
        // Update the heading based off the IMU
        updateHeading();

        telemetry.addData("Heading", getHeading());
        telemetry.addData("Position", currentPosition);

        if (tileEdgeDetectorSide.isDetected()) {
            telemetry.addData("Angle to Tile", String.format("%.2f", tileEdgeDetectorSide.getAngleToTile()));
            telemetry.addData("Distance to Tile", String.format("%.2f in", tileEdgeDetectorSide.getDistanceToTileHorizontal() * 12.0));
        }

        // Now allow any commands to run with the updated data
        super.updateStatus();
    }

    /**
     * Updates the heading with the IMU
     */
    private void updateHeading() {

        // The following code adapted with permission from team SkyStone 2019-2020.

        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        if (lastOrientation == null) {
            lastOrientation = orientation;
        }

        double deltaAngle = orientation.firstAngle - lastOrientation.firstAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        cumulativeHeading += deltaAngle;

        lastOrientation = orientation;
    }

    /**
     * Sets the motor powers equal to the controllers inputs.
     *
     * @param drive
     * @param turn
     * @param strafe
     */
    public void drive(double drive, double turn, double strafe) {
        setMotorMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setPower(drive - turn - strafe);
        frontRight.setPower(drive + turn + strafe);
        backRight.setPower(drive + turn - strafe);
        backLeft.setPower(drive - turn + strafe);
    }

    public enum Direction {
        x,
        y
    }


    /**
     * Moves forward the given distance in the chosen direction at the given speed
     *
     * @param distance  Move that many tiles. Backwards and Right are negative Directions.z
     * @param direction The direction you want to move in
     * @param speed     The speed you want to move at
     */
    public void move(double distance, Direction direction, double speed) {
        if (direction == x) {
            executeCommand(new Strafe(distance, speed));
            //currentPosition.x += distance;
        } else {
            executeCommand(new MoveForward(distance, speed));
            //currentPosition.y += distance;
        }
    }

    /**
     * Moves forward the given distance.
     *
     * @param distance the distance to move in tiles
     * @param speed    a factor 0-1 that indicates how fast to move
     */
    public void moveForward(double distance, double speed) {
        executeCommand(new MoveForward(distance, speed));
    }

    /**
     * Moves forward the given distance
     *
     * @param distance the distance to move in tiles. Positive to the left, Negative to the right
     * @param speed    a factor 0-1 that indicates how fast to move
     */
    public void strafe(double distance, double speed) {
        executeCommand(new Strafe(distance, speed));
    }

    public void strafeTime(double time, double speed) {
        executeCommand(new StrafeTime(time, speed));
    }

    /**
     * Turns the given angle
     *
     * @param angle Positive is left, negative is right, turns the given angle in degrees
     * @param speed 0-1, how fast we move
     */
    public void rotate(double angle, double speed) {
        executeCommand(new Rotate(angle, speed));
    }

    /**
     * Aligns the robot to the given angle from the edge of the tile.
     *
     * @param targetAngle the desired angle from the tile edge, in degrees.
     * @param speed       the master speed at which we travel
     * @param time        the time the detector will wait in seconds
     */
    public void alignToTileAngle(double targetAngle, double speed, double time) {
        if (tileEdgeDetectorSide.waitForDetection(time)) {
            double initialAngle = tileEdgeDetectorSide.getAngleToTile();
            double angle = targetAngle - initialAngle;

            double maxRotationForAlignment = 45.0;
            if (Math.abs(angle) < maxRotationForAlignment) {
                rotate(angle, speed);
            }
        }
    }

    public void alignToTileAngle(double targetAngle, double speed) {
        alignToTileAngle(targetAngle, speed, 1);
    }

    /**
     * Strafes the robot so that the edge of the right wheel is the requested distance from the edge of the closest
     * tile to the right.
     *
     * @param targetDistance the desired distance from the edge of the tile, in tiles.
     * @param speed          the master speed at which we travel
     * @param time           the time the detector will wait in seconds
     */
    public void moveDistanceFromTileEdge(double targetDistance, double speed, double time) {
        if (tileEdgeDetectorSide.waitForDetection(time)) {
            double initialDistance = tileEdgeDetectorSide.getDistanceToTileHorizontal();
            double distance = targetDistance - initialDistance;

            // Sanity check - don't try to move more than 10 inches
            double maxDistance = inches(10);
            if (Math.abs(distance) < maxDistance) {
                strafe(distance, speed);
            }
        }
    }

    public void moveDistanceFromTileEdge(double targetDistance, double speed) {
        moveDistanceFromTileEdge(targetDistance, speed, 1);
    }

    /**
     * Turns off all the motors.
     */
    private void stopMotors() {
        // Shut off the motor power
        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
    }

    /**
     * Turns on all the motors.
     *
     * @param speed how fast the motors turn
     */
    private void setMotorPower(double speed) {
        // Shut off the motor power
        for (DcMotorEx motor : motors) {
            motor.setPower(speed);
        }
    }

    /**
     * Set the run mode for all motors.
     */
    private void setMotorMode(DcMotor.RunMode mode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(mode);
        }
    }

    /**
     * Calculates the average position for each motor.
     */
    private int averageMotorPosition() {
        int sum = 0;
        for (DcMotorEx motor : motors) {
            sum += Math.abs(motor.getCurrentPosition());
        }
        return sum / motors.size();
    }

    /**
     * Converts tiles traveled into number of ticks moved by
     *
     * @param distance how far you want to travel in tiles
     * @return number of ticks to turn
     */
    private int tilesToTicks(double distance) {
        //in tiles
        double wheelCircumference = (WHEEL_SIZE * Math.PI);

        double wheelRevolutions = distance / wheelCircumference;

        //turns wheelRevolutions into degrees, then divides by DEGREES_PER_TICK
        return (int) Math.round(wheelRevolutions * TICKS_PER_REVOLUTION);
    }

    /**
     * Calculates a smooth power curve between any two positions (in ticks, degrees, inches, etc),
     * based on the current position, the initial position, and the target position.
     *
     * @param current the current measured position
     * @param initial the initial position that was moved from
     * @param target  the target position being moved to
     * @param speed   the master speed, with range 0.0 - 1.0
     */
    private double getPowerCurveForPosition(double current, double initial, double target, double speed) {
        // Scale the position to between 0 - 1
        double xVal = scaleProgress(current, initial, target);

        double minPower = 0.15;

        double power;
        if (xVal < 0.25) {
            power = 1 / (1 + Math.pow(Math.E, -16 * (2 * xVal - 0.125)));

            // While accelerating, gradually increase the min power with time
            //minPower += time.seconds() / 4.0;

        } else {
            power = 1 / (1 + Math.pow(Math.E, 8 * (2 * xVal - 1.675)));
        }

        power *= speed;

        if (power < minPower) {
            power = minPower;
        }

        return power;
    }

    /**
     * Scales current progress from an initial value to a target value, returning as a fraction between 0.0 - 1.0.
     */
    private double scaleProgress(double current, double initial, double target) {
        if (target == initial) return 1.0;
        return (current - initial) / (target - initial);
    }

    private abstract class BaseCommand implements Command {

        @Override
        public void stop() {
            stopMotors();
        }
    }

    private class MoveForward extends BaseCommand {

        /**
         * The distance we want to move.
         */
        private double distance;

        /**
         * The speed at which to move (0 - 1).
         */
        private double speed;

        /**
         * The number of ticks we want to move.
         */
        private int ticks;

        public MoveForward(double distance, double speed) {
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public void start() {

            // Figure out the distance in ticks
            ticks = tilesToTicks(distance);

            setMotorMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            for (DcMotorEx motor : motors) {
                motor.setTargetPosition(ticks);
            }

            setMotorMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        }

        @Override
        public boolean updateStatus() {

            // Check if we've reached the correct number of ticks
            int ticksMoved = averageMotorPosition();

            double power = getPowerCurveForPosition(ticksMoved, 0, Math.abs(ticks), speed);

            telemetry.addData("ticks moved", ticksMoved);
            telemetry.addData("ticks", ticks);
            telemetry.addData("currentPower", power);

            setMotorPower(power);
            return ticksMoved >= Math.abs(ticks);
        }

    }

    private class Strafe extends BaseCommand {

        /**
         * The distance we want to move. Negative direction is to the right, Positive to the left
         */
        private double distance;

        /**
         * The speed at which to move.
         */
        private double speed;

        private int ticks;

        // todo check distance multiplied by strafe modifier
        public Strafe(double distance, double speed) {
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public void start() {
            ticks = tilesToTicks(distance);

            setMotorMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setTargetPosition(-ticks);
            frontRight.setTargetPosition(ticks);
            backLeft.setTargetPosition(ticks);
            backRight.setTargetPosition(-ticks);

            setMotorMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        @Override
        public boolean updateStatus() {
            int ticksMoved = averageMotorPosition();

            double progress = scaleProgress(Math.abs(ticksMoved), 0, Math.abs(ticks));

            telemetry.addData("tick moved", ticksMoved);
            telemetry.addData("ticks", ticks);

            setMotorPower(getPowerCurveForPosition(ticksMoved, 0, Math.abs(ticks), speed));

            return progress >= 1.0;
        }
    }

    private class StrafeTime extends BaseCommand {

        /**
         * The distance we want to move.
         */
        private double duration;

        /**
         * The speed at which to move.  Negative direction is to the right, Positive to the left
         */
        private double speed;

        // todo check distance multiplied by strafe modifier
        public StrafeTime(double duration, double speed) {
            this.duration = duration;
            this.speed = speed;
        }

        @Override
        public void start() {
            setMotorMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

            frontLeft.setPower(-speed);
            frontRight.setPower(speed);
            backLeft.setPower(speed);
            backRight.setPower(-speed);
        }

        @Override
        public boolean updateStatus() {
            return time.seconds() >= duration;
        }
    }


    private class Rotate extends BaseCommand {

        private double initialHeading;
        private double targetHeading;
        private double speed;

        public Rotate(double angle, double speed) {
            this.initialHeading = cumulativeHeading;
            this.targetHeading = cumulativeHeading + angle;
            this.speed = speed;
        }

        @Override
        public void start() {
            setMotorMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }

        @Override
        public boolean updateStatus() {
            double power = getPowerCurveForPosition(cumulativeHeading, initialHeading, targetHeading, speed);

            //if problems check this
            if (targetHeading < cumulativeHeading) {
                power = -power;
            }
            double progress = scaleProgress(cumulativeHeading, initialHeading, targetHeading);

            telemetry.addData("Heading", cumulativeHeading);
            telemetry.addData("Initial Heading", initialHeading);
            telemetry.addData("Target Heading", targetHeading);
            telemetry.addData("Motor Power Curve", power);

            frontLeft.setPower(-power);
            backLeft.setPower(-power);
            frontRight.setPower(power);
            backRight.setPower(power);

            return progress >= 1.0;
        }

    }
}
