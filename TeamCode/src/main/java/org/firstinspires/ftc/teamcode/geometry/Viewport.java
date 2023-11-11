package org.firstinspires.ftc.teamcode.geometry;

import org.firstinspires.ftc.teamcode.util.ScalingUtil;

/**
 * A viewport (e.g. from a webcam), with coordinates at each corner of the image in an external
 * coordinate system, such as relative to the corner of the robot.
 * <p>
 * The viewport image starts from (0,0) in the top left corner and counts up to (width,height) in
 * the bottom right corner of the image.
 */
public class Viewport {

    private Position viewTopLeft, viewTopRight, viewBottomLeft, viewBottomRight;
    private Position externalTopLeft, externalTopRight, externalBottomLeft, externalBottomRight;

    public Viewport(
            double viewWidth, double viewHeight,
            Position externalTopLeft, Position externalTopRight,
            Position externalBottomLeft, Position externalBottomRight
    ) {
        this(
                new Rectangle(0, viewWidth, viewHeight, 0),
                externalTopLeft, externalTopRight,
                externalBottomLeft, externalBottomRight
        );
    }

    public Viewport(
            Rectangle view,
            Position externalTopLeft, Position externalTopRight,
            Position externalBottomLeft, Position externalBottomRight
    ) {
        this(
                view.topLeft(), view.topRight(),
                view.bottomLeft(), view.bottomRight(),
                externalTopLeft, externalTopRight,
                externalBottomLeft, externalBottomRight
        );
    }

    public Viewport(
            Position viewTopLeft, Position viewTopRight,
            Position viewBottomLeft, Position viewBottomRight,
            Position externalTopLeft, Position externalTopRight,
            Position externalBottomLeft, Position externalBottomRight
    ) {
        this.viewTopLeft = viewTopLeft;
        this.viewTopRight = viewTopRight;
        this.viewBottomLeft = viewBottomLeft;
        this.viewBottomRight = viewBottomRight;
        this.externalTopLeft = externalTopLeft;
        this.externalTopRight = externalTopRight;
        this.externalBottomLeft = externalBottomLeft;
        this.externalBottomRight = externalBottomRight;
    }

    public Position convertViewToExternal(Position viewPosition) {
        // Scale the view pixel coordinates to be in unit coordinates (0-1).
        Position unit = scaleToUnit(viewPosition,
                viewTopLeft, viewTopRight,
                viewBottomLeft, viewBottomRight
        );

        // Scale the unit coordinates (0-1) to be in external coordinates.
        Position external = scaleFromUnit(unit,
                externalTopLeft, externalTopRight,
                externalBottomLeft, externalBottomRight
        );

        return external;
    }

    public Position convertExternalToView(Position externalPosition) {
        // Scale the external coordinates to be in unit coordinates (0-1).
        Position unit = scaleToUnit(externalPosition,
                externalTopLeft, externalTopRight,
                externalBottomLeft, externalBottomRight
        );

        // Scale the unit coordinates (0-1) to be in view coordinates.
        Position view = scaleFromUnit(unit,
                viewTopLeft, viewTopRight,
                viewBottomLeft, viewBottomRight
        );

        return view;
    }

    private Position scaleToUnit(
            Position position,
            Position topLeft, Position topRight,
            Position bottomLeft, Position bottomRight
    ) {
        Line left = new Line(topLeft, bottomLeft);
        Line right = new Line(topRight, bottomRight);
        Line top = new Line(topLeft, topRight);
        Line bottom = new Line(bottomLeft, bottomRight);

        Position leftMiddle = left.withY(position.getY());
        Position rightMiddle = right.withY(position.getY());
        double x = fractionBetween(position, leftMiddle, rightMiddle);

        Position topMiddle = top.withX(position.getX());
        Position bottomMiddle = bottom.withX(position.getX());
        double y = fractionBetween(position, bottomMiddle, topMiddle);

        return new Position(x, y);
    }

    private Position scaleFromUnit(
            Position position,
            Position topLeft, Position topRight,
            Position bottomLeft, Position bottomRight
    ) {
        // Bilinear interpolation, first find the position on the left and right sides in the y direction.
        Position left = between(bottomLeft, topLeft, position.getY());
        Position right = between(bottomRight, topRight, position.getY());

        // Now find the point in the middle in the x direction.
        Position middle = between(left, right, position.getX());
        return middle;
    }

    private Position between(Position first, Position second, double fraction) {
        // Calculate the position between two points
        Vector2 difference = second.minus(first).multiply(fraction);
        return first.add(difference);
    }

    private double fractionBetween(Position position, Position first, Position second) {
        // Calculate the fractional position between three colinear points
        Line line = new Line(first, second);
        if (Math.abs(line.getAngleToX()) < 45) {
            return ScalingUtil.scaleLinear(position.getX(), first.getX(), second.getX(), 0, 1.0);
        } else {
            return ScalingUtil.scaleLinear(position.getY(), first.getY(), second.getY(), 0, 1.0);
        }
    }

}
