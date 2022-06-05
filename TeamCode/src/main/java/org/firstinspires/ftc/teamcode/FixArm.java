package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class FixArm extends OpMode {
    public DcMotor arm;
    public int position;
    public final static double DEADZONE = 0.1;

    @Override
    public void init() {
        arm = hardwareMap.dcMotor.get("arm");
        position = 0;
    }

    @Override
    public void loop() {
        if(Math.abs(gamepad2.right_stick_y) > DEADZONE) {
            position += 100 * -gamepad2.right_stick_y;
        }
        arm.setTargetPosition(position);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(.5);
    }
}
