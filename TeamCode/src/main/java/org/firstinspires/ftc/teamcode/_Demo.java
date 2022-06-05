package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inches;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Point;

@Autonomous
public class _Demo extends LinearOpMode {

    public static final int PARALLEL_TO_TILE_EDGE = 0;
    protected Robot robot;
    protected double fieldSide;
    protected BarCodePositionDetector.BarCodePosition barCodePosition;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        robot.getDriveTrain().alignToTileAngle(0,.3);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveDistanceFromTileEdge(inches(3),0.3);
        robot.waitForCommandsToFinish();

        robot.waitForStop();
    }

    protected void initRobot() {
        robot = new Robot(this);
        initBarCodePositions();
        robot.init();
    }

    private void initBarCodePositions() {
        Point first = new Point(135, 100);
        Point second = new Point(360, 100);

        robot.getBarCodePositionDetector().setBarCodePositions(first, second, null, BarCodePosition.RIGHT);
    }

    protected Arm.Position getArmPositionForBarCode(BarCodePosition barCodePosition) {
        switch (barCodePosition) {
            case LEFT: return Arm.Position.LOW;
            case MIDDLE: return Arm.Position.MEDIUM;
            case RIGHT: return Arm.Position.HIGH;
            default: throw new IllegalArgumentException();
        }
    }
}

