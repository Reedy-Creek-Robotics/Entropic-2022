package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.teamcode.util.TileEdgeDetectionUtil;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.List;

public class TileEdgeDetectionUtilTest {

    @Test
    public void detectLines() {
        URL url = TileEdgeDetectionUtilTest.class.getResource("/cornerTileImage.bmp");
        String filename = url.getFile();

        Mat image = Imgcodecs.imread(filename);

        List<TileEdgeDetectionUtil.Line> lines = TileEdgeDetectionUtil.detectLines(image, 0.0, 0.0);
    }

}
