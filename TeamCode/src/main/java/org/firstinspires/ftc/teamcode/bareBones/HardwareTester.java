package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

import java.util.ArrayList;
import java.util.List;

@TeleOp
public class HardwareTester extends OpMode {

    private Controller controller;
    private int selectedDevice = 0;

    private List<HardwareDevice> devices;

    double targetPosition;

    @Override
    public void init() {
        devices = new ArrayList<>();
        for (HardwareDevice device : hardwareMap) {
            devices.add(device);

            if(device instanceof DcMotorEx){
                ((DcMotorEx) device).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                ((DcMotorEx) device).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }

        controller = new Controller(gamepad1);
    }

    @Override
    public void loop() {
        // Output the selected device to telemetry
        HardwareDevice device = devices.get(selectedDevice);

        telemetry.addData("Device Count", selectedDevice + " of " + devices.size());
        telemetry.addData("Device", hardwareMap.getNamesOf(device));
        telemetry.addData("Type", device.getClass().getSimpleName());

        if (controller.isPressed(Controller.Button.RIGHT_BUMPER)) {
            selectedDevice++;
            if (selectedDevice >= devices.size()) selectedDevice = 0;
        } else if (controller.isPressed(Controller.Button.LEFT_BUMPER)) {
            selectedDevice--;
            if (selectedDevice < 0) selectedDevice = devices.size() - 1;
        }

        if (device instanceof DcMotorEx) {
            DcMotorEx motor = (DcMotorEx) device;



            telemetry.addData("Position", motor.getCurrentPosition());
            telemetry.addData("Power", motor.getPower());
            telemetry.addData("encoder tolerance", motor.getTargetPositionTolerance());

            double power = controller.leftStickY();
            motor.setPower(power*0.5);

            if (controller.isPressed(Controller.Button.CIRCLE)) {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            /*if(controller.isPressed(Controller.Button.SQUARE)){
                motor.setTargetPosition();
            }*/

        } else if (device instanceof Servo) {
            Servo servo = (Servo) device;

            telemetry.addData("Position", servo.getPosition());

            if (controller.isPressed(Controller.AnalogControl.RIGHT_STICK_Y)) {
                targetPosition += controller.analogValue(Controller.AnalogControl.RIGHT_STICK_Y) * 0.01;
            }

            if (controller.isPressed(Controller.Button.DPAD_UP)){
                targetPosition += 0.05;
            } else if (controller.isPressed(Controller.Button.DPAD_DOWN)) {
                targetPosition -= 0.05;
            }

            servo.setPosition(targetPosition);

            if (controller.isPressed(Controller.Button.CIRCLE)) {
                targetPosition = 0;
                servo.setPosition(targetPosition);
            }

        }

        telemetry.update();
    }

}
