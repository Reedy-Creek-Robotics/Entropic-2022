package org.firstinspires.ftc.teamcode.geometry;

public class Angle {
    double val;
    AngleUnit unit;
    AngleType type;
    double maxDomain;
    double minDomain;

    public Angle(double val, AngleUnit unit, AngleType type, double maxDomain) {
        this.val = val;
        this.unit = unit;
        this.type = type;
        this.maxDomain = maxDomain;
        this.minDomain = maxDomain - 1;
    }
    //simple constructor
    public Angle(double val) {
        this.val = val;
        this.unit = AngleUnit.Degrees;
        this.type = AngleType.BEARING;
        this.maxDomain = 1;
        this.minDomain = maxDomain - 1;
    }

    //TODO: add delta method to get difference between angles

    //TODO: add method to align to right angle;

    void setDomain(double maxDomain){
        this.maxDomain = maxDomain;
        this.minDomain = maxDomain - 1;
    }

    /*TODO:implement way to change domain
    IDEA: create value where you set max domain on a scale of 0-1
    Min is derived by subtracting one
    whenever max is reached min multiplied by the current unit is added to input
     */
    //not correct
    double toDomain(double input){
        return ((input+(maxDomain)*unit.scale)%unit.scale)-maxDomain*unit.scale;
    }

    Angle convertUnitTo(AngleUnit newUnit){
        val *= unit.conversionList[newUnit.conversionIndex];
        unit = newUnit;
        return this;
    }

    Angle convertTypeTo(AngleType newType){
        val = 360 - toDomain(val) + type.conversionList[newType.conversionIndex];
        type = newType;
        return this;
    }

    public enum AngleUnit{
        Radians(2*Math.PI,0,1,180/Math.PI),
        Degrees(360,1,Math.PI/180,1);

        int conversionIndex;
        double[] conversionList;
        double scale;

        AngleUnit(double scale, int conversionIndex, double ... conversionList) {
            this.conversionIndex = conversionIndex;
            this.conversionList = conversionList;
            this.scale = scale;
        }
    }
    public enum AngleType {
        //TODO: fill in conversionList
        BEARING     (0,1,-90),
        DIRECTION   (1,90,1);

        final int conversionIndex;
        final double[] conversionList;
        AngleType(int conversionIndex, double... conversionList) {
            this.conversionIndex = conversionIndex;
            this.conversionList = conversionList;
        }
    }



}
