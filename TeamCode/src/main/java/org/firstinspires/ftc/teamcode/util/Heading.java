package org.firstinspires.ftc.teamcode.util;

public class Heading {

    /**
     * The heading angle in degrees.
     */
    private double value;

    public Heading(double value) {
        this.value = normalize(value);
    }

    /**
     * Returns the value of this heading as an angle in degrees.
     *
     * This will always be in the range (0 to 360)
     */
    public double getValue() {
        return value;
    }

    /**
     * Adds the given delta in degrees to this heading, returning a new Heading instance.
     */
    public Heading add(double delta) {
        return new Heading(value + delta);
    }

    /**
     * Returns the delta in degrees between this heading and the given other heading.
     *
     * This will always be in the range (-180 to 180)
     */
    public double delta(Heading other) {
        double result = normalize(value - other.value);
        if (result > 180) result -= 360;
        return result;
    }

    /**
     * Normalize the given angle in degrees to a range between (0 to 360)
     */
    private double normalize(double value) {
        double result = value % 360;
        if (result < 0) {
            result += 360;
        }
        return result;
    }

}
