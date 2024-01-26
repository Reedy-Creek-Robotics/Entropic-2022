package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoCalibration extends BaseCalibration{
    Servo launcher;

    @Override
    protected void switchToBarebone() {
        launcher = hardwareMap.servo.get("DroneLaunchServo");
    }

    @Override
    protected void hardwareTesting() {
        telemetry.addData("current position", launcher.getPosition());
    }

    @Override
    protected void switchToComponent() {
        launcher.setPosition(1);
    }

    @Override
    protected void calibration() {

    }

    @Override
    protected void confirmation() {

    }
}
