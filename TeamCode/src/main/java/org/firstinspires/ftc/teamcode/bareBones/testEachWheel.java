/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(group = "Barebone Component Testing")
public class testEachWheel extends LinearOpMode
{
    public double powerLevel = 0.5;
    public int numTicks = 3000;

    @Override
    public void runOpMode()
    {
        DcMotor frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        DcMotor frontRight = hardwareMap.dcMotor.get("FrontRight");
        DcMotor backLeft = hardwareMap.dcMotor.get("BackLeft");
        DcMotor backRight = hardwareMap.dcMotor.get("BackRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection((DcMotorSimple.Direction.FORWARD));

        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);



        ElapsedTime timeSinceLastButton = new ElapsedTime();

        waitForStart();


        while (opModeIsActive())
        {
            if(gamepad1.a && timeSinceLastButton.milliseconds() > 1000) {
                timeSinceLastButton.reset();
                testWheel(frontLeft,"Front Left");
            }
            if(gamepad1.b && timeSinceLastButton.milliseconds() > 1000) {
                timeSinceLastButton.reset();
                testWheel(frontRight,"Front Right");
            }
            if(gamepad1.x && timeSinceLastButton.milliseconds() > 1000) {
                timeSinceLastButton.reset();
                testWheel(backLeft,"Back Left");
            }
            if(gamepad1.y && timeSinceLastButton.milliseconds() > 1000) {
                timeSinceLastButton.reset();
                testWheel(backRight,"Back Right");
            }
        }
    }

    public void testWheel(DcMotor thisMotor,String motorName) {
        thisMotor.setTargetPosition(numTicks);
        thisMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        thisMotor.setPower(powerLevel);
        while(thisMotor.isBusy()) {
            telemetry.addData("Testing ",motorName);
            telemetry.addData("Current ticks:",thisMotor.getCurrentPosition());
            telemetry.update();
        }
        thisMotor.setPower(0);
        thisMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }
}