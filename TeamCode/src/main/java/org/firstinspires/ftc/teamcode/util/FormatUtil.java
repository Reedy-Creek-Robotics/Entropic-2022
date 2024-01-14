package org.firstinspires.ftc.teamcode.util;

public class FormatUtil {

    public static String format(Double value) {
        return format(value, 3);
    }

    public static String format(Double value, int precision) {
        if (value == null) return "";
        return String.format("%." + precision + "f", value);
    }

}
