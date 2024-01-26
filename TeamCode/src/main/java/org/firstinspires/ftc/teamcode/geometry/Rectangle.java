package org.firstinspires.ftc.teamcode.geometry;

import org.opencv.core.Mat;

public class Rectangle extends Mat {

    private double top, right, bottom, left;

    public Rectangle(double width, double height) {
        this(height, width, 0, 0);
    }

    public Rectangle(Position position, double width, double height) {
        this(
                position.getY(),
                position.getX() + width,
                position.getY() + height,
                position.getX()
        );
    }

    public Rectangle(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public double getRight() {
        return right;
    }

    public double getBottom() {
        return bottom;
    }

    public double getLeft() {
        return left;
    }

    public Position topLeft() {
        return new Position(left, top);
    }

    public Position topRight() {
        return new Position(right, top);
    }

    public Position bottomLeft() {
        return new Position(left, bottom);
    }

    public Position bottomRight() {
        return new Position(right, bottom);
    }

    /**
     * Returns true if this rectangle contains the given position, false otherwise.
     */
    public boolean contains(Position point) {
        return point.getX() > left && point.getX() < right &&
                point.getY() > bottom && point.getY() < top;
    }

    public double getWidth() {
        return right - left;
    }

    public double getHeight() {
        return bottom - top;
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    /**
     * Clips a line to the bounds of this rectangle and returns the clipped version.
     */
    public Line clip(Line line) {
        // Use the Cohen-Sutherland algorithm for clipping lines.
        // https://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm

        // compute outcodes for P0, P1, and whatever point lies outside the clip rectangle
        double x1 = line.getP1().getX(), y1 = line.getP1().getY();
        double x2 = line.getP2().getX(), y2 = line.getP2().getY();
        int outCode1 = computeOutCodes(x1, y1);
        int outCode2 = computeOutCodes(x2, y2);

        while (true) {
            if ((outCode1 | outCode2) == 0) {
                // bitwise OR is 0: both points inside window; trivially accept and exit loop
                return new Line(new Position(x1, y1), new Position(x2, y2));

            } else if ((outCode1 & outCode2) != 0) {
                // bitwise AND is not 0: both points share an outside zone (LEFT, RIGHT, TOP,
                // or BOTTOM), so both must be outside window; exit loop (accept is false)
                return null;

            } else {
                // failed both tests, so calculate the line segment to clip
                // from an outside point to an intersection with clip edge
                double x, y;

                // At least one endpoint is outside the clip rectangle; pick it.
                int outCodeOut = Math.max(outCode1, outCode2);

                // Now find the intersection point;
                // use formulas:
                //   slope = (y1 - y0) / (x1 - x0)
                //   x = x0 + (1 / slope) * (ym - y0), where ym is ymin or ymax
                //   y = y0 + slope * (xm - x0), where xm is xmin or xmax
                // No need to worry about divide-by-zero because, in each case, the
                // outcode bit being tested guarantees the denominator is non-zero
                if ((outCodeOut & OutCode.TOP.value) != 0) {           // point is above the clip window
                    x = x1 + (x2 - x1) * (top - y1) / (y2 - y1);
                    y = top;
                } else if ((outCodeOut & OutCode.BOTTOM.value) != 0) { // point is below the clip window
                    x = x1 + (x2 - x1) * (bottom - y1) / (y2 - y1);
                    y = bottom;
                } else if ((outCodeOut & OutCode.RIGHT.value) != 0) {  // point is to the right of clip window
                    y = y1 + (y2 - y1) * (right - x1) / (x2 - x1);
                    x = right;
                } else if ((outCodeOut & OutCode.LEFT.value) != 0) {   // point is to the left of clip window
                    y = y1 + (y2 - y1) * (left - x1) / (x2 - x1);
                    x = left;
                } else {
                    throw new IllegalStateException();
                }

                // Now we move outside point to intersection point to clip
                // and get ready for next pass.
                if (outCodeOut == outCode1) {
                    x1 = x;
                    y1 = y;
                    outCode1 = computeOutCodes(x1, y1);
                } else {
                    x2 = x;
                    y2 = y;
                    outCode2 = computeOutCodes(x2, y2);
                }
            }
        }
    }

    private int computeOutCodes(double x, double y) {
        int outcode = 0;
        if (x < left) {
            outcode |= OutCode.LEFT.value;
        } else if (x > right) {
            outcode |= OutCode.RIGHT.value;
        }
        if (y < bottom) {
            outcode |= OutCode.BOTTOM.value;
        } else if (y > top) {
            outcode |= OutCode.TOP.value;
        }
        return outcode;
    }

    private enum OutCode {
        LEFT,
        RIGHT,
        BOTTOM,
        TOP;

        private final int value = 1 << this.ordinal();
    }

}
