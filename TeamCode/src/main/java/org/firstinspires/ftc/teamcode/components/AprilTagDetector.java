package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.teamcode.util.Color.BLUE;
import static org.firstinspires.ftc.teamcode.util.Color.GREEN;
import static org.firstinspires.ftc.teamcode.util.Color.RED;
import static org.firstinspires.ftc.teamcode.util.Color.WHITE;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectorJNI;

import java.util.ArrayList;
import java.util.List;

public class AprilTagDetector extends BaseComponent {

    /**
     * The webcam to use for detection
     */
    private WebCam webCam;

    private FrameProcessor frameProcessor;

    private AprilTagDetectionParameters parameters = new AprilTagDetectionParameters();

    private List<AprilTagDetection> detections = new ArrayList<>();

    public AprilTagDetector(RobotContext context, WebCam webCam) {
        super(context);
        this.webCam = webCam;
    }

    /**
     * Sets the detection parameters.  This should be called prior to calling activate.
     */
    public void setDetectionParameters(AprilTagDetectionParameters parameters) {
        if (isActive()) {
            throw new IllegalStateException();
        }
        this.parameters = parameters;
    }

    public void activate() {
        frameProcessor = new FrameProcessor(parameters);
        webCam.setFrameProcessor(frameProcessor);
    }

    public boolean isActive() {
        return frameProcessor != null;
    }

    public void deactivate() {
        webCam.setFrameProcessor(null);
        frameProcessor = null;
    }

    public List<AprilTagDetection> getDetections() {
        return detections;
    }

    public AprilTagDetection waitForDetection(double seconds) {
        ElapsedTime begin = new ElapsedTime();
        while (begin.seconds() < seconds) {
            List<AprilTagDetection> detections = this.detections;
            if (!detections.isEmpty()) {
                return detections.get(0);
            }
        }
        return null;
    }

    /**
     * A FrameProcessor that detects April tags.
     * <p>
     * Adapted with permission from the sample code developed by the OpenFTC Team, 2021.
     */
    public class FrameProcessor implements WebCam.FrameProcessor {

        private long nativeApriltagPtr;
        private Mat gray = new Mat();

        Mat cameraMatrix;

        private double fx;
        private double fy;
        private double cx;
        private double cy;

        // UNITS ARE METERS
        private double tagsize;
        private double tagsizeX;
        private double tagsizeY;

        public FrameProcessor(AprilTagDetectionParameters parameters) {
            this.tagsize = parameters.tagsize;
            this.tagsizeX = parameters.tagsize;
            this.tagsizeY = parameters.tagsize;
            this.fx = parameters.fx;
            this.fy = parameters.fy;
            this.cx = parameters.cx;
            this.cy = parameters.cy;

            constructMatrix();

            // Allocate a native context object. See the corresponding deletion in the finalizer
            nativeApriltagPtr = AprilTagDetectorJNI.createApriltagDetector(AprilTagDetectorJNI.TagFamily.TAG_36h11.string, 3, 3);
            AprilTagDetectorJNI.setApriltagDetectorDecimation(nativeApriltagPtr, parameters.decimation);
        }

        @Override
        protected void finalize() {
            // Might be null if createApriltagDetector() threw an exception
            if (nativeApriltagPtr != 0) {
                // Delete the native context we created in the constructor
                AprilTagDetectorJNI.releaseApriltagDetector(nativeApriltagPtr);
                nativeApriltagPtr = 0;
            } else {
                System.out.println("AprilTagDetectionPipeline.finalize(): nativeApriltagPtr was NULL");
            }
        }

        @Override
        public void processFrame(Mat input, Mat output) {

            // Convert to greyscale
            Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGBA2GRAY);

            // Run AprilTag
            List<AprilTagDetection> detections = AprilTagDetectorJNI.runAprilTagDetectorSimple(
                    nativeApriltagPtr, gray,
                    tagsize,
                    fx, fy, cx, cy
            );

            // Atomic update, so no need for synchronization
            AprilTagDetector.this.detections = detections;

