package org.firstinspires.ftc.teamcode.components;


import static org.firstinspires.ftc.teamcode.util.FormatUtil.format;

import org.firstinspires.ftc.teamcode.components.WebCam.FrameContext;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.firstinspires.ftc.teamcode.util.Color;
import org.firstinspires.ftc.teamcode.util.DrawUtil;
import org.firstinspires.ftc.teamcode.util.FormatUtil;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

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
        private Mat threshold = new Mat();

        private Mat morphed = new Mat();
        private Mat temp = new Mat();
        private Mat kernel = Mat.ones(7, 7, CvType.CV_8U);

        Mat hierarchy = new Mat();

        private Mat contoured = new Mat();
        private Mat bounded = new Mat();




        @Override
        public void processFrame(Mat input, Mat output, FrameContext frameContext) {
            // todo: change all the mats to the right stuff. Set detection somewhere.
            // https://techvidvan.com/tutorials/detect-objects-of-similar-color-using-opencv-in-python/

            Scalar colorDiff = Scalar.all(255).mul(Scalar.all(parameters.colorThreshold));

            Scalar lowerBound = add(parameters.poleColor, negate(colorDiff));
            Scalar upperBound = add(parameters.poleColor, colorDiff);


            Core.inRange(input, lowerBound, upperBound, threshold);

            Imgproc.morphologyEx(threshold, temp, Imgproc.MORPH_CLOSE, kernel);
            Imgproc.morphologyEx(temp, morphed, Imgproc.MORPH_OPEN, kernel);

            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(morphed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            input.copyTo(contoured);

            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(contoured, contours, i, Color.RED.toBGR(), 2, Imgproc.LINE_8);
            }

            telemetry.addData("Number of Contours", contours.size());

            assert !contours.isEmpty();
            MatOfPoint contour = contours.get(0);

            input.copyTo(bounded);

            Rect boundingRect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(bounded, boundingRect, Color.GREEN.toBGR(), 2);

            double area = Imgproc.contourArea(contour);
            double width = boundingRect.width;
            double height = boundingRect.height;

            int textStart = 30;
            int textHeight = 25;
            double fontScale = 1.2;
            int font = Imgproc.FONT_HERSHEY_PLAIN;

            DrawUtil.drawText(bounded, "Area: " + format(area,1),new Position(10, textStart), Color.GREEN);
            DrawUtil.drawText(bounded, "Width: " + format(width), new Position(10, textStart + textHeight), Color.GREEN);
            DrawUtil.drawText(bounded, "Height: " + format(height), new Position(10, textStart + 2 * textHeight), Color.GREEN);
            DrawUtil.drawText(bounded, "Fill: " + format(area / (width * height) * 100,1), new Position(10, textStart + 3 * textHeight), Color.GREEN);

            input.copyTo(output);
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
