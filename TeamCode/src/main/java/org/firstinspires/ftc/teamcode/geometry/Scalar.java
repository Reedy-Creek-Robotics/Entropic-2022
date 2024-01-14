package org.firstinspires.ftc.teamcode.geometry;

public class Scalar {
    double val;
    ScalarUnit unit;

    public Scalar(double val, ScalarUnit unit) {
        this.val = val;
        this.unit = unit;
    }

    public Scalar(double val) {
        this.val = val;
        this.unit = ScalarUnit.UNITS;
    }

    Scalar convertUnitsTo(ScalarUnit newUnit) {
        this.unit = newUnit;
        this.val *= this.unit.conversionList[newUnit.conversionIndex];
        return this;
    }

    public enum ScalarUnit {
        //TODO: fill in zeros
        UNITS(0, 1, 1, 1, 1, 1),
        TICKS(1, 1, 1, 0, 0, 0),
        CENTIMETERS(2, 1, 0, 1, 0.3937, 0.01640),
        INCHES(3, 1, 0, 2.54, 1, 0.04167),
        TILES(4, 1, 0, 60.96, 24, 1);

        final int conversionIndex;
        final double[] conversionList;

        ScalarUnit(int conversionIndex, double... conversionList) {
            this.conversionIndex = conversionIndex;
            this.conversionList = conversionList;
        }
    }
}
