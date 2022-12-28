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

        return theta > 180 ? theta - 360 : theta;
    }

}
