package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Controller.Button;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.WebCam;
import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.geometry.Viewport;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.HashMap;
import java.util.Map;

@TeleOp
public class WebCamCalibration extends OpMode {

    /**
     * The center of the calibration mat (in inches), marked with intersecting diagonal lines.
     */
    private static final Position MAT_CENTER = new Position(8.5, 6.5); // inches

    private Robot robot;

    private Size resolution;
    private Position viewCenter;

    private Controller controller;

    private boolean annotateOutput = true;

    private enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    private Map<Corner, AnchorPoint> cornerPoints;
    private Corner activeCorner;
    private AnchorPoint activeCornerPoint;

    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();

        RobotDescriptor descriptor = robot.getRobotContext().robotDescriptor;
        resolution = descriptor.webCamResolution;
        viewCenter = new Position(resolution.width / 2, resolution.height / 2);
        controller = new Controller(gamepad1);

        resetCalibration();

        robot.getWebCamSide().setFrameProcessor(new WebCam.FrameProcessor() {
            @Override
            public void processFrame(Mat input, Mat output) {

                if (annotateOutput) {

                    // Draw the corners that have been set so far, and lines between
                    for (Corner corner : Corner.values()) {
                        Corner nextCorner = Corner.values()[(corner.ordinal() + 1) % Corner.values().length];
                        AnchorPoint cornerPoint = cornerPoints.get(corner);
                        AnchorPoint nextCornerPoint = cornerPoints.get(nextCorner);
                        if (cornerPoint != null && nextCornerPoint != null) {
                            DrawUtil.drawLine(output,
                                    new Line(cornerPoint.viewPosition, nextCornerPoint.viewPosition),
                                    Color.BLUE, 3
                            );
                        }

                        if (cornerPoint != null) {
                            DrawUtil.drawMarker(output, cornerPoint.viewPosition, Color.BLUE);

                            Position screenCorner = imageCorner(corner);
                            Position labelPosition = screenCorner.add(viewCenter.minus(screenCorner).multiply(0.3));
                            DrawUtil.drawText(output, cornerPoint.position.toString(), labelPosition, Color.GREEN, 0.5, 1);
                        }
                    }

                    // Draw the active corner point
                    if (activeCornerPoint != null && activeCorner != null) {
                        DrawUtil.drawMarker(output, activeCornerPoint.viewPosition, Color.ORANGE);

                        DrawUtil.drawText(output, activeCorner.toString(),
                                viewCenter.add(new Vector2(-50, -20)), Color.ORANGE);
                        DrawUtil.drawText(output, activeCornerPoint.position.toString(),
                                viewCenter.add(new Vector2(-50, 20)), Color.ORANGE);
                    }
                }
            }
        });
    }

    private void resetCalibration() {
        cornerPoints = new HashMap<>();
        startCorner(Corner.TOP_LEFT);
    }

    @Override
    public void loop() {
        if (controller.isPressed(Button.START)) {
            resetCalibration();
        } else if (controller.isPressed(Button.Y)) {
            annotateOutput = !annotateOutput;
        } else if (controller.isPressed(Button.A)) {
            cornerPoints.put(activeCorner, activeCornerPoint);
            startCorner(nextCorner(activeCorner));
        } else if (controller.isPressed(Button.DPAD_LEFT)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(-0.5, 0));
        } else if (controller.isPressed(Button.DPAD_RIGHT)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(0.5, 0));
        } else if (controller.isPressed(Button.DPAD_UP)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(0, 0.5));
        } else if (controller.isPressed(Button.DPAD_DOWN)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(0, -0.5));
        }

        if (controller.leftStickX() != 0.0 || controller.leftStickY() != 0.0) {
            double moveSpeed = 0.5;
            activeCornerPoint.viewPosition = activeCornerPoint.viewPosition.add(
                    new Vector2(-controller.leftStickX(), -controller.leftStickY()).multiply(moveSpeed)
            );
        }

        Viewport viewport = createViewport();
        if (viewport != null) {
            telemetry.addData("Image Top Left", viewport.convertViewToExternal(imageCorner(Corner.TOP_LEFT)));
            telemetry.addData("Image Top Right", viewport.convertViewToExternal(imageCorner(Corner.TOP_RIGHT)));
            telemetry.addData("Image Bottom Left", viewport.convertViewToExternal(imageCorner(Corner.BOTTOM_LEFT)));
            telemetry.addData("Image Bottom Right", viewport.convertViewToExternal(imageCorner(Corner.BOTTOM_RIGHT)));
        }

        robot.updateStatus();
    }

    private Viewport createViewport() {
        AnchorPoint topLeft = cornerPoints.get(Corner.TOP_LEFT);
        AnchorPoint topRight = cornerPoints.get(Corner.TOP_RIGHT);
        AnchorPoint bottomLeft = cornerPoints.get(Corner.BOTTOM_LEFT);
        AnchorPoint bottomRight = cornerPoints.get(Corner.BOTTOM_RIGHT);

        if (topLeft != null && topRight != null && bottomLeft != null && bottomRight != null) {
            return new Viewport(
                    topLeft.viewPosition, topRight.viewPosition,
                    bottomLeft.viewPosition, bottomRight.viewPosition,
                    topLeft.position, topRight.position,
                    bottomLeft.position, bottomRight.position
            );
        } else {
            return null;
        }
    }

    private Corner nextCorner(Corner corner) {
        return Corner.values()[(corner.ordinal() + 1) % Corner.values().length];
    }

    private void startCorner(Corner corner) {
        AnchorPoint existing = cornerPoints.get(corner);
        if (existing == null) {
            double startingY = (corner == Corner.TOP_LEFT || corner == Corner.TOP_RIGHT) ?
                    resolution.height / 10 : resolution.height * 9 / 10;
            double startingX = (corner == Corner.TOP_LEFT || corner == Corner.BOTTOM_LEFT) ?
                    resolution.width / 10 : resolution.width * 9 / 10;

            Position viewPosition = new Position(startingX, startingY);

            activeCornerPoint = new AnchorPoint(MAT_CENTER, viewPosition);
        } else {
            activeCornerPoint = existing;
        }
        activeCorner = corner;
    }

    private Position imageCorner(Corner corner) {
        switch (corner) {
            case TOP_LEFT:
                return new Position(0, 0);
            case TOP_RIGHT:
                return new Position(resolution.width, 0);
            case BOTTOM_RIGHT:
                return new Position(resolution.width, resolution.height);
            case BOTTOM_LEFT:
                return new Position(0, resolution.height);
            default:
                throw new IllegalStateException();
        }
    }

    private class AnchorPoint {
        private Position position;
        private Position viewPosition;

        public AnchorPoint(Position position, Position viewPosition) {
            this.position = position;
            this.viewPosition = viewPosition;
        }
    }

}
