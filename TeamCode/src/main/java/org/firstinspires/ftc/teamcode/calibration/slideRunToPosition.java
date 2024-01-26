package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class slideRunToPosition extends LinearOpMode {

    DcMotor leftMotor, rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("LeftSlide");
        rightMotor = hardwareMap.dcMotor.get("RightSlide");

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setTargetPosition(200);
        rightMotor.setTargetPosition(200);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        while(opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())){
            telemetry.addData("left: ", leftMotor.getCurrentPosition());
            telemetry.addData("right: ", rightMotor.getCurrentPosition());
            telemetry.update();
        }

        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}
