package org.firstinspires.ftc.teamcode.geometry;

public class VectorN {

    private double[] values;

    public VectorN(double... values) {
        this.values = values;
    }

    public double get(int index) {
        return values[index];
    }

    public int size() {
        return values.length;
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

    /**
     * Scales the vector such that it's largest component value has the given absolute value maxAbsComponentValue.
     */
    public VectorN withMaxComponent(double maxAbsComponentValue) {
        double existingMax = Double.MIN_VALUE;
        for (double value : values) {
            double abs = Math.abs(value);
            if (abs > existingMax) {
                existingMax = abs;
            }
        }

        double scale = maxAbsComponentValue / existingMax;

        return multiply(scale);
    }

    /**
     * Clamps the vector such that it does not exceed the given max component absolute value.
     */
    public VectorN clampToMax(double maxAbsComponentValue) {
        double existingMax = Double.MIN_VALUE;
        for (double value : values) {
            double abs = Math.abs(value);
            if (abs > existingMax) {
                existingMax = abs;
            }
        }

        if (existingMax > maxAbsComponentValue) {
            double scale = maxAbsComponentValue / existingMax;
            return multiply(scale);
        }

        // No need to modify the vector, since it is already in the allowed range.
        return this;
    }

}
