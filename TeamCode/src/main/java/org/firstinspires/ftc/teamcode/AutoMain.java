package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Point;


public abstract class AutoMain extends LinearOpMode {

    public static final int PARALLEL_TO_TILE_EDGE = 0;
    protected Robot robot;
    protected double fieldSide;
    protected BarCodePositionDetector.BarCodePosition barCodePosition;

    enum StartPosition {
        RED_DUCK,
        RED_WAREHOUSE,
        BLUE_DUCK,
        BLUE_WAREHOUSE
    }

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        //turn on LEDs to light up QR code
        robot.getLedStrip().activateLed("white",5);

        //Detect the barcode position
        robot.getBarCodePositionDetector().reset();
        barCodePosition = robot.getBarCodePositionDetector().detect();
        telemetry.addData("Bar Code Position", barCodePosition).setRetained(true);
        telemetry.update();

        //turn off LEDs
        robot.getLedStrip().stopLedStrip();

        if(robot.getRearWebCam().getStreamOutput())
            robot.waitForStop();

    }

    protected void initRobot() {
        robot = new Robot(this, true);
        initBarCodePositions();
        robot.init();
    }

    protected void robotStart() {
        robot.getCapper().goToPosition(Capper.Position.TRAVEL);
        robot.waitForCommandsToFinish();
    }

    private void initBarCodePositions() {

        Point first = new Point(135, 100);
        Point second = new Point(410, 100);

        if (getStartPosition() == StartPosition.RED_DUCK) {
            robot.getBarCodePositionDetector().setBarCodePositions(
                    first,
                    second,
                    null,
                    BarCodePosition.RIGHT
            );

        } else if (getStartPosition() == StartPosition.RED_WAREHOUSE) {
            robot.getBarCodePositionDetector().setBarCodePositions(
                    first,
                    second,
                    null,
                    BarCodePosition.RIGHT
            );

        } else if (getStartPosition() == StartPosition.BLUE_WAREHOUSE) {
            robot.getBarCodePositionDetector().setBarCodePositions(
                    null,
                    first,
                    second,
                    BarCodePosition.LEFT
            );

        } else if (getStartPosition() == StartPosition.BLUE_DUCK) {
            robot.getBarCodePositionDetector().setBarCodePositions(
                    null,
                    first,
                    second,
                    BarCodePosition.LEFT
            );

        } else {
            throw new IllegalArgumentException();
        }
    }

    protected Arm.Position getArmPositionForBarCode(BarCodePosition barCodePosition) {
        switch (barCodePosition) {
            case LEFT: return Arm.Position.LOW;
            case MIDDLE: return Arm.Position.MEDIUM;
            case RIGHT: return Arm.Position.HIGH;
            default: throw new IllegalArgumentException();
        }
    }

    abstract StartPosition getStartPosition();

}
