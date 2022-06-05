package org.firstinspires.ftc.teamcode.test;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.QwiicLEDStrip;

//yellow to green

@TeleOp()
@Disabled
public class TestLEDOpMode extends OpMode {
    private QwiicLEDStrip ledStrip;
    private ElapsedTime elapsedTime = new ElapsedTime();
    private int colorIndex = 0;
    private @ColorInt int[] colors = new int[] { Color.rgb(148, 0, 211), Color.rgb(75, 0, 130), Color.rgb(0, 0, 255),
            Color.rgb(0, 255, 0), Color.rgb(255, 0, 0), Color.rgb(255, 255, 0), Color.parseColor("purple"),
            Color.parseColor("teal"), Color.parseColor("silver"), Color.rgb(0, 0, 0) };

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        ledStrip = hardwareMap.get(QwiicLEDStrip.class, "ledStrip");
        ledStrip.setBrightness(4);
        ledStrip.setColor(Color.parseColor("red"));
        ledStrip.setColor(5, Color.parseColor("blue"));
        telemetry.addData("led_strip",ledStrip.getConnectionInfo());
    }

    @Override
    public void start() {
        elapsedTime.reset();
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        ledStrip.setColor(5,Color.parseColor("yellow"));
        ledStrip.setBrightness(31);
    }

    @Override
    public void stop() {
        ledStrip.turnAllOff();
    }
}
