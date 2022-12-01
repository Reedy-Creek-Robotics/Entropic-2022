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

    public Vector2 add(Vector2 other) {
        return new Vector2(
                x + other.x,
                y + other.y
        );
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

}
