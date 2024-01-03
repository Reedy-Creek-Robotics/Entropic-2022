package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class bareboneDrive extends OpMode {
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    Controller controller;

    private double drive = 0, turn = 0, strafe = 0;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotorEx.class, "FrontLeft");
        leftRear = hardwareMap.get(DcMotorEx.class, "BackLeft");
        rightRear = hardwareMap.get(DcMotorEx.class, "BackRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "FrontRight");

        controller = new Controller(gamepad1);
    }

    @Override
    public void loop() {
        drive = controller.leftStickY();
        strafe = controller.leftStickX();
        turn = controller.rightStickX();

        leftRear.setPower(drive - turn + strafe);
        rightRear.setPower(drive + turn - strafe);
        leftFront.setPower(drive - turn - strafe);
        rightFront.setPower(drive + turn + strafe);

        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        debugTelemetry(telemetry);
        telemetry.update();
    }

    public void debugTelemetry(Telemetry t) {
        t.addData("FL (current)", leftFront.getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("BL (current)", leftRear.getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("FR (current)", rightFront.getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("BR (current)", rightRear.getCurrent(CurrentUnit.MILLIAMPS));

        t.addData("FL (velocity)", leftFront.getVelocity());
        t.addData("BL (velocity)", leftRear.getVelocity());
        t.addData("FR (velocity)", rightFront.getVelocity());
        t.addData("BR (velocity)", rightRear.getVelocity());
    }
}
