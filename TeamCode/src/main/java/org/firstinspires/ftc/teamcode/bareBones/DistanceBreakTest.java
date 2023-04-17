package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
@TeleOp
public class DistanceBreakTest extends OpMode {
    DigitalChannel distanceBreakSensor;

    boolean swit = false;
    @Override
    public void init(){
        distanceBreakSensor = hardwareMap.digitalChannel.get("distanceSensor");
    }

    @Override
    public void loop(){
        telemetry.addData("is within distance",distanceBreakSensor.getState());

        if(distanceBreakSensor.getState()){swit = true;}
        if(swit){telemetry.addData("test: ",true);}
        telemetry.update();
    }
}