            // For fun, use OpenCV to draw 6DOF markers on the image. We actually recompute the pose using
            // OpenCV because I haven't yet figured out how to re-use AprilTag's pose in OpenCV.
            for (AprilTagDetection detection : detections) {
                Pose pose = poseFromTrapezoid(detection.corners, cameraMatrix, tagsizeX, tagsizeY);
                drawAxisMarker(output, tagsizeY / 2.0, 6, pose.rvec, pose.tvec, cameraMatrix);
                draw3dCubeMarker(output, tagsizeX, tagsizeX, tagsizeY, 5, pose.rvec, pose.tvec, cameraMatrix);
            }
        }

        private void constructMatrix() {
            //     Construct the camera matrix.
            //
            //      --         --
            //     | fx   0   cx |
            //     | 0    fy  cy |
            //     | 0    0   1  |
            //      --         --
            //

            cameraMatrix = new Mat(3, 3, CvType.CV_32FC1);

            cameraMatrix.put(0, 0, fx);
            cameraMatrix.put(0, 1, 0);
            cameraMatrix.put(0, 2, cx);

            cameraMatrix.put(1, 0, 0);
            cameraMatrix.put(1, 1, fy);
            cameraMatrix.put(1, 2, cy);

            cameraMatrix.put(2, 0, 0);
            cameraMatrix.put(2, 1, 0);
            cameraMatrix.put(2, 2, 1);
        }

        /**
         * Draw a 3D axis marker on a detection. (Similar to what Vuforia does)
         *
         * @param buf          the RGB buffer on which to draw the marker
         * @param length       the length of each of the marker 'poles'
         * @param rvec         the rotation vector of the detection
         * @param tvec         the translation vector of the detection
         * @param cameraMatrix the camera matrix used when finding the detection
         */
        private void drawAxisMarker(Mat buf, double length, int thickness, Mat rvec, Mat tvec, Mat cameraMatrix) {
            // The points in 3D space we wish to project onto the 2D image plane.
            // The origin of the coordinate space is assumed to be in the center of the detection.
            MatOfPoint3f axis = new MatOfPoint3f(
                    new Point3(0, 0, 0),
                    new Point3(length, 0, 0),
                    new Point3(0, length, 0),
                    new Point3(0, 0, -length)
            );

            // Project those points
            MatOfPoint2f matProjectedPoints = new MatOfPoint2f();
            Calib3d.projectPoints(axis, rvec, tvec, cameraMatrix, new MatOfDouble(), matProjectedPoints);
            Point[] projectedPoints = matProjectedPoints.toArray();

            // Draw the marker!
            Imgproc.line(buf, projectedPoints[0], projectedPoints[1], RED.toRGBA(), thickness);
            Imgproc.line(buf, projectedPoints[0], projectedPoints[2], GREEN.toRGBA(), thickness);
            Imgproc.line(buf, projectedPoints[0], projectedPoints[3], BLUE.toRGBA(), thickness);

            Imgproc.circle(buf, projectedPoints[0], thickness, WHITE.toRGBA(), -1);
        }

