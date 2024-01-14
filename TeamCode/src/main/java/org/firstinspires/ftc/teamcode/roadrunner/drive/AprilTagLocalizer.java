package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagLocalizer implements Localizer {

    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private Pose2d cameraPosition;

    public AprilTagLocalizer(Pose2d cameraPosition, WebcamName webcam) {
        this.cameraPosition = cameraPosition;

        // Create the AprilTag processor.
        aprilTagProcessor = new AprilTagProcessor.Builder()
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)//TODO: look into this for ultrawide cameras

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(webcam);


        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTagProcessor);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    Pose2d poseEstimate = new Pose2d();

    @NonNull
    @Override
    public Pose2d getPoseEstimate() {
        return poseEstimate;
    }

    @Override
    public void setPoseEstimate(@NonNull Pose2d pose2d) {
        poseEstimate = pose2d;
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return null;
    }

    @Override
    public void update() {
        calcNewPositions();
    }

    private Pose2d getTagPosition (AprilTagDetection detection){
        for (AprilTagPositions tag : AprilTagPositions.values()) {
            if (tag.id == detection.id) {
                return tag.pos;
            }
        }
        return null;
    }

    public List<Pose2d> calcNewPositions (){
        List<Pose2d> newPositions = new ArrayList<>();

        for (AprilTagDetection detection: aprilTagProcessor.getDetections()) {
            Pose2d detectionVector = new Pose2d(detection.ftcPose.y, detection.ftcPose.yaw);
            Pose2d tagPosition = getTagPosition(detection);

            newPositions.add(tagPosition.minus(detectionVector.plus(cameraPosition)));
        }
        //positionsOverTime.addAll(newPositions);
        return newPositions;
    }

    private enum AprilTagPositions {
        RED1        (1, 0, 0),
        RED2        (2, 0, 0),
        RED3        (3, 0, 0),
        REDWALL1    (4, 0, 0),
        REDWALL2    (5, 0, 0),
        BLUE1       (6, 0, 0),
        BLUE2       (7, 0, 0),
        BLUE3       (8, 0, 0),
        BLUEWALL1   (9, 0, 0),
        BLUEWALL2   (10, 0, 0);

        int id;
        Pose2d pos;

        AprilTagPositions(int id, double x, double y) {
            this.id = id;
            this.pos = new Pose2d(x,y);
        }
    }
}
