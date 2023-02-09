package org.firstinspires.ftc.teamcode.util;

public class FormatUtil {

    public static String format(double value) {
        return format(value, 3);
    }

    public static String format(double value, int precision) {
        return String.format("%." + precision + "f", value);
    }

}