        private void draw3dCubeMarker(
                Mat buf,
                double length,
                double tagWidth, double tagHeight,
                int thickness,
                Mat rvec, Mat tvec, Mat cameraMatrix
        ) {
            //axis = np.float32([[0,0,0], [0,3,0], [3,3,0], [3,0,0],
            //       [0,0,-3],[0,3,-3],[3,3,-3],[3,0,-3] ])

            // The points in 3D space we wish to project onto the 2D image plane.
            // The origin of the coordinate space is assumed to be in the center of the detection.
            MatOfPoint3f axis = new MatOfPoint3f(
                    new Point3(-tagWidth / 2, tagHeight / 2, 0),
                    new Point3(tagWidth / 2, tagHeight / 2, 0),
                    new Point3(tagWidth / 2, -tagHeight / 2, 0),
                    new Point3(-tagWidth / 2, -tagHeight / 2, 0),
                    new Point3(-tagWidth / 2, tagHeight / 2, -length),
                    new Point3(tagWidth / 2, tagHeight / 2, -length),
                    new Point3(tagWidth / 2, -tagHeight / 2, -length),
                    new Point3(-tagWidth / 2, -tagHeight / 2, -length));

            // Project those points
            MatOfPoint2f matProjectedPoints = new MatOfPoint2f();
            Calib3d.projectPoints(axis, rvec, tvec, cameraMatrix, new MatOfDouble(), matProjectedPoints);
            Point[] projectedPoints = matProjectedPoints.toArray();

            // Pillars
            for (int i = 0; i < 4; i++) {
                Imgproc.line(buf, projectedPoints[i], projectedPoints[i + 4], BLUE.toRGBA(), thickness);
            }

            // Base lines
            //Imgproc.line(buf, projectedPoints[0], projectedPoints[1], blue, thickness);
            //Imgproc.line(buf, projectedPoints[1], projectedPoints[2], blue, thickness);
            //Imgproc.line(buf, projectedPoints[2], projectedPoints[3], blue, thickness);
            //Imgproc.line(buf, projectedPoints[3], projectedPoints[0], blue, thickness);

            // Top lines
            Imgproc.line(buf, projectedPoints[4], projectedPoints[5], GREEN.toRGBA(), thickness);
            Imgproc.line(buf, projectedPoints[5], projectedPoints[6], GREEN.toRGBA(), thickness);
            Imgproc.line(buf, projectedPoints[6], projectedPoints[7], GREEN.toRGBA(), thickness);
            Imgproc.line(buf, projectedPoints[4], projectedPoints[7], GREEN.toRGBA(), thickness);
        }

        /**
         * Extracts 6DOF pose from a trapezoid, using a camera intrinsics matrix and the
         * original size of the tag.
         *
         * @param points       the points which form the trapezoid
         * @param cameraMatrix the camera intrinsics matrix
         * @param tagsizeX     the original width of the tag
         * @param tagsizeY     the original height of the tag
         * @return the 6DOF pose of the camera relative to the tag
         */
        Pose poseFromTrapezoid(Point[] points, Mat cameraMatrix, double tagsizeX, double tagsizeY) {
            // The actual 2d points of the tag detected in the image
            MatOfPoint2f points2d = new MatOfPoint2f(points);

            // The 3d points of the tag in an 'ideal projection'
            Point3[] arrayPoints3d = new Point3[4];
            arrayPoints3d[0] = new Point3(-tagsizeX / 2, tagsizeY / 2, 0);
            arrayPoints3d[1] = new Point3(tagsizeX / 2, tagsizeY / 2, 0);
            arrayPoints3d[2] = new Point3(tagsizeX / 2, -tagsizeY / 2, 0);
            arrayPoints3d[3] = new Point3(-tagsizeX / 2, -tagsizeY / 2, 0);
            MatOfPoint3f points3d = new MatOfPoint3f(arrayPoints3d);

            // Using this information, actually solve for pose
            Pose pose = new Pose();
            Calib3d.solvePnP(points3d, points2d, cameraMatrix, new MatOfDouble(), pose.rvec, pose.tvec, false);

            return pose;
        }

    }

    /*
     * A simple container to hold both rotation and translation
     * vectors, which together form a 6DOF pose.
     */
    private static class Pose {
        Mat rvec;
        Mat tvec;

        public Pose() {
            rvec = new Mat();
            tvec = new Mat();
        }
    }

    public static class AprilTagDetectionParameters {
        // UNITS ARE METERS
        public double tagsize = 0.166;

        // Lens intrinsics
        // UNITS ARE PIXELS
        // NOTE: this calibration is for the C920 webcam at 800x448.
        // You will need to do your own calibration for other configurations!
        public double fx = 578.272;
        public double fy = 578.272;
        public double cx = 402.145;
        public double cy = 221.506;

        public float decimation = 3.0f;
    }

}
