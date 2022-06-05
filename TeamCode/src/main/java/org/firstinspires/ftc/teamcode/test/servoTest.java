package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class servoTest extends OpMode {
    public Servo servo;

    @Override
    public void init() { servo = hardwareMap.servo.get("servo"); }
    @Override
    public void loop() {
        double pos = -(gamepad1.right_stick_y/2+0.5);
        servo.setPosition(pos);
        telemetry.addData("pos",pos);
        telemetry.update();
    }
}