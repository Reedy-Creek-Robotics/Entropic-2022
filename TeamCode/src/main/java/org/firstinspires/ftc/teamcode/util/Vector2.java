package org.firstinspires.ftc.teamcode.util;

public class Vector2 {
    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Heading toHeading() {
        double epsilon = 1e-5;
        if (Math.abs(x) < epsilon) {
            return y > 0 ? new Heading(90) : new Heading(-90);
        } else {
            return new Heading(Math.toDegrees(Math.atan(y / x)));
        }
    }

}
