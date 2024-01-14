package org.firstinspires.ftc.teamcode.util;

public class ScalingUtil {

    public static double scaleLinear(double value, double inputMin, double inputMax, double scaleMin, double scaleMax) {
        double scaledX = (value - inputMin) / (inputMax - inputMin);
        double scaledY = scaledX * (scaleMax - scaleMin) + scaleMin;

        return scaledY;
    }

    public static double scaleQuadratic(double value, double inputMin, double inputMax, double scaleMin, double scaleMax) {
        double scaledX = (value - inputMin) / (inputMax - inputMin);
        double scaledY = (scaledX * scaledX) * (scaleMax - scaleMin) + scaleMin;

        return scaledY;
    }

    public static double scaleParabolic(double value, double exponent, double inputMin, double inputMax, double scaleMin, double scaleMax) {
        double scaledX = (value - inputMin) / (inputMax - inputMin);
        double scaledY = Math.pow(scaledX, exponent) * (scaleMax - scaleMin) + scaleMin;

        return scaledY;
    }

}
