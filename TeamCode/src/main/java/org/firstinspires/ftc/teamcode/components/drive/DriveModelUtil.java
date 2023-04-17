package org.firstinspires.ftc.teamcode.components.drive;

import static org.firstinspires.ftc.teamcode.RobotDescriptor.EmpiricalStrafeCorrection;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.DistanceUtil;
import org.firstinspires.ftc.teamcode.util.RampUtil;
import org.firstinspires.ftc.teamcode.util.ScalingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DriveModelUtil {

    /**
     * Converts tiles traveled into number of ticks moved by
     *
     * @param distance how far you want to travel in tiles
     * @return number of ticks to move
     */
    public static int tilesToTicks(RobotDescriptor robotDescriptor, double distance) {
        double wheelSizeInTiles = DistanceUtil.toTiles(robotDescriptor.wheelSizeInMm, DistanceUnit.MM);
        double wheelCircumference = wheelSizeInTiles * Math.PI;
        double wheelRevolutions = distance / wheelCircumference;

        double ticksPerRevolution = robotDescriptor.wheelMotorEncoderTicksPerRevolution;
        return (int) Math.round(wheelRevolutions * ticksPerRevolution);
    }

    /**
     * Converts ticks moved by the motor into the number of tiles traveled.
     *
     * @param ticks the number of ticks that were moved by the motor
     * @return the distance in tiles that the robot moved
     */
    public static double ticksToTiles(RobotDescriptor robotDescriptor, double ticks) {
        double wheelSizeInTiles = DistanceUtil.toTiles(robotDescriptor.wheelSizeInMm, DistanceUnit.MM);
        double wheelCircumference = wheelSizeInTiles * Math.PI;
        double wheelRevolutions = ticks / robotDescriptor.wheelMotorEncoderTicksPerRevolution;

        return wheelRevolutions * wheelCircumference;
    }
}
