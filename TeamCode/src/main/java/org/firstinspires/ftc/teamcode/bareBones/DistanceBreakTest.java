package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class DistanceBreakTest extends OpMode {

    DigitalChannel distanceBreakSensor;

    @Override
    public void init(){
        DigitalChannel distanceBreakSensor=hardwareMap.digitalChannel.get("distanceSensor");
    }

    @Override
    public void loop(){
        telemetry.addData("is within distance",distanceBreakSensor.getState());
        telemetry.update();
    }
}
