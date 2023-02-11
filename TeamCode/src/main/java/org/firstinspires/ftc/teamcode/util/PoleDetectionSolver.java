package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.RobotDescriptor.EmpiricalPoleDetection;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.opencv.core.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PoleDetectionSolver {

    private RobotDescriptor robotDescriptor;
    private Parameters parameters;

    public PoleDetectionSolver(RobotDescriptor robotDescriptor, Parameters parameters) {
        this.robotDescriptor = robotDescriptor;
        this.parameters = parameters;
    }

    public PoleDetection solve(List<PoleContour> poleContours) {

        // Select the correct contour by picking the valid contour with the largest area.
        PoleContour contour = pickBestContour(poleContours);

        if (contour != null) {
            // Get the detections and scale the values between detections
            double width = contour.boundingRect.getWidth();

            List<EmpiricalPoleDetection> detections = robotDescriptor.empiricalPoleDetections;

            // Find the two closest empirical measurements for this motor power.
            Collections.sort(detections, new Comparator<RobotDescriptor.EmpiricalPoleDetection>() {
                @Override
                public int compare(RobotDescriptor.EmpiricalPoleDetection first, RobotDescriptor.EmpiricalPoleDetection second) {
                    return Double.compare(
                            Math.abs(first.averageWidth - width),
                            Math.abs(second.averageWidth - width)
                    );
                }
            });

            EmpiricalPoleDetection low = detections.get(0);
            EmpiricalPoleDetection high = detections.get(1);
            if (low.averageWidth > high.averageWidth) {
                EmpiricalPoleDetection swap = low;
                low = high;
                high = swap;
            }

            double distanceY = ScalingUtil.scaleLinear(
                    width,
                    low.averageWidth, high.averageWidth,
                    low.distance, high.distance
            );

            // todo: once we have the y coordinate, calculate the x coordinate by either using bilinear interpolation
            // todo: from multiple x coordinates at each y, or by solving the geometry equations directly.
            double distanceX = 0;

            return new PoleDetection(new Vector2(distanceX, distanceY), contour);

        } else {
            // There was no valid pole detection to solve.
            return null;
        }
    }

    public PoleContour pickBestContour(List<PoleContour> poleContours) {
        // Sort the contours, descending by area, so we can find the largest contour.
        Collections.sort(poleContours, new Comparator<PoleContour>() {
            @Override
            public int compare(PoleContour a, PoleContour b) {
                return Double.compare(b.area, a.area);
            }
        });

        // Now pick the first one that is valid.
        for (PoleContour contour : poleContours) {
            if (isValidDetection(contour)) {
                return contour;
            }
        }

        // No valid contours were found.
        return null;
    }

    public boolean isValidDetection(PoleContour contour) {

        // A contour is valid if it has a certain fill percentage.
        double fill = contour.area / contour.boundingRect.getArea();
        if (fill < parameters.fillThreshold) {
            return false;
        }

        // Check if the rectangle does not extend to the majority of the height of the screen.  Otherwise, it's
        // probably not a pole, or it's too far away.
        double heightFraction = contour.boundingRect.getHeight() / parameters.resolution.height;
        if (heightFraction < parameters.heightThreshold) {
            return false;
        }

        // Check if the rectangle includes the left or right side of the screen.  If so, it's not valid.
        if (contour.boundingRect.getLeft() == 0 || ((int) contour.boundingRect.getRight() == (int) parameters.resolution.width)) {
            return false;
        }

        // The contour appears to be a valid pole.
        return true;
    }

    public static class PoleContour {

        /**
         * The id of the contour according to the image detection source (e.g. OpenCv).
         */
        public int id;

        /**
         * The bounding rectangle of the contour.
         */
        public Rectangle boundingRect;

        /**
         * The area of the contour.
         */
        public double area;

        /**
         * The centroid of the contour.
         */
        public Position centroid;

        /**
         * The average width of the contour;
         */
        public double averageWidth;

        public PoleContour(int id, Rectangle boundingRect, double area, Position centroid, double averageWidth) {
            this.id = id;
            this.boundingRect = boundingRect;
            this.area = area;
            this.centroid = centroid;
            this.averageWidth = averageWidth;
        }
    }

    public static class PoleDetection {

        /**
         * The distance from the front edge of the robot to the pole.
         * Measured in tiles, with the x component being lateral offset, and y being forward offset.
         */
        public Vector2 distance;

        /**
         * The contour that was used to determine the pole distance.
         */
        public PoleContour observedContour;

        /**
         * The time at which this observation was made.
         */
        public ElapsedTime observationTime;

        public PoleDetection(Vector2 distance, PoleContour observedContour) {
            this.distance = distance;
            this.observedContour = observedContour;
        }
    }

    public static class Parameters {

        /**
         * The resolution of the webcam image.
         */
        public Size resolution;

        /**
         * The fraction of the image's height that a pole must fill for it to be considered valid.
         */
        public double heightThreshold = 0.5;

        /**
         * The fraction of a contour's bounding rect that must be filled in for it to be considered valid.
         */
        public double fillThreshold = 0.8;

        public Parameters(Size resolution) {
            this.resolution = resolution;
        }
    }

}
