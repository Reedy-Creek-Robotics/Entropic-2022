package org.firstinspires.ftc.teamcode.util;

import org.opencv.core.Scalar;

public enum Color {

    BLACK(0, 0, 0),
    LIGHT_GRAY(170, 170, 170),
    BLUE(255, 0, 0),
    GREEN(0, 255, 0),
    ORANGE(0, 165, 255),
    RED(0, 0, 255),
    WHITE(255, 255, 255),
    PURPLE(240, 32, 160);

    final double blue, green, red;

    Color(double blue, double green, double red) {
        this.blue = blue;
        this.green = green;
        this.red = red;
    }

    public Scalar toRGBA() {
        return new Scalar(red, green, blue, 255);
    }

}
