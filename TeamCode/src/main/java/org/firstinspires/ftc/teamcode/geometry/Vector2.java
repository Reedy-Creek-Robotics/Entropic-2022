package org.firstinspires.ftc.teamcode.geometry;

import android.annotation.SuppressLint;

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
        return new Heading(Math.toDegrees(Math.atan2(y, x)));
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(
                x + other.x,
                y + other.y
        );
    }

    public Vector2 rotate(double angle) {
        double theta = Math.toRadians(angle);
        return new Vector2(
                x * Math.cos(theta) - y * Math.sin(theta),
                y * Math.cos(theta) + x * Math.sin(theta)
        );
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 withMagnitude(double magnitude) {
        double scalar = magnitude / this.magnitude();
        return multiply(scalar);
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public double angleTo(Vector2 other) {
        return Math.toDegrees(Math.acos(dot(other) / (magnitude() * other.magnitude())));
    }

    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
