package org.firstinspires.ftc.teamcode.geometry;

public class Line {

    private Position p1, p2;

    public Line(Position p1, Position p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Position getP1() {
        return p1;
    }

    public Position getP2() {
        return p2;
    }

    /**
     * Returns an equivalent line with the guarantee that its p1 and p2 x values are increasing.
     */
    public Line normalizeX() {
        return p1.getX() > p2.getX() ?
                new Line(p2, p1) :
                this;
    }

    /**
     * Returns an equivalent line with the guarantee that its p1 and p2 y values are increasing.
     */
    public Line normalizeY() {
        return p1.getY() > p2.getY() ?
                new Line(p2, p1) :
                this;
    }

    /**
     * Returns an equivalent line with the guarantee that its p1 and p2 values are in ascending
     * order of x, or ascending order of y if the line is vertical.
     */
    public Line normalize() {
        return p1.getX() != p2.getX() ?
                normalizeX() :
                normalizeY();
    }

    /**
     * Finds the point where this line intersects with the other line.
     */
    public Position intersect(Line other) {
        // Matrix determinant solution
        // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line

        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();
        double x3 = other.p1.getX(), y3 = other.p1.getY();
        double x4 = other.p2.getX(), y4 = other.p2.getY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        double x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        double y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;

        return new Position(x, y);
    }

    /**
     * Returns the point on the line with the given x coordinate.
     */
    public Position withX(double x) {
        // Put in slope intercept form, y = mx + b
        double m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double b = p1.getY() - m * p1.getX();
        return new Position(x, m * x + b);
    }

    public Position withY(double y) {
        // Put in vertical slope intercept form, x = my + b
        double m = (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
        double b = p1.getX() - m * p1.getY();
        return new Position(m * y + b, y);
    }

    /**
     * Returns the angle in degrees between the line and the positive X axis, in the range
     * (-90 < angle <= 90).
     */
    public double getAngleToX() {
        // Convert to a vector
        Line normalized = normalizeX();
        Vector2 offset = normalized.p2.minus(normalized.p1);

        // Because line is normalized in x direction, heading should be from (0-90), or (270-360).
        double theta = offset.toHeading().getValue();
        if (theta >= 270) theta = -(360 - theta);
        if (theta <= -90) theta += 180;

        return theta;
    }

    /**
     * Returns the angle in degrees between the line and the positive Y axis, in the range
     * (-90 < angle <= 90).
     */
    public double getAngleToY() {
        // Convert to a vector
        Line normalized = normalizeY();
        Vector2 offset = normalized.p2.minus(normalized.p1);

        // Because line is normalized in y direction, heading should be from (0-90), or (90-180).
        double theta = offset.toHeading().getValue() - 90;
        if (theta <= -90) theta += 180;

        return theta;
    }

    /**
     * Returns the angle in degrees between the line and the other line, in the range
     * (-90 < angle <= 90).
     */
    public double getAngleToLine(Line otherLine) {
        double angle = toVector().angleTo(otherLine.toVector());
        if (angle > 90) angle = angle - 180;
        return angle;
    }

    /**
     * Determines if a given position falls to the left side of the line, when drawn from p1 to p2.
     */
    public boolean isLeft(Position position) {
        return ((p2.getX() - p1.getX()) * (position.getY() - p1.getY()) -
                (p2.getY() - p1.getY()) * (position.getX() - p1.getX())) > 0;
    }

    /**
     * Convert this line into a vector.
     */
    public Vector2 toVector() {
        return p2.minus(p1);
    }

}
