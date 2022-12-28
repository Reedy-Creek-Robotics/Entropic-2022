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

    /*
    public Position withX(double x) {

    }

    public Position withY(double y) {

    }
    */

    /**
     * Returns the angle in degrees between the line and the X axis, in the range (0-180).
     */
    public double getAngle() {
        // Convert to a vector.
        Vector2 offset = p1.minus(p2);

        // Heading is from 0 - 360 as opposed to 0 to 180, so convert.
        double theta = offset.toHeading().getValue();

        return theta > 180 ? theta - 180 : theta;
    }

    /**
     * Determines if a given position falls to the left side of the line, when drawn from p1 to p2.
     */
    public boolean isLeft(Position position) {
        return ((p2.getX() - p1.getX()) * (position.getY() - p1.getY()) -
                (p2.getY() - p1.getY()) * (position.getX() - p1.getX())) > 0;
    }

}
