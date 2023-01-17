package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class HoughLineDetector {

    private Mat gray = new Mat(); //image grayscaled
    private Mat edges = new Mat(); //image with edges
    private Mat edgesMasked = new Mat(); // edges image with mask applied

    private HoughParameters parameters;

    public HoughLineDetector(HoughParameters parameters) {
        this.parameters = parameters;
    }

    public HoughParameters getParameters() {
        return parameters;
    }

    /**
     * Uses the hough transform to detect lines in the given image.  Automatically groups lines that are very
     * similar into a single line.
     *
     * @param image the image in which to detect lines
     * @return the list of lines that were detected in the image.
     */
    public List<HoughLine> detectLines(Mat image) {
        // detects the lines
        List<HoughLine> lines = identifyLines(image);

        // Group lines that are very similar together
        return groupSimilarLines(lines);
    }

    private List<HoughLine> identifyLines(Mat input) {

        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(gray, edges, 60, 60 * 3, 3, false);

        Mat masked = applyMask(edges, parameters.imageMask, edgesMasked);

        Mat houghLines = new Mat();
        Imgproc.HoughLines(
                masked,
                houghLines,
                parameters.rhoResolution,
                parameters.thetaResolution,
                parameters.pixelVoterThreshold,
                0,
                0,
                Math.toRadians(parameters.minTheta),
                Math.toRadians(parameters.maxTheta)
        );

        List<HoughLine> lines = new ArrayList<>();
        for (int i = 0; i < houghLines.rows(); i++) {
            double[] data = houghLines.get(i, 0);
            double rho = data[0];
            double theta = Math.toDegrees(data[1]);
            lines.add(new HoughLine(rho, theta));
        }

        return lines;
    }

    private Mat applyMask(Mat image, Mat mask, Mat dst) {
        if (mask != null) {
            Core.bitwise_and(image, mask, dst);
            return dst;
        } else {
            return image;
        }
    }

    private List<HoughLine> groupSimilarLines(List<HoughLine> lines) {
        List<HoughLine> result = new ArrayList<>();

        for (HoughLine line : lines) {
            // First, check if we have an existing line that is similar to this one (within the threshold).
            HoughLine similarLine = findSimilarLine(line, result);
            if (similarLine == null) {
                // No lines similar to this one have been found, so add it to the result
                result.add(line);
            } else {
                // Do a weighted Average with the existing line
                similarLine.count++;
                similarLine.theta = ((similarLine.theta * (similarLine.count - 1)) + line.theta) / similarLine.count;
                similarLine.rho = ((similarLine.rho * (similarLine.count - 1)) + line.rho) / similarLine.count;
            }
        }

        return result;
    }

    private HoughLine findSimilarLine(HoughLine line, List<HoughLine> linesToSearch) {

        for (HoughLine candidate : linesToSearch) {
            if (Math.abs(candidate.theta - line.theta) < parameters.similarLineThetaThreshold &&
                    Math.abs(candidate.rho - line.rho) < parameters.similarLineRhoThreshold)
            {
                return candidate;
            }
        }
        return null;
    }

    public static class HoughParameters {
        public double similarLineRhoThreshold = 2.0;
        public double similarLineThetaThreshold = 2.0;
        public int pixelVoterThreshold = 125;  // number of pixels that must correspond to a line
        public double rhoResolution = 1;  // 1 pixel
        public double thetaResolution = Math.toRadians(1); // 1 degree
        public double minTheta = 0;
        public double maxTheta = 180;
        public Mat imageMask;
    }

    public static class HoughLine {
        public double rho;
        public double theta;
        public int count;

        public HoughLine(double rho, double theta) {
            this.rho = rho;
            this.theta = theta;
            this.count = 1;
        }

        /**
         * Convert to a line in terms of two points, in the (x,y) coordinates of the image.
         *
         * @param resolution the resolution of the image
         */
        public Line toLine(Size resolution) {
            double theta = Math.toRadians(this.theta);
            double rho = this.rho;

            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a * rho;
            double y0 = b * rho;

            double length = Math.max(resolution.width, resolution.height) * 2;
            double pt1y = Math.round(y0 + length * (a));
            double pt1x = Math.round(x0 + length * (-b));
            double pt2x = Math.round(x0 - length * (-b));
            double pt2y = Math.round(y0 - length * (a));

            Position pt1 = new Position(pt1x, pt1y);
            Position pt2 = new Position(pt2x, pt2y);

            Line line = new Line(pt1, pt2);

            line = new Rectangle(resolution.height, resolution.width, 0, 0).clip(line);
            line = line.normalize();

            return line;
        }
    }

}
