package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@Disabled
@TeleOp
public class BareBonesSlideHeightCalibration extends OpMode {
    private LinearSlide slide;
    private Controller controller;

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void init() {
        controller = new Controller(gamepad2);

        leftMotor = hardwareMap.dcMotor.get("LeftSlide");
        rightMotor= hardwareMap.dcMotor.get("RightSlide");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    @Override
    public void loop() {
        leftMotor.setPower(-(controller.leftStickY() * 0.25));
        rightMotor.setPower(-(controller.leftStickY() * 0.25));

        telemetry.addData("right: ", rightMotor.getCurrentPosition());
        telemetry.addData("left: ", leftMotor.getCurrentPosition());
        telemetry.update();
    }
}
