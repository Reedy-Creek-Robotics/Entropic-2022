package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.tilesToInches;
import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;
import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.FieldSpaceCoordinates;
import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.RobotSpaceCoordinates;
import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.convertToFieldSpace;
import static org.firstinspires.ftc.teamcode.util.RobotFieldConversionUtil.convertToRobotSpace;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.TileEdgeSolver;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;

import java.util.Arrays;
import java.util.List;

@SuppressLint("DefaultLocale")
public class DriveTrain extends BaseComponent {

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
     * The current position for the robot.
     */
    private Position position;

    /**
     * The current heading for the robot.
     */
    private Heading heading;

    /**
     * The current velocity vector in tiles / sec.
     */
    private Vector2 velocity;

    /**
     * The previous iteration's position.
     */
    private Position previousPosition;

    /**
     * The previous iteration's orientation data obtained from the IMU.
     */
    private Orientation previousImuOrientation;

    /**
     * The previous iteration's tick counts for the motors.
     */
    private MotorTicks previousMotorTicks;

    /**
     * The time of the previous update iteration.
     */
    private ElapsedTime previousUpdateTime;

    /**
     * The previously seen tile edge observation.  We don't want to apply the same observation twice.
     */
    private TileEdgeSolver.TileEdgeObservation previousObservation;


