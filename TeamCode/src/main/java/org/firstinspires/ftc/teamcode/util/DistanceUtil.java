package org.firstinspires.ftc.teamcode.util;

public class DistanceUtil {

    /**
     * Diameter of the wheel in tiles.
     */
    private static final double WHEEL_SIZE = (0.328084 * 12) / 23.625; // inches / (23.625 in / tile);

    /**
     * The number of encoder ticks that pass in a complete revolution of the motor.
     */
    private static final double TICKS_PER_REVOLUTION = 537.6;

    /**
     * Converts inches to tiles.
     */
    public static double inches(double inches) {
        return inches / (189.0/8);
    }

    /**
     * Converts tiles traveled into number of ticks moved by
     *
     * @param distance how far you want to travel in tiles
     * @return number of ticks to move
     */
    public static int tilesToTicks(double distance) {
        double wheelCircumference = (WHEEL_SIZE * Math.PI);
        double wheelRevolutions = distance / wheelCircumference;

        return (int) Math.round(wheelRevolutions * TICKS_PER_REVOLUTION);
    }

    /**
     * Converts ticks moved by the motor into the number of tiles traveled.
     *
     * @param ticks the number of ticks that were moved by the motor
     * @return the distance in tiles that the robot moved
     */
    public static double ticksToTiles(double ticks) {
        double wheelCircumference = (WHEEL_SIZE * Math.PI);
        double wheelRevolutions = ticks / TICKS_PER_REVOLUTION;

        return wheelRevolutions * wheelCircumference;
    }

}
