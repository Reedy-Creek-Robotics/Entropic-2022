package org.firstinspires.ftc.teamcode.components;

import static android.os.Environment.getExternalStorageDirectory;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor.WebCamDescriptor;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Date;
import java.util.concurrent.TimeUnit;


//TODO: redo with new FTC portal thing
public class WebCam extends BaseComponent {

    /**
     * The webcam descriptor.
     */
    private WebCamDescriptor webCamDescriptor;

    /**
     * Indicates if output should be streamed to the monitor.
     */
    private boolean streamOutput;

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


    public WebCam(RobotContext context, WebCamDescriptor descriptor, boolean streamOutput) {
        super(context);
        this.webCamDescriptor = descriptor;
        this.streamOutput = streamOutput;
    }

    @Override
    public void init() {

        String name = webCamDescriptor.name;
        Size resolution = webCamDescriptor.resolution;
        WebcamName webcamName = hardwareMap.get(WebcamName.class, name);

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
                camera.startStreaming((int) resolution.width, (int) resolution.height, OpenCvCameraRotation.UPRIGHT);
                camera.showFpsMeterOnViewport(false);

                // Now that the camera is open the exposure can be adjusted.
                exposureControl = camera.getExposureControl();
                exposureControl.setMode(ExposureControl.Mode.Manual);
                setExposure(webCamDescriptor.exposureMs);
            }

            @Override
            public void onError(int errorCode) {
                //telemetry.log().add("Error opening " + cameraName + ": " + errorCode);
            }
        });
    }

    public void stop() {
        camera.setPipeline(null);
        camera.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            @Override
            public void onClose() {
                telemetry.log().add("Stopped camera [" + webCamDescriptor.name + "]");
            }
        });
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

    public Size getResolution() {
        return webCamDescriptor.resolution;
    }

    public WebCamDescriptor getWebCamDescriptor() {
        return webCamDescriptor;
    }

    public boolean isReady() {
        return frameCount > 0;
    }

    public void waitUntilReady() {
        while (!isReady() && !isStopRequested()) {
            sleep(50);
        }
    }

    public FrameProcessor getFrameProcessor() {
        return frameProcessor;
    }

    public void setFrameProcessor(FrameProcessor frameProcessor) {
        this.frameProcessor = frameProcessor;
    }

    public void removeFrameProcessor() {
        this.frameProcessor = null;
    }

    public boolean isStreaming() {
        return streamOutput;
    }

    /**
     * Sets the exposure of the camera
     *
     * @param duration how long the exposure is set to in milliseconds
     */
    public void setExposure(long duration) {
        exposureControl.setExposure(duration, TimeUnit.MILLISECONDS);
    }

    public Long getExposure() {
        return exposureControl != null ?
                exposureControl.getExposure(TimeUnit.MILLISECONDS) :
                null;
    }

    private class CameraPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {
            try {

                synchronized (WebCam.this) {

                    FrameContext context = new FrameContext(
                            new ElapsedTime(),
                            frameCount++
                    );

                    input.copyTo(output);

                    // Make sure it is RGBA, and the size that we expect.
                    Size resolution = webCamDescriptor.resolution;
                    assert input.width() == (int) resolution.width && input.height() == (int) resolution.height;
                    assert input.channels() == 4 : "Expected RGBA image from webcam";

                    // Allow any frame processors to analyze the image and annotate the output.
                    if (frameProcessor != null) {
                        frameProcessor.processFrame(input, output, context);

                    }

                    return output;
                }
            } catch (Exception e) {
                telemetry.addData("Frame Error", ErrorUtil.convertToString(e));
            }
            return input;
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
        void processFrame(Mat input, Mat output, FrameContext frameContext);

    }

    public static class FrameContext {

        public ElapsedTime frameTime;
        public int frameCount;

        public FrameContext(ElapsedTime frameTime, int frameCount) {
            this.frameTime = frameTime;
            this.frameCount = frameCount;
        }
    }

}
