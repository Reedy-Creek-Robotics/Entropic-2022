package org.firstinspires.ftc.teamcode.util;

import org.opencv.core.Point;

public class PointUtil {

    /**
     * Calculates the distance between two points.
     */
    public static double distance(Point a, Point b) {
        double deltaX = a.x - b.x;
        double deltaY = a.y - b.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Calculates a new position at a given distance and heading from an origin point.
     *
     * @param distance the distance to move
     * @param heading  the heading to move (in degrees)
     */
    public static Point move(Point origin, double distance, double heading) {
        double headingInRadians = heading * Math.PI / 180;
        return new Point(
                origin.x + Math.cos(headingInRadians) * distance,
                origin.y + Math.sin(headingInRadians) * distance
        );
    }

}
