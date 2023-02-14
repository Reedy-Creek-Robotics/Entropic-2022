package org.firstinspires.ftc.teamcode.geometry;

import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.firstinspires.ftc.teamcode.RobotDescriptor.EmpiricalPoleDetection;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PoleDistanceSolver {

    private RobotDescriptor robotDescriptor;
    private Parameters parameters;

    public PoleDistanceSolver(RobotDescriptor robotDescriptor, Parameters parameters) {
        this.robotDescriptor = robotDescriptor;
        this.parameters = parameters;
    }

    public PoleDetection solve(List<PoleContour> poleContours) {

        // Select the correct contour by picking the valid contour with the largest area.
        PoleContour contour = pickBestContour(poleContours);

        if (contour != null) {
            // Get the detections and scale the values between detections
            double width = contour.boundingRect.getWidth();

            List<EmpiricalPoleDetection> detections = new ArrayList<>(robotDescriptor.empiricalPoleDetections);

            // Find the closest empirical measurement for this perceived pole width (in pixels).
            Collections.sort(detections, new Comparator<RobotDescriptor.EmpiricalPoleDetection>() {
                @Override
                public int compare(RobotDescriptor.EmpiricalPoleDetection first, RobotDescriptor.EmpiricalPoleDetection second) {
                    return Double.compare(
                            Math.abs(first.averageWidth - width),
                            Math.abs(second.averageWidth - width)
                    );
                }
            });

            EmpiricalPoleDetection bestMatch = detections.get(0);
            double bestMatchDistanceToCamera = bestMatch.distance - robotDescriptor.poleDetectionCameraOffset.getY();

            // Now that we have the best empirical match, calculate the focal length of the camera.
            // https://pyimagesearch.com/2015/01/19/find-distance-camera-objectmarker-using-python-opencv/

            // The formula for focal length is:
            //   Focal-Length = (Perceived-Width * Distance) / Object-Width

            double poleWidth = 1.0; // inches
            double focalLength = (bestMatch.averageWidth * bestMatchDistanceToCamera) / poleWidth;

            // Now that we have the calculated focal length from the empirical measurement, we can find the distance
            // to the pole in the current image by solving for Distance in the equation above.
            //   Distance = (Focal-Length * Object-Width) / Perceived-Width
            double distanceY = (focalLength * poleWidth) / contour.averageWidth;

            // Now use trigonometry to determine the distanceX from the camera middle.
            double imageWidth = parameters.resolution.width;
            double imageCenter = imageWidth / 2;
            double imageX = contour.centroid.getX();
            double theta = (imageCenter - imageX) / imageWidth * parameters.fieldOfViewDegrees;
            double distanceX = distanceY * Math.tan(Math.toRadians(theta));

            Vector2 distance = new Vector2(distanceX, distanceY);
            Vector2 distanceRelativeToFrontCenter = distance.add(robotDescriptor.poleDetectionCameraOffset);

            // Convert the distance to tiles.
            Vector2 distanceInTiles = inchesToTiles(distanceRelativeToFrontCenter);

            return new PoleDetection(distanceInTiles, contour);

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
         * The field of view of the webcam in degrees.
         */
        public double fieldOfViewDegrees;

        /**
         * The fraction of the image's height that a pole must fill for it to be considered valid.
         */
        public double heightThreshold = 0.3;

        /**
         * The fraction of a contour's bounding rect that must be filled in for it to be considered valid.
         */
        //public double fillThreshold = 0.7;
        public double fillThreshold = 0.7;


        public Parameters(Size resolution, double fieldOfViewDegrees) {
            this.resolution = resolution;
            this.fieldOfViewDegrees = fieldOfViewDegrees;
        }
    }

}
