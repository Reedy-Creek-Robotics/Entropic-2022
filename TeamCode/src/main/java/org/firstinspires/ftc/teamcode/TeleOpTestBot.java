package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class TeleOpTestBot extends OpMode {
    private DcMotor arm;
    private Robot robot;

    @Override
    public void init() {
        arm = hardwareMap.dcMotor.get("arm");
        robot = new Robot(this, false);
    }

    @Override
    public void loop() {
        robot.getDriveTrain().drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
    }
}
