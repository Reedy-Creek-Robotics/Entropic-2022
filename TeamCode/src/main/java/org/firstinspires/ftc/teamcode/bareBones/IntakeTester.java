package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.BaseDrivingTeleOp;


public class RollerIntake extends BaseDrivingTeleOp {

    private DcMotor intakeMotor;

    @Override
    public void init() {
        intakeMotor = hardwareMap.dcMotor.get("Intake");
    }

    @Override
    public void loop() {
        //If I press y, it goes forward, x goes backward.
        if (gamepad1.right_trigger) {
            intakeMotor.setPower(1);
        } else if (gamepad1.left_trigger) {
            intakeMotor.setPower(-1);
        } else {
            intakeMotor.setPower(0);
        }
    }
}
