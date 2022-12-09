package org.firstinspires.ftc.teamcode;

/**
 * Variables that indicate the physical characteristics of the robot.
 */
public class RobotDescriptor {

    /**
     * The diameter of the robot's wheels in mm.
     */
    private double wheelSizeInMm;

    /**
     * The number of encoder ticks that the robot's drive train motors count per full revolution.
     */
    private double wheelMotorEncoderTicksPerRevolution;

    /**
     * For ramping up, the maximum ratio of robot speed in tiles/sec to abs motor power (0-1).
     */
    private double rampingMaximumSpeedToMotorPowerRatio;

    /**
     * For ramping up or down, the minimum motor power that will be applied to move the robot.
     */
    private double rampingMinMotorPower;


    public double getWheelSizeInMm() {
        return wheelSizeInMm;
    }

    public double getWheelMotorEncoderTicksPerRevolution() {
        return wheelMotorEncoderTicksPerRevolution;
    }

    public double getRampingMaximumSpeedToMotorPowerRatio() {
        return rampingMaximumSpeedToMotorPowerRatio;
    }

    public double getRampingMinMotorPower() {
        return rampingMinMotorPower;
    }

}
