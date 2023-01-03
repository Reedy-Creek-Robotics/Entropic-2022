package org.firstinspires.ftc.teamcode.components;

import static android.os.Environment.getExternalStorageDirectory;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WebCam extends BaseComponent {

    public static final Size DEFAULT_RESOLUTION = new Size(640, 360);

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
    private OpenCvWebcam camera;

    /**
     * Used to set the exposure in ms for the webcam.
     */
    private ExposureControl exposureControl;

    public WebCam(RobotContext context, String cameraName, boolean streamOutput, Size resolution) {
        super(context);
        this.cameraName = cameraName;
        this.streamOutput = streamOutput;
        this.size = resolution;
    }

    public WebCam(RobotContext context, String cameraName, boolean streamOutput) {
        this(context, cameraName, streamOutput, DEFAULT_RESOLUTION);
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

        camera.showFpsMeterOnViewport(false);

        exposureControl = camera.getExposureControl();
        exposureControl.setMode(ExposureControl.Mode.Manual);
        setExposure(robotDescriptor.webCamExposureMs);
    }

    public synchronized void saveLastFrame() {
        String filename = getExternalStorageDirectory() + "/webcam-frame-" +
                new Date().toString().replace(' ', '-') +
                this.getExposure() + ".bmp";

        telemetry.addData("WebCam Frame Saved", filename);

        boolean success = Imgcodecs.imwrite(filename, output);
        telemetry.addData("did I write?", success);
    }

    public int getFrameCount() {
        return frameCount;
    }

    public boolean getStreamOutput() {
        return streamOutput;
    }

    public long getExposure() {
        return exposureControl.getExposure(TimeUnit.MILLISECONDS);
    }

    public boolean isReady() {
        return frameCount > 0;
    }

    public void setFrameProcessor(FrameProcessor frameProcessor) {
        this.frameProcessor = frameProcessor;
    }

    /**
     * Sets the exposure of the camera
     *
     * @param duration how long the exposure is set to in milliseconds
     */
    public void setExposure(long duration) {
        exposureControl.setExposure(duration, TimeUnit.MILLISECONDS);
    }

    public void removeFrameProcessor() {
        this.frameProcessor = null;
    }

    private class CameraPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {

            synchronized (WebCam.this) {

                input.copyTo(output);

                // Make sure it is RGBA, and the size that we expect.
                assert input.width() == (int) size.width && input.height() == (int) size.height;
                assert input.channels() == 4 : "Expected RGBA image from webcam";

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

    }

    public interface FrameProcessor {

        /**
         * Will be invoked on each frame, giving the processor a chance to analyze the input image
         * and, if desired, update the output image.
         *
         * @param input  the input frame image, in RGBA.
         * @param output the output image, in RGBA.
         */
        void processFrame(Mat input, Mat output);

    }

}
