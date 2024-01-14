package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class SlideHeightCalibration extends OpMode {
    private LinearSlide slide;
    private Controller controller;

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void init() {
        //slide = new LinearSlide(BaseComponent.createRobotContext(this));
        controller = new Controller(gamepad2);

        leftMotor =  hardwareMap.dcMotor.get("LeftSlide");
        rightMotor = hardwareMap.dcMotor.get("RightSlide");

        /*leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
    }

    @Override
    public void loop() {
        //slide.manualSlideMove(-controller.leftStickY() * 0.25);
        leftMotor.setPower(-(controller.leftStickY() * 0.25));
        rightMotor.setPower(-(controller.leftStickY() * 0.25));

        /*telemetry.addData("right: ", rightMotor.getCurrentPosition());
        telemetry.addData("left: ", leftMotor.getCurrentPosition());
        telemetry.update()*/;
    }
}
