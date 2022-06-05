package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Disabled
@TeleOp
public class BeamBreak extends OpMode {
    public DigitalChannel beamBreak;

    @Override
    public void init() {
        beamBreak = hardwareMap.digitalChannel.get("beamBreak");
    }

    @Override
    public void loop() {
        if(beamBreak.getState()){
            telemetry.addData("Beam state:", "Nothing is here");
        }
        else{
            telemetry.addData("Beam state:", "Object detected");
        }
        telemetry.update();
    }
}
