package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.geometry.Line;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

public class HoughUtil {

    /**
     * Uses the hough transform to detect lines in the given image.  Automatically groups lines that are very
     * similar into a single line.
     *
     * @param image the image in which to detect lines
     * @return the list of lines that were detected in the image.
     */
    public static List<HoughLine> detectLines(
            Mat image,
            double similarLineRhoThreshold,
            double similarLineThetaThreshold
    ) {

        /*
        From TileEdgeDetector:
        todo: convert this to util
        List<TileEdgeDetector.Line> lines = detectLines(input, output);
        for (TileEdgeDetector.Line line : lines) {
            //drawOutputLine(output, line, Color.BLUE.toRGBA());
        }

        // The near and far edges of the gap between tiles are about 0.8 inches apart.  We set the threshold
        // slightly higher than this so that If we detect both of these lines we will group them together.  Their
        // weighted average will fall in the middle.
        double rhoThreshold = 0.9 * VERTICAL_PIXELS_PER_INCH;

        // Group lines that are very similar together
        lines = groupSimilarLines(lines, 2, rhoThreshold);
        */

        // todo: detect lines and group similar lines within a threshold
        return new ArrayList<>();
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

            double length = Math.max(resolution.width, resolution.height);
            double pt1y = Math.round(y0 + length * (a));
            double pt1x = Math.round(x0 + length * (-b));
            double pt2x = Math.round(x0 - length * (-b));
            double pt2y = Math.round(y0 - length * (a));

            Position pt1 = new Position(pt1x, pt1y);
            Position pt2 = new Position(pt2x, pt2y);

            // todo: clip the line to the size of the viewport, if needed

            return new Line(pt1, pt2);
        }
    }

}
