package org.firstinspires.ftc.teamcode.geometry;

import android.annotation.SuppressLint;

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
     * Calculates the distance between this position and the line that passes through the given points.
     */
    public double distance(Line line) {
        double x1 = line.getP1().x, y1 = line.getP1().y;
        double x2 = line.getP2().x, y2 = line.getP2().y;
        double top = Math.abs((x2 - x1) * (y1 - y) - (x1 - x) * (y2 - y1));
        double bottom = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return top / bottom;
    }

    /**
     * Calculates the offset between this position and another.
     */
    public Vector2 minus(Position other) {
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

    /**
     * Aligns the given position to the edge of the nearest tile.
     */
    public Position alignToTileEdge() {
        return new Position(Math.round(x * 2.0) / 2.0, Math.round(y * 2.0) / 2.0);
    }

    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }

    public String toString(int precision) {
        return String.format("(%." + precision + "f, %." + precision + "f)", x, y);
    }

}
