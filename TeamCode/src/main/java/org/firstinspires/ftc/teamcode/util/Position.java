package org.firstinspires.ftc.teamcode.util;

import android.annotation.SuppressLint;

import org.opencv.core.Point;

public class Position {
    private double x, y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns a new position that is the given distance away from this one, at the given heading.
     */
    public Position move(double distance, Heading heading) {
        double headingInRadians = heading.getValue() * Math.PI / 180;
        return new Position(
                x + Math.cos(headingInRadians) * distance,
                y + Math.sin(headingInRadians) * distance
        );
    }

    public Position add(Vector2 vector) {
        return new Position(
                x + vector.getX(),
                y + vector.getY()
        );
    }

    /**
     * Calculates the distance between this position and another.
     */
    public double distance(Position other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the offset between this position and another.
     */
    public Vector2 offset(Position other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return new Vector2(dx, dy);
    }

    /**
     * Aligns the given position to the middle of the nearest tile.
     */
    public Position alignToTileMiddle() {
        return new Position(
                Math.round(x - 0.5) + 0.5,
                Math.round(y - 0.5) + 0.5
        );
    }

    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }

}
