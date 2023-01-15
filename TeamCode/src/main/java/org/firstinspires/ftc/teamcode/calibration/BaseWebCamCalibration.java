package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.Button.A;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Controller.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controller.Button.START;
import static org.firstinspires.ftc.teamcode.Controller.Button.X;
import static org.firstinspires.ftc.teamcode.Controller.Button.Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseTeleOp;
import org.firstinspires.ftc.teamcode.Controller;
import org.firstinspires.ftc.teamcode.RobotDescriptor.WebCamDescriptor;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.WebCam;
import org.firstinspires.ftc.teamcode.components.WebCam.FrameContext;
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

public abstract class BaseWebCamCalibration extends BaseTeleOp {

    /**
     * The center of the calibration mat (in inches), marked with intersecting diagonal lines.
     */
    private static final Position MAT_CENTER = new Position(8.5, 6.5); // inches

    private Size resolution;
    private Position viewCenter;

    private boolean calibrationMode = false;
    private boolean showGrid = false;

    private enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    private Map<Corner, AnchorPoint> cornerPoints;
    private Corner activeCorner;
    private AnchorPoint activeCornerPoint;

    private WebCam webCam;
    private WebCamDescriptor webCamDescriptor;

    protected abstract WebCam getWebCam();

    @Override
    public void init() {
        super.init();

        webCam = getWebCam();
        webCamDescriptor = webCam.getWebCamDescriptor();
        resolution = webCamDescriptor.resolution;
        viewCenter = new Position(resolution.width / 2, resolution.height / 2);
        controller = new Controller(gamepad1);

        resetCalibration();

        robot.getWebCamSide().setFrameProcessor(new WebCam.FrameProcessor() {
            @Override
            public void processFrame(Mat input, Mat output, FrameContext frameContext) {

                if (!calibrationMode) {

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
                                viewCenter.add(new Vector2(-50, -80)), Color.ORANGE);
                        DrawUtil.drawText(output, activeCornerPoint.position.toString(1),
                                viewCenter.add(new Vector2(-50, -40)), Color.ORANGE);

                    }

                } else {
                    Position topLeft = webCamDescriptor.topLeft.robot;
                    Position topRight = webCamDescriptor.topRight.robot;
                    Position bottomLeft = webCamDescriptor.bottomLeft.robot;
                    Position bottomRight = webCamDescriptor.bottomRight.robot;

                    Viewport viewport = new Viewport(
                            resolution.width, resolution.height,
                            topLeft, topRight,
                            bottomLeft, bottomRight
                    );

                    if (showGrid) {
                        long minX = Math.round(Math.min(topLeft.getX(), bottomLeft.getX())) - 1;
                        long minY = Math.round(Math.min(bottomLeft.getY(), bottomRight.getY())) - 1;
                        long maxX = Math.round(Math.max(topRight.getX(), bottomRight.getX())) + 1;
                        long maxY = Math.round(Math.max(topLeft.getY(), topRight.getY())) + 1;

                        for (long x = minX; x <= maxX; x++) {
                            Line line = new Line(
                                    viewport.convertExternalToView(new Position(x, minY)),
                                    viewport.convertExternalToView(new Position(x, maxY))
                            );
                            DrawUtil.drawLine(output, line, Color.BLUE, 1);
                        }
                        for (long y = minY; y <= maxY; y++) {
                            Line line = new Line(
                                    viewport.convertExternalToView(new Position(minX, y)),
                                    viewport.convertExternalToView(new Position(maxX, y))
                            );
                            DrawUtil.drawLine(output, line, Color.BLUE, 1);
                        }
                    }

                    if (activeCornerPoint != null) {
                        Position viewPosition = activeCornerPoint.viewPosition;
                        Position position = viewport.convertViewToExternal(viewPosition);
                        DrawUtil.drawMarker(output, activeCornerPoint.viewPosition, Color.ORANGE);

                        DrawUtil.drawText(output, "Test Mode",
                                viewCenter.add(new Vector2(-50, -80)), Color.ORANGE);
                        DrawUtil.drawText(output, "Pixel: " + activeCornerPoint.viewPosition.toString(0),
                                viewCenter.add(new Vector2(-100, -40)), Color.ORANGE);
                        DrawUtil.drawText(output, "Inches: " + position.toString(),
                                viewCenter.add(new Vector2(-100, 0)), Color.ORANGE);
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
        if (controller.isPressed(START)) {
            resetCalibration();
        } else if (controller.isPressed(Y)) {
            calibrationMode = !calibrationMode;
        } else if (controller.isPressed(X)) {
            showGrid = !showGrid;
        } else if (controller.isPressed(A)) {
            cornerPoints.put(activeCorner, activeCornerPoint);
            startCorner(nextCorner(activeCorner));
        } else if (controller.isPressed(DPAD_LEFT)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(-0.5, 0));
        } else if (controller.isPressed(DPAD_RIGHT)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(0.5, 0));
        } else if (controller.isPressed(DPAD_UP)) {
            activeCornerPoint.position = activeCornerPoint.position.add(new Vector2(0, 0.5));
        } else if (controller.isPressed(DPAD_DOWN)) {
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
