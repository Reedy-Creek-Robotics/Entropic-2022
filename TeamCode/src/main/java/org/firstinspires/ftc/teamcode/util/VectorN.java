package org.firstinspires.ftc.teamcode.util;

public class VectorN {

    private double[] values;

    public VectorN(double... values) {
        this.values = values;
    }

    public double get(int index) {
        return values[index];
    }

    public double magnitude() {
        double sum = 0.0;
        for (double value : values) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    public VectorN multiply(double scalar) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] * scalar;
        }
        return new VectorN(result);
    }

    public VectorN withMagnitude(double magnitude) {
        double scalar = magnitude / this.magnitude();
        return multiply(scalar);
    }

}
