package org.firstinspires.ftc.teamcode.components;


import org.firstinspires.ftc.teamcode.components.WebCam.FrameContext;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.Color;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PoleDetector extends BaseComponent{

    /**
     * The webcam to use for detection.
     */
    private WebCam webCam;

    private FrameProcessor frameProcessor;

    /**
     * The parameters to use when doing pole detection.
     */
    private PoleDetectionParameters parameters;

    /**
     * The most recent pole detection.
     */
    private Detection detection;

    public PoleDetector(RobotContext context, WebCam webCam) {
        super(context);
        this.webCam = webCam;
        this.parameters = new PoleDetectionParameters();
    }

    public void activate() {
        frameProcessor = new FrameProcessor();
        webCam.setFrameProcessor(frameProcessor);
    }

    public boolean isActive() {
        return frameProcessor != null;
    }

    public void deactivate() {
        webCam.removeFrameProcessor();
        frameProcessor = null;
    }

    public static class PoleDetectionParameters {

        /**
         * The color of the pole in RGBA format.
         */
        public Scalar poleColor = Color.BLUE.toRGBA();

        /**
         * The color detection threshold from (0-1).
         */
        public double colorThreshold = 0.1;

    }

    public static class Detection {

        /**
         * The distance from the front edge of the robot to the pole.
         * Measured in tiles, with the x component being lateral offset, and y being forward offset.
         */
        public Vector2 distance;

        public Detection(Vector2 distance) {
            this.distance = distance;
        }

    }

    private class FrameProcessor implements WebCam.FrameProcessor {

        private Mat mask = new Mat();

        @Override
        public void processFrame(Mat input, Mat output, FrameContext frameContext) {
            // todo: process image set the `detection` variable if we see anything

            Scalar colorDiff = Scalar.all(255).mul(Scalar.all(parameters.colorThreshold));

            Scalar lowerBound = add(parameters.poleColor, negate(colorDiff));
            Scalar upperBound = add(parameters.poleColor, colorDiff);

            // Create a mask with all the pixels that are within the color range.
            Core.inRange(input, lowerBound, upperBound, mask);

            // todo: find the individual poles  (hough?)  (or contours?)
            // https://techvidvan.com/tutorials/detect-objects-of-similar-color-using-opencv-in-python/
        }
    }

    private static Scalar negate(Scalar a) {
        return a.mul(Scalar.all(-1.0));
    }

    private static Scalar add(Scalar a, Scalar b) {
        double[] result = new double[a.val.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = a.val[i] + b.val[i];
        }
        return new Scalar(result);
    }

}