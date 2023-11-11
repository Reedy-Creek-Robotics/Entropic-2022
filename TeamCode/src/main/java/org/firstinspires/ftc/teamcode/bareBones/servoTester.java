package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(group = "Barebone Component Testing")
public class servoTester extends OpMode {
    public CRServo crServo;


    @Override
    public void init() {
        crServo = hardwareMap.crservo.get("Intake");
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            crServo.setPower(1);
        }else if(gamepad1.b) {
            crServo.setPower(-1);
        }

        if(gamepad1.y) {
            crServo.setPower(0);
        }
    }
}
