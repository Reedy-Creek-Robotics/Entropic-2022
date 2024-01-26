package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class SlideCalibration extends BaseCalibration{

    LinearSlide slide;
    DcMotor leftMotor, rightMotor;

    InputCatcher up = new InputCatcher(controller, Controller.Button.DPAD_UP);
    InputCatcher down = new InputCatcher(controller, Controller.Button.DPAD_DOWN);
    InputCatcher right = new InputCatcher(controller, Controller.Button.DPAD_RIGHT);
    InputCatcher left = new InputCatcher(controller, Controller.Button.DPAD_LEFT);

    @Override
    protected void switchToBarebone() {
        if(leftMotor == null || rightMotor == null){
            slide = null;

            leftMotor = hardwareMap.dcMotor.get("LeftSlide");
            rightMotor = hardwareMap.dcMotor.get("RightSlide");

            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    @Override
    protected void hardwareTesting() {
        leftMotor.setPower((controller.leftStickY() ));
        rightMotor.setPower((controller.leftStickY() ));

        telemetry.addData("left: ", leftMotor.getCurrentPosition());
        telemetry.addData("right: ", rightMotor.getCurrentPosition());
    }

    @Override
    protected void switchToComponent() {
        leftMotor = null;
        rightMotor = null;

        if(slide == null){
            slide = new LinearSlide(BaseComponent.createRobotContext(this));
            slide.init();
        }

        if(state == 2){
            slide.moveToTicks(800);
        }
    }

    @Override
    protected void calibration() {
        slide.manualSlideOverride(controller.leftStickY());

        telemetry.addData("pos",slide.getPosition());

    }

    @Override
    protected void confirmation() {
        slide.update();
    }
}