    public DriveTrain(RobotContext context, WebCam webCamSide) {
        super(context);

        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get("FrontLeft");
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get("FrontRight");
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get("BackLeft");
        backRight = (DcMotorEx) hardwareMap.dcMotor.get("BackRight");
        motors = Arrays.asList(frontLeft, frontRight, backLeft, backRight);

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        tileEdgeDetectorSide = new TileEdgeDetector(context, webCamSide);
        addSubComponents(tileEdgeDetectorSide);

        // For now starting position is to be assumed the origin (0, 0)
        position = new Position(.5, .5);
        heading = new Heading(90);
        velocity = new Vector2(0, 0);
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

        previousUpdateTime = new ElapsedTime();
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

    public TileEdgeDetector getTileEdgeDetectorSide() {
        return tileEdgeDetectorSide;
    }

    public BNO055IMU getImu() {
        return imu;
    }

    /**
     * Return the heading of the robot as an angle in degrees from (0 - 360).
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Return the current position of the robot.
     */
    public Position getPosition() {
        return position;
    }

    @Override
    public void updateStatus() {
        // Update the current position and heading based off of sensory data
        updateCurrentPosition();
        updateCurrentHeading();
        updateCurrentVelocity();

        previousUpdateTime.reset();

        telemetry.addData("Heading", heading);
        telemetry.addData("Position", position);
        telemetry.addData("Speed", format(velocity.magnitude()));

        //telemetry.addData("Current Command", getCurrentCommand());
        //telemetry.addData("Next Commands", getNextCommands());

        // Now allow any commands to run with the updated data
        super.updateStatus();
    }

    /**
     * Updates the current position of the bot.
     */
    private void updateCurrentPosition() {
        // Use the motor encoders to approximate the change in position.
        updateCurrentPositionWithMotorTicks();

        // Override this with a visual observation from hough code, if there is one.
        updateCurrentPositionWithTileEdgeObservation();
    }

    private void updateCurrentPositionWithMotorTicks() {
        // Determine the number of ticks moved by each wheel.
        MotorTicks ticks = getCurrentMotorTicks();

        //telemetry.addData("Motor Ticks", ticks.toString());

        if (previousMotorTicks != null) {

            // If we have a previous tick count, calculate how far the robot has moved based on the delta in ticks,
            // and add that to the current position.

            int deltaBackLeft = ticks.backLeft - previousMotorTicks.backLeft;
            int deltaBackRight = ticks.backRight - previousMotorTicks.backRight;
            int deltaFrontLeft = ticks.frontLeft - previousMotorTicks.frontLeft;
            int deltaFrontRight = ticks.frontRight - previousMotorTicks.frontRight;

            Vector2 deltaPositionRelativeToField = MecanumUtil.calculatePositionOffsetFromWheelRotations(
                    robotDescriptor,
                    deltaBackLeft,
                    deltaBackRight,
                    deltaFrontLeft,
                    deltaFrontRight,
                    heading,
                    null
            );

            position = position.add(deltaPositionRelativeToField);
        }

        // Remember the current motor ticks for the next loop iteration
        previousMotorTicks = ticks;
    }

    private void updateCurrentPositionWithTileEdgeObservation() {
        // Check whether there is an observation, and if we have not yet seen it.
        TileEdgeSolver.TileEdgeObservation observation = tileEdgeDetectorSide.getObservation();
        if (observation != null && previousObservation != observation) {
            previousObservation = observation;

            // First, compute the expected robot space coordinates using our theoretical position.
            RobotSpaceCoordinates robotSpaceCoordinates = convertToRobotSpace(new FieldSpaceCoordinates(heading, position));

            // Then using the observation overwrite the expected with the actual values.
            if (observation.distanceRight != null) {
                double distanceRightInches = tilesToInches(observation.distanceRight) - robotDescriptor.robotDimensionsInInches.width / 2;
                telemetry.addData("Distance Right", String.format("%.2f in", distanceRightInches));
                robotSpaceCoordinates.distanceRight = observation.distanceRight;
            }
            if (observation.distanceFront != null) {
                double distanceFrontInches = tilesToInches(observation.distanceFront) - robotDescriptor.robotDimensionsInInches.height / 2;
                telemetry.addData("Distance Front", String.format("%.2f in", distanceFrontInches));
                robotSpaceCoordinates.distanceFront = observation.distanceFront;
            }
            if (observation.headingOffset != null) {
                telemetry.addData("Heading Offset", observation.headingOffset);
                robotSpaceCoordinates.headingOffset = observation.headingOffset;
            }

            // Convert back to field space.
            FieldSpaceCoordinates updatedFieldSpaceCoordinates = convertToFieldSpace(robotSpaceCoordinates);

            // Apply the current velocity as well, taking into account how old this observation is.
            double elapsed = observation.observationTime.seconds();
            Vector2 velocityCorrection = velocity != null ?
                    velocity.multiply(elapsed) :
                    new Vector2(0, 0);

            Position updatedPosition = updatedFieldSpaceCoordinates.position.add(velocityCorrection);

            // Sanity check - make sure that the distance we would be correcting is less than a maximum.
            double maxHoughCorrectionDistance = inchesToTiles(12);

            Vector2 correction = updatedPosition.minus(position);
            double correctionDistance = correction.magnitude();

            if (correctionDistance < maxHoughCorrectionDistance) {
                // Finally update the current position to be the actual position.
                //heading = updatedFieldSpaceCoordinates.heading;
                position = updatedPosition;

                // Also update the previous position by the same amount, so the velocity doesn't jump.
                if (previousPosition != null) {
                    previousPosition = previousPosition.add(correction);
                }
            }
        }
    }

    public void setPosition(Position position) {
        this.position = position;

        // Also null out the previous position so that the velocity doesn't jump unnaturally.
        previousPosition = null;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    private MotorTicks getCurrentMotorTicks() {
        return new MotorTicks(
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition(),
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition()
        );
    }

    /**
     * Updates the current heading of the bot.
     */
    private void updateCurrentHeading() {

        // The following code adapted with permission from team SkyStone 2019-2020.

        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        if (previousImuOrientation == null) {
            previousImuOrientation = orientation;
        }

        double deltaAngle = orientation.firstAngle - previousImuOrientation.firstAngle;
        heading = heading.add(deltaAngle);

        previousImuOrientation = orientation;
    }

    /**
     * Updates the current velocity of the bot.
     */
    private void updateCurrentVelocity() {

        if (previousPosition != null) {

            Vector2 delta = position.minus(previousPosition);
            double elapsed = previousUpdateTime.seconds();

            velocity = delta.multiply(1 / elapsed);
        }

        previousPosition = position;
    }

    /**
     * Waits for a tile edge detection up to the given number of seconds.
     *
     * Note that this method will block, and should only be called when the robot is not doing
     * anything else.  Otherwise, the effects are unpredictable.
     */
    public void waitForTileEdgeDetection(double maxTime) {
        boolean active = tileEdgeDetectorSide.isActive();
        if (!active) {
            // Activate the tile edge detector if it's not turned on.
            tileEdgeDetectorSide.activate();
        }

        // Wait for up to the requested time until we have a valid observation.
        ElapsedTime waitTime = new ElapsedTime();
        while (!isStopRequested() && waitTime.seconds() < maxTime) {
            TileEdgeSolver.TileEdgeObservation observation = tileEdgeDetectorSide.getObservation();
            if (observation != null && observation.distanceRight != null) {
                sleep(5);
            }
        }

        if (!active) {
            // If we turned on edge detection just for this method, now disable it.
            tileEdgeDetectorSide.deactivate();
        }
    }

    /**
     * Sets the motor powers equal to the controllers inputs.
     */
    public void drive(double drive, double turn, double strafe, double speed) {

        // Stop any current command from executing.
        stopAllCommands();

        MecanumUtil.MotorPowers motorPowers = MecanumUtil.calculateWheelPowerForDrive(
                drive,
                strafe,
                turn,
                speed
        );

        setMotorMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        setMotorPowers(motorPowers);
    }

    public void driverRelative(double drive, double turn, double strafe, double speed) {

        // Stop any current command from executing.
        stopAllCommands();

        MecanumUtil.MotorPowers motorPowers = MecanumUtil.calculateWheelPowerForDriverRelative(
                drive,
                strafe,
                turn,
                heading,
                speed
        );

        setMotorMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        setMotorPowers(motorPowers);
    }

    public enum Direction {
        X,
        Y
    }

    /**
     * Moves the given distance in tiles, in the given direction, at the given speed.
     *
     * @param distance      Move that many tiles in the x and y direction. Backwards and Right are negative Directions.
     * @param targetHeading The end heading the robot should be oriented at.
     * @param speed         The speed you want to move at
     */
    public void moveTargetDistance(Vector2 distance, Heading targetHeading, double speed) {
        executeCommand(new MoveTargetDistance(distance, targetHeading, speed));
    }

    /**
     * Moves the given distance in tiles, in the given direction, at the given speed.
     *
     * @param distance Move that many tiles in the x and y direction. Backwards and Right are negative Directions.
     * @param speed    The speed you want to move at
     */
    public void moveTargetDistance(Vector2 distance, double speed) {
        executeCommand(new MoveTargetDistance(distance, heading, speed));
    }

    /**
     * Moves to the given target position and heading
     *
     * @param targetPosition The target position at which the robot should end.
     * @param targetHeading  The end heading the robot should be oriented at.
     * @param speed          The speed you want to move at.
     */
    public void moveToTargetPosition(Position targetPosition, Heading targetHeading, double speed) {
        executeCommand(new MoveToTargetPosition(targetPosition, targetHeading, speed));
    }

    public void moveToHeading(Heading targetHeading, double speed) {
        executeCommand(new MoveToTargetPosition(position, targetHeading, speed));
    }

    /**
     * Moves to the given target position and heading
     *
     * @param targetPosition The target position at which the robot should end.
     * @param speed          The speed you want to move at.
     */
    public void moveToTargetPosition(Position targetPosition, double speed) {
        executeCommand(new MoveToTargetPosition(targetPosition, heading, speed));
    }

    /**
     * Moves the given distance in tiles, in the given direction, at the given speed.
     *
     * @param distance  Move that many tiles. Backwards and Right are negative Directions.
     * @param direction The direction you want to move in
     * @param speed     The speed you want to move at
     */
    public void moveAlignedToTileCenter(double distance, Direction direction, double speed) {
        executeCommand(new MoveAlignedToTileCenter(direction, distance, speed));
    }

    /**
     * Rotates the given number of degrees, attempting to end aligned to the nearest tile edge.
     *
     * @param rotation the number of degrees to rotate
     * @param speed    The speed you want to move at
     */
    public void rotateAlignedToTile(double rotation, double speed) {
        executeCommand(new RotateAlignedToTile(rotation, speed));
    }

    /**
     * Centers the robot within the current tile.
     */
    public void centerInCurrentTile(double speed) {
        moveAlignedToTileCenter(0.0, Direction.X, speed);
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
    /*
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
    }*/

    /**
     * Strafes the robot so that the edge of the right wheel is the requested distance from the edge of the closest
     * tile to the right.
     *
     * @param targetDistance the desired distance from the edge of the tile, in tiles.
     * @param speed          the master speed at which we travel
     * @param time           the time the detector will wait in seconds
     */
    /*
    public void moveDistanceFromTileEdge(double targetDistance, double speed, double time) {
        if (tileEdgeDetectorSide.waitForDetection(time)) {
            double initialDistance = tileEdgeDetectorSide.getDistanceToTileHorizontal();
            double distance = targetDistance - initialDistance;

            // Sanity check - don't try to move more than 10 inches
            double maxDistance = inchesToTiles(10);
            if (Math.abs(distance) < maxDistance) {
                strafe(distance, speed);
            }
        }
    }

    public void moveDistanceFromTileEdge(double targetDistance, double speed) {
        moveDistanceFromTileEdge(targetDistance, speed, 1);
    }*/

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

    private void setMotorPowers(MecanumUtil.MotorPowers motorPowers) {
        frontLeft.setPower(motorPowers.frontLeft);
        frontRight.setPower(motorPowers.frontRight);
        backLeft.setPower(motorPowers.backLeft);
        backRight.setPower(motorPowers.backRight);
    }

    /**
     * Set the run mode for all motors.
     */
    private void setMotorMode(DcMotor.RunMode mode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(mode);
        }

        // Also, if the encoders are being reset, forget the previous motor ticks, in order to keep the robot from
        // thinking it has jumped through space and time when updating the position in the next loop.
        if (mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER || mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
            previousMotorTicks = null;
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

        public String toString() {
            return getClass().getSimpleName();
        }

    }

    private abstract class BaseMoveCommand extends BaseCommand {

        /**
         * The speed at which to move (0 - 1).
         */
        private double speed;

        /**
         * The position that the robot is trying to achieve.
         */
        private Position targetPosition;

        /**
         * The heading that the robot is trying to achieve.
         */
        private Heading targetHeading;

        /**
         * The starting position of the robot.
         */
        private Position startingPosition;

        /**
         * The starting heading.
         */
        private Heading startingHeading;

        public BaseMoveCommand(double speed) {
            this.speed = speed;
        }

        /**
         * Called on command start, the child class should calculate and return the target position.
         */
        protected abstract Position calculateTargetPosition();

        /**
         * Called on command start, the child class should calculate and return the target heading.
         */
        protected abstract Heading calculateTargetHeading();

        public double getSpeed() {
            return speed;
        }

        public Position getTargetPosition() {
            return targetPosition;
        }

        public Heading getTargetHeading() {
            return targetHeading;
        }

        @Override
        public void start() {
            targetPosition = calculateTargetPosition();
            targetHeading = calculateTargetHeading();

            startingPosition = position;
            startingHeading = heading;

            setMotorMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        @Override
        public boolean updateStatus() {
            MecanumUtil.MotorPowers motorPowers = MecanumUtil.calculateWheelPowerForTargetPosition(
                    robotDescriptor,
                    position, heading, velocity,
                    targetPosition, targetHeading,
                    speed
            );

            //telemetry.addData("startingPosition", startingPosition);
            //telemetry.addData("targetPosition", targetPosition);
            //telemetry.addData("targetHeading", targetHeading);
            //telemetry.addData("motorPowers", motorPowers);

            setMotorPowers(motorPowers);

            double distanceMoved = position.distance(startingPosition);
            double distanceRemaining = position.distance(targetPosition);
            double headingMoved = Math.abs(heading.delta(startingHeading));
            double headingRemaining = Math.abs(heading.delta(targetHeading));

            // Finish the command when the target position is reached and we are within a threshold of the target heading.
            boolean targetPositionReached =
                    distanceRemaining <= robotDescriptor.movementTargetPositionReachedThreshold ||
                            distanceMoved >= startingPosition.distance(targetPosition);

            boolean targetHeadingReached =
                    headingMoved >= Math.abs(startingHeading.delta(targetHeading)) ||
                            headingRemaining <= robotDescriptor.rotationTargetHeadingReachedThreshold;

            return targetPositionReached && targetHeadingReached;
        }
    }

    /**
     * Moves the robot by the given offset and to the given heading.
     */
    private class MoveTargetDistance extends BaseMoveCommand {

        private Vector2 offsetToMove;

        private Heading targetHeading;

        public MoveTargetDistance(Vector2 offsetToMove, Heading targetHeading, double speed) {
            super(speed);
            this.offsetToMove = offsetToMove;
            this.targetHeading = targetHeading;
        }

        @Override
        protected Position calculateTargetPosition() {
            return position.add(offsetToMove);
        }

        @Override
        protected Heading calculateTargetHeading() {
            return targetHeading;
        }

    }

    /**
     * Moves the robot to the given target position and heading.
     */
    private class MoveToTargetPosition extends BaseMoveCommand {

        private Position targetPosition;

        private Heading targetHeading;

        public MoveToTargetPosition(Position targetPosition, Heading targetHeading, double speed) {
            super(speed);
            this.targetPosition = targetPosition;
            this.targetHeading = targetHeading;
        }

        @Override
        protected Position calculateTargetPosition() {
            return targetPosition;
        }

        @Override
        protected Heading calculateTargetHeading() {
            return targetHeading;
        }
    }

    /**
     * This command will move in a path along the center line of a tile row or column.
     * <p>
     * If the robot is not aligned to the tile's center, this will also attempt to correct that by moving to tile
     * center along the path of movement.
     * <p>
     * If the robot's heading is not aligned to a 90 degree tile boundary, this will also attempt to correct that
     * by making the smallest rotation possible.
     */
    private class MoveAlignedToTileCenter extends BaseMoveCommand implements CombinableCommand {

        /**
         * The direction in which the robot should move.
         */
        private Direction direction;

        /**
         * The distance, in tiles, that the robot should move.
         */
        private double distance;

        public MoveAlignedToTileCenter(Direction direction, double distance, double speed) {
            super(speed);
            this.direction = direction;
            this.distance = distance;
        }

        @Override
        protected Position calculateTargetPosition() {
            // Calculate the new target position, aligned to the tile middle.
            Position targetPosition;
            if (direction == X) {
                targetPosition = new Position(position.getX() + distance, position.getY());
            } else if (direction == Y) {
                targetPosition = new Position(position.getX(), position.getY() + distance);
            } else {
                throw new IllegalArgumentException();
            }
            return targetPosition.alignToTileMiddle();
        }

        @Override
        protected Heading calculateTargetHeading() {
            return heading.alignToRightAngle();
        }

        @Override
        public Command combineWith(Command other) {
            if (other instanceof MoveAlignedToTileCenter) {

                // If the other move command is the same direction as this one, it can be combined with this one.
                MoveAlignedToTileCenter otherMoveCommand = (MoveAlignedToTileCenter) other;
                if (direction == otherMoveCommand.direction) {

                    // If this command has already been started, and a targetPosition calculated, calculate the
                    // remaining distance to the target.  Otherwise, the command has not yet been started but is in
                    // the queue, so just use the full requested distance to move.
                    double remainingDistance = getTargetPosition() != null ?
                            position.distance(getTargetPosition()) * Math.signum(distance) :
                            distance;

                    return new MoveAlignedToTileCenter(
                            direction,
                            remainingDistance + otherMoveCommand.distance,
                            getSpeed()
                    );
                }
            }

            return null;
        }

        public String toString() {
            return String.format("Move Tile (%s %.1f)", direction, distance);
        }
    }

    private class RotateAlignedToTile extends BaseMoveCommand implements CombinableCommand {

        /**
         * The number of degrees to rotate.
         */
        double rotation;

        public RotateAlignedToTile(double rotation, double speed) {
            super(speed);
            this.rotation = rotation;
        }

        @Override
        protected Position calculateTargetPosition() {
            return position;
        }

        @Override
        protected Heading calculateTargetHeading() {
            return heading.add(rotation).alignToRightAngle();
        }

        @Override
        public Command combineWith(Command other) {
            if (other instanceof RotateAlignedToTile) {

                // If the other move command is a rotate aligned to tile, we can combine it with this one.
                RotateAlignedToTile otherRotateCommand = (RotateAlignedToTile) other;

                // If the other command has already been started, and a targetHeading calculated, calculate the
                // remaining rotation.  Otherwise, the command has not yet been started but is in the queue, so
                // just use the full requested rotation to move.
                double remainingRotation = getTargetHeading() != null ?
                        getTargetHeading().delta(heading) :
                        rotation;

                return new RotateAlignedToTile(
                        remainingRotation + otherRotateCommand.rotation,
                        getSpeed()
                );
            }

            return null;
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

        ////////////////////////////////////////////////
        //Test Code
        private double progress;
        ////////////////////////////////////////////////

        public MoveForward(double distance, double speed) {
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public void start() {

            ////////////////////////////////////////////////
            //Test Code
            progress = 0;
            ////////////////////////////////////////////////

            // Figure out the distance in ticks
            ticks = MecanumUtil.tilesToTicks(robotDescriptor, distance);

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

            telemetry.addData("ticks moved", ticksMoved);
            telemetry.addData("ticks", ticks);

            setMotorPower(getPowerCurveForPosition(ticksMoved, 0, Math.abs(ticks), speed));
            return progress >= 1.0;
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

        ////////////////////////////////////////////////
        //Test Code
        private double progress;
        ////////////////////////////////////////////////

        public Strafe(double distance, double speed) {
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public void start() {
            ////////////////////////////////////////////////
            //Test Code
            progress = 0;
            ////////////////////////////////////////////////

            ticks = MecanumUtil.tilesToTicks(robotDescriptor, distance);

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

            telemetry.addData("tick moved", ticksMoved);
            telemetry.addData("ticks", ticks);

            setMotorPower(getPowerCurveForPosition(ticksMoved, 0, Math.abs(ticks), speed));

            return progress >= 1.0;
        }
    }

    private class Rotate extends BaseCommand {

        private Heading initialHeading;
        private Heading targetHeading;
        private double speed;

        public Rotate(double angle, double speed) {
            this.initialHeading = heading;
            this.targetHeading = heading.add(angle);
            this.speed = speed;
        }

        @Override
        public void start() {
            setMotorMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        @Override
        public boolean updateStatus() {

            double power = getPowerCurveForPosition(heading.getValue(), initialHeading.getValue(), targetHeading.getValue(), speed);

            //if problems check this
            if (targetHeading.delta(heading) < 0) {
                power = -power;
            }
            double progress = scaleProgress(heading.getValue(), initialHeading.getValue(), targetHeading.getValue());

            telemetry.addData("Heading", heading);
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

    private static class MotorTicks {
        int backLeft;
        int backRight;
        int frontLeft;
        int frontRight;

        public MotorTicks(int backLeft, int backRight, int frontLeft, int frontRight) {
            this.backLeft = backLeft;
            this.backRight = backRight;
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
        }

        public String toString() {
            return String.format(
                    "BL %d\tBR %d\tFL %d\tFR%d",
                    backLeft, backRight, frontLeft, frontRight
            );
        }
    }

}
