package org.firstinspires.ftc.teamcode.components.drive;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.geometry.VectorN;


public class MotorPowers {

    public double backLeft;
    public double backRight;
    public double frontLeft;
    public double frontRight;

    public MotorPowers(double backLeft, double backRight, double frontLeft, double frontRight) {
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
    }

    public VectorN toVectorN() {
        return new VectorN(
                backLeft,
                backRight,
                frontLeft,
                frontRight
        );
    }

    public static MotorPowers fromVectorN(VectorN vector) {
        return new MotorPowers(
                vector.get(0),
                vector.get(1),
                vector.get(2),
                vector.get(3)
        );
    }

    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format(
                "BL %.2f, BR %.2f, FL %.2f, FR %.2f",
                backLeft, backRight, frontLeft, frontRight
        );
    }
}