package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.time.Instant;
import java.util.Date;

public class WebCam extends BaseComponent {

    public static final Size HOUGHRESOLUTION = new Size(640, 360);

    /**
     * The webcam name from the Rev control configuration.
     */
    private String cameraName;

    /**
     * Indicates if output should be streamed to the monitor.
     */
    private boolean streamOutput;

    /**
     * The desired camera resolution.
     */
    private Size size;

    /**
     * The frame that is currently being processed (in BGR format).
     */
    private Mat frame = new Mat();

    /**
     * The output that will be rendered to the viewport (in RGBA format).
     */
    private Mat output = new Mat();

    /**
     * The frame processor that will be invoked to process any captured data.
     */
    private FrameProcessor frameProcessor;

    /**
     * The number of frames that have been processed.
     */
    private int frameCount = 0;

    /**
     * The OpenCV camera device that we are using.
     */
    private OpenCvCamera camera;


    public WebCam(OpMode opMode, String cameraName, boolean streamOutput, Size resolution) {
        super(opMode);
        this.cameraName = cameraName;
        this.streamOutput = streamOutput;
        this.size = resolution;
    }

    public WebCam(OpMode opMode, String cameraName, boolean streamOutput) {
        this(opMode,cameraName,streamOutput,HOUGHRESOLUTION);
    }

    @Override
    public void init() {

        WebcamName webcamName = hardwareMap.get(WebcamName.class, this.cameraName);
        if (streamOutput) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        } else {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        }

        camera.setPipeline(new CameraPipeline());

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming((int) size.width, (int) size.height, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                //todo handle error
            }
        });

        while (!isReady() && !isStopRequested()) {
            sleep(100);
        }
    }

    public void saveLastFrame() {
        // todo: save the last frame to disk
        // todo: figure out how to save the image to disk?  maybe if a button is pressed?
        String filename = "webcam-frame-" + new Date().toString().replace(' ', '-') + ".jpg";
        telemetry.addData("WebCam Frame Saved", filename);

        Imgcodecs.imwrite(filename, output);
    }

    public int getFrameCount() {
        return frameCount;
    }

    public boolean getStreamOutput() {
        return streamOutput;
    }

    public boolean isReady() {
        return frameCount > 0;
    }

    public void setFrameProcessor(FrameProcessor frameProcessor) {
        this.frameProcessor = frameProcessor;
    }

    public void removeFrameProcessor() {
        this.frameProcessor = null;
    }

    private class CameraPipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {

            input.copyTo(output);

            // Make sure it is RGBA, and the size that we expect.
            assert input.width() == (int) size.width && input.height() == (int) size.height;
            assert input.channels() == 4 : "Expected RGBA image from webcam";

            // Convert to BGR before handing input frame to processors.
            Imgproc.cvtColor(input, frame, Imgproc.COLOR_RGBA2BGR);

            assert frame.channels() == 3 : "Expected BGR image after conversion";

            frameCount++;

            // Allow any frame processors to analyze the image and annotate the output.
            if (frameProcessor != null) {
                try {
                    frameProcessor.processFrame(input, output);
                } catch (Exception e) {
                    telemetry.addData("Frame Error", ErrorUtil.convertToString(e));
                }
            }

            return output;
        }
    }

    public interface FrameProcessor {

        /**
         * Will be invoked on each frame, giving the processor a chance to analyze the input image
         * and, if desired, update the output image.
         *
         * @param input   the input frame image, in BGR.
         * @param output  the output image, in RGBA.
         */
        void processFrame(Mat input, Mat output);

    }

}
