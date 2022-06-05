package org.firstinspires.ftc.teamcode.util;

import org.opencv.core.Scalar;

public enum Color {

    BLUE(255, 0, 0),
    GREEN(0, 255, 0),
    ORANGE(0, 255, 165),
    RED(0, 0, 255);

    double blue, green, red;

    Color(double blue, double green, double red) {
        this.blue = blue;
        this.green = green;
        this.red = red;
    }

    public Scalar toRGBA() {
        return new Scalar(red, green, blue, 255);
    }

    public Scalar toBGR() {
        return new Scalar(blue, green, red);
    }

}
