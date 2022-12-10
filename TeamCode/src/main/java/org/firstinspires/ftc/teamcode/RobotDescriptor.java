package org.firstinspires.ftc.teamcode;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The diameter of the robot's wheels in mm.
     */
    public double wheelSizeInMm = 100.0;

    /**
     * The number of encoder ticks that the robot's drive train motors count per full revolution.
     */
    public double wheelMotorEncoderTicksPerRevolution = 537.6;

    /**
     * For ramping up, the maximum ratio of robot speed in tiles/sec to abs motor power (0-1).
     */
    public double rampingUpMaximumSpeedToMotorPowerRatio = 2.0; // todo: measure this empirically

    /**
     * For ramping down, the maximum ratio of motor power to the distance left to reach the target.
     */
    public double rampingDownMaximumMotorPowerToDistanceRemainingRatio = 1.0; // todo: measure this empirically

    /**
     * For ramping up or down, the minimum motor power that will be applied to move the robot.
     */
    public double rampingMinMotorPower = 0.05;

}
