package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class RollerIntake extends OpMode {

    private DcMotor intakeMotor;

    @Override
    public void init() {
        intakeMotor = hardwareMap.dcMotor.get("Intake");
    }

    @Override
    public void loop() {
        //If I press y, it goes forward, x goes backward.
        if (gamepad1.y) {
            intakeMotor.setPower(1);
        } else if (gamepad1.x) {
            intakeMotor.setPower(-1);
        }
    }
}
