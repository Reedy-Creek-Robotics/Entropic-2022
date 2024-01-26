package org.firstinspires.ftc.teamcode.bareBones;


import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.ColorDetector;
import org.firstinspires.ftc.teamcode.game.Controller;
import org.firstinspires.ftc.teamcode.opmodes.BaseTeleOp;
import org.opencv.core.Scalar;

import java.util.List;

@TeleOp(group = "Barebone Component Testing")
public class ColorDetectorTester extends BaseTeleOp {

    ColorDetector colorDetector;

    final int INITIAL_DETECTION_WINDOW = 20;
    final int MIN_DETECTION_WINDOW = 1;
    int colorDetectionWindow;

    Scalar lowerBound;
    Scalar defaultLowerBound;

    Scalar upperBound;
    Scalar defaultUpperBound;


    @Override
    public void init() {
        super.init();
        robot.getTeamPropDetector().activate();
        colorDetector = robot.getTeamPropDetector().getColorDetector();
        lowerBound = getLowerBound();
        upperBound = getUpperBound();
        colorDetectionWindow = INITIAL_DETECTION_WINDOW;
    }

    private Scalar getLowerBound() {
        //get range values
        return colorDetector.getParameters().ranges.get(0).first;
    }

    private Scalar getUpperBound() {
        int lastPairIndex = colorDetector.getParameters().ranges.size() - 1;
        return colorDetector.getParameters().ranges.get(lastPairIndex).second;
    }

    private void increaseColorDetectionWindow() {
 //       if (colorDetectionWindow < MAX_DETECTION_WINDOW) {
            colorDetectionWindow += 1;
            lowerBound.val[0] -= 1;
            upperBound.val[0] += 1;

            setColorDetectionBounds(lowerBound, upperBound);
//        }
    }

    private void decreaseColorDetectionWindow() {
        if (colorDetectionWindow > MIN_DETECTION_WINDOW) {
            colorDetectionWindow -= 1;
            upperBound.val[0] -= 1;
            lowerBound.val[0] += 1;

            setColorDetectionBounds(lowerBound, upperBound);
        }
    }

    private void setColorDetectionBounds(Scalar lowerBound, Scalar upperBound) {
        Pair<Scalar, Scalar> lowerPair = colorDetector.getParameters().ranges.get(0);
        int lastPairIndex = colorDetector.getParameters().ranges.size() - 1;
        Pair<Scalar, Scalar> upperPair = colorDetector.getParameters().ranges.get(lastPairIndex);

        colorDetector.getParameters().ranges.set(0, new Pair<>(lowerBound, lowerPair.second));
        colorDetector.getParameters().ranges.set(lastPairIndex, new Pair<>(upperPair.first, upperBound));
    }


    @Override
    public void loop() {
        if (controller.isPressed(Controller.Button.B)) {
            //if b is pressed, decrease color detection window
            decreaseColorDetectionWindow();

        } else if (controller.isPressed(Controller.Button.Y)) {
            increaseColorDetectionWindow();
        }
        List<ColorDetector.ColorDetection> detections = colorDetector.getDetections();
        telemetry.addData("Detections", detections.size());
        telemetry.addData("ColorDetectionWindow", colorDetectionWindow * 2);
        telemetry.addData("TeamPropPosition", robot.getTeamPropDetector().getDetectedPosition());
        telemetry.addData("lowerColorBound", lowerBound.val[0]);
        telemetry.addData("upperColorBound", upperBound.val[0]);
        telemetry.update();
    }
}
