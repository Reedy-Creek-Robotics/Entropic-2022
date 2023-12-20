package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Controller;

import java.util.ArrayList;
import java.util.List;

public class HardwareTester extends OpMode {

    private Controller controller;
    private List<DcMotorEx> motors = new ArrayList<>();
    private int selectedMotorIndex = 0;

    private DcMotorEx Motor1;
    private DcMotorEx Motor2;
    private DcMotorEx Motor3;
    private DcMotorEx Motor4;

    private boolean MotorControl;

    private List<Servo> servos = new ArrayList<>();
    private int selectedServoIndex = 0;

    private Servo Servo1;
    private Servo Servo2;
    private Servo Servo3;
    private Servo Servo4;

    private boolean ServoControl;

    @Override
    public void init() {
        controller = new Controller(gamepad1);

        for (DcMotor dcMotor : hardwareMap.dcMotor) {
            motors.add((DcMotorEx) dcMotor);

        }
        for (Servo servo : hardwareMap.servo) {
            servos.add((Servo) servo);

        }
    }

    @Override
    public void loop() {
        // changes from testing motors to servos with dpad up
        if (controller.isPressed(Controller.Button.DPAD_UP)) {
            MotorControl = true;
            ServoControl = false;
        // changes from testing servos to motors with dpad down
        } else if (controller.isPressed(Controller.Button.DPAD_DOWN)) {
            MotorControl = false;
            ServoControl = true;
        }
        if (MotorControl) {
            // This changes the selected motor
            if (controller.isPressed(Controller.Button.DPAD_RIGHT)) {
                selectedMotorIndex++;
            } else if (controller.isPressed(Controller.Button.DPAD_LEFT)) {
                selectedMotorIndex--;
            } else if (controller.isPressed(Controller.Button.DPAD_RIGHT) && selectedMotorIndex == 3) {
                selectedMotorIndex = 0;
            } else if (controller.isPressed(Controller.Button.DPAD_LEFT) && selectedMotorIndex == 0) {
                selectedMotorIndex = 3;
            }
        } else if (ServoControl){
            // This changes the selected servo
            if (controller.isPressed(Controller.Button.DPAD_RIGHT)) {
                selectedServoIndex++;
            } else if (controller.isPressed(Controller.Button.DPAD_LEFT)) {
                selectedServoIndex--;
            } else if (controller.isPressed(Controller.Button.DPAD_RIGHT) && selectedServoIndex == 3) {
                selectedServoIndex = 0;
            } else if (controller.isPressed(Controller.Button.DPAD_LEFT) && selectedMotorIndex == 0) {
                selectedServoIndex = 3;
            }
        }

        // This gets the motors hardware map name
        DcMotorEx selectedMotor = motors.get(selectedMotorIndex);
        String motorName = hardwareMap.getNamesOf(selectedMotor).iterator().next();
        telemetry.addData("Motor", motorName);

        // This gets the servos hardware map name
        Servo selectedServo = servos.get(selectedServoIndex);
        String servoName = hardwareMap.getNamesOf(selectedServo).iterator().next();
        telemetry.addData("Servo", servoName);
    }

}
