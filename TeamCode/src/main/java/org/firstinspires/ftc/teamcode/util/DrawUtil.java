package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class DrawUtil {

    public static void drawText(Mat output, String text, Position position, Color color) {
        drawText(output, text, position, color, 1.0, 2);
    }

    public static void drawText(Mat output, String text, Position position, Color color, double scale, int thickness) {
        Imgproc.putText(
                output,
                text,
                toPoint(position),
                Imgproc.FONT_HERSHEY_COMPLEX, scale,
                color.toRGBA(),
                thickness
        );
    }

    public static void drawMarker(Mat output, Position point, Color color) {
        Imgproc.drawMarker(output, toPoint(point), color.toRGBA(), Imgproc.MARKER_CROSS, 5, 2);
    }

    public static void drawLine(Mat output, Line line, Color color) {
        drawLine(output, line, color, 2);
    }

    public static void drawLine(Mat output, Line line, Color color, int thickness) {
        Imgproc.line(
                output,
                toPoint(line.getP1()),
                toPoint(line.getP2()),
                color.toRGBA(),
                thickness
        );
    }

    private static Point toPoint(Position position) {
        return new Point(position.getX(), position.getY());
    }

}
