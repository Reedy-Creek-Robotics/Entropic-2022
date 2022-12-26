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
     * Returns the angle in degrees between the line and the X axis, in the range (0-180).
     */
    public double getAngle() {

        // todo: write a test case

        //todo: may have this backward
        Vector2 offset = p1.minus(p2);

        //todo: is form 0 - 360 as opposed to -180 to 180
        double theta = offset.toHeading().getValue();

        return theta > 180 ? theta - 360 : theta;
    }

}
