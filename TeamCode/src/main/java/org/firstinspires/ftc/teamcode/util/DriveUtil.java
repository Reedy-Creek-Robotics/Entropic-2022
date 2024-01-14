package org.firstinspires.ftc.teamcode.util;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.VectorN;

public interface DriveUtil {
    MotorPowers calculateWheelPowerForDrive(
            double drive,
            double strafe,
            double turn,
            double speedFactor
    );

    MotorPowers calculateWheelPowerForDriverRelative(
            double drive,
            double strafe,
            double turn,
            Heading heading,
            double speedFactor
    );

    class MotorPowers {
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
}

