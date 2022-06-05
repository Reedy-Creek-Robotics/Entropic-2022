package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.BarCodePositionDetector.BarCodePosition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Point;

@Autonomous
public class _AutoTester extends LinearOpMode {

    public static final int PARALLEL_TO_TILE_EDGE = 0;
    protected Robot robot;
    protected double fieldSide;
    protected BarCodePositionDetector.BarCodePosition barCodePosition;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        //robot.getBarCodePositionDetector().detect();

        waitForStart();

        while(!isStopRequested()) {
            if (gamepad1.a) {

                robot.getLedStrip().activateLed("white",5);

                //Detect the barcode position
                robot.getBarCodePositionDetector().reset();
                barCodePosition = robot.getBarCodePositionDetector().detect();
                telemetry.addData("Bar Code Position", barCodePosition).setRetained(true);
                telemetry.update();

                robot.getLedStrip().stopLedStrip();
            }
        }
    }

    protected void initRobot() {
        robot = new Robot(this, 0);
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
