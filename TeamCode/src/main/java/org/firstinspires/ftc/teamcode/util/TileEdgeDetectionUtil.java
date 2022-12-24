package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotDescriptor;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class TileEdgeDetectionUtil {

    /**
     * Detects tile edges in the given image, and derives an observation about the position of the robot within
     * the tile.
     *
     * @param descriptor the description of the robot's physical characteristics
     * @param image the image of the tiles to use for detection
     *
     * @return the observation of the robot's position.
     */
    public static TileEdgeObservation detectTileEdges(
            RobotDescriptor descriptor,
            Mat image
    ) {
        List<Line> lines = detectLines(image, 0.0, 0.0);


        // todo: figure out what the lines mean, based on geometry
        return null;
    }

    /**
     * Uses the hough transform to detect lines in the given image.  Automatically groups lines that are very
     * similar into a single line.
     *
     * @param image the image in which to detect lines
     *
     * @return the list of lines that were detected in the image.
     */
    public static List<Line> detectLines(
            Mat image,
            double similarLineRhoThreshold,
            double similarLineThetaThreshold
    ) {
        // todo: detect lines and group similar lines within a threshold
        return new ArrayList<>();
    }

    public static class TileEdgeObservation {
        public Double distanceFront;
        public Double distanceRight;
        public Double headingOffset;
        public ElapsedTime observationTime;

        public TileEdgeObservation(Double distanceFront, Double distanceRight, Double headingOffset) {
            this.distanceFront = distanceFront;
            this.distanceRight = distanceRight;
            this.headingOffset = headingOffset;
            this.observationTime = new ElapsedTime();
        }
    }

    public static class Line {
        public double rho;
        public double theta;
        public int count;

        public Line(double rho, double theta) {
            this.rho = rho;
            this.theta = theta;
            this.count = 1;
        }
    }

}
