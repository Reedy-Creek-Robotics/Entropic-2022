package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class LedStrip extends BaseComponent{
    QwiicLEDStrip ledStrip;

    public LedStrip(OpMode opMode) {
        super(opMode);
        this.ledStrip = hardwareMap.get(QwiicLEDStrip.class, "ledStrip");
    }

    /**
     * Activates the led strip with the given settings for the given light
     * @param color color the strip displays
     * @param position the light you want to turn on
     * @param brightness how bright the led is
     */
    public void activateLed(String color, int position, int brightness) {
        executeCommand(new BaseCommand(color,position,brightness));
    }

    /**
     * Activates the led strip with the given settings for all lights
     * @param color color the strip displays
     * @param brightness how bright the led is
     */
    public void activateLed(String color, int brightness) {
        executeCommand(new BaseCommand(color,brightness));
    }

    public void stopLedStrip() {
        ledStrip.setBrightness(0);
    }

    private class BaseCommand implements Command{
        private String color;
        private int position;
        private int brightness;

        public BaseCommand(String color, int position, int brightness) {
            this.color = color;
            this.position = position;
            this.brightness = brightness;
        }

        public BaseCommand(String color, int brightness) {
            this.color = color;
            this.brightness = brightness;
            position = -1;
        }

        @Override
        public void start() {
            if(position == -1) {
                ledStrip.setColor(Color.parseColor(color));
                ledStrip.setBrightness(brightness);
            }else {
                ledStrip.setColor(position,brightness);
                ledStrip.setBrightness(position,brightness);
            }
        }

        @Override
        public void stop() {
            stopLedStrip();
        }

        @Override
        public boolean updateStatus() {
            return false;
        }
    }
}
