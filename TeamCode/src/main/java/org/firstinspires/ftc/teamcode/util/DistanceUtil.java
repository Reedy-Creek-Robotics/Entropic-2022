package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public class DistanceUtil {

    /**
     * The size of each tile in inches.
     */
    private static final double INCHES_PER_TILE = 23.625;

    /**
     * Converts a distance with arbitrary units to tiles.
     */
    public static double toTiles(double distance, DistanceUnit unit) {
        return inchesToTiles(DistanceUnit.INCH.fromUnit(unit, distance));
    }

    public static double tilesToInches(double distance) {
        return distance * INCHES_PER_TILE;
    }

    /**
     * Converts inches to tiles.
     */
    public static double inchesToTiles(double inches) {
        return inches / INCHES_PER_TILE;
    }

    public static Vector2 inchesToTiles(Vector2 vectorInInches) {
        double xInTiles = inchesToTiles(vectorInInches.getX());
        double yInTiles = inchesToTiles(vectorInInches.getY());
        return new Vector2(xInTiles, yInTiles);
    }

}
