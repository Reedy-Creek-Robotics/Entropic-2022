package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Disabled
@TeleOp
public class ServoMotorTest extends OpMode {

    private static List<String> motorNames = Arrays.asList(
            "FrontLeft",
            "FrontRight",
            "BackLeft",
            "BackRight",
            "arm"
    );

    private static List<String> servoNames = Arrays.asList(
    );

    private List<DcMotorEx> motors;
    private List<Servo> servos;

    private int currentMotor;
    private int currentServo;

    @Override
    public void init() {
        motors = new ArrayList<>();
        for (String name : motorNames) {
            motors.add(motor(name));
        }

        servos = new ArrayList<>();
        for (String name : servoNames) {
            servos.add(servo(name));
        }

        currentMotor = 0;
        currentServo = 0;
    }

    @Override
    public void loop() {
        //Gamepad 1 = Motors
        //Gamepad 2 = Servos

        //Switches between motors with the dpad
        if (gamepad1.dpad_up) {
            currentMotor++;
            currentMotor = currentMotor % motors.size();
        }
        if (gamepad1.dpad_down) {
            currentMotor--;
            currentMotor = currentMotor % motors.size();
        }

        //Switches between servos with the dpad
        if(gamepad2.dpad_up) {
            currentServo++;
            currentServo = currentMotor % servos.size();
        }
        if(gamepad2.dpad_down) {
            currentServo--;
            currentServo = currentServo % servos.size();
        }
        
        if(motors.size() !=0 ) {
            //Displays the current motor in telemetry
            telemetry.addData("Current Motor", motorNames.get(currentMotor));
            telemetry.addData("Current motor power",gamepad1.left_stick_y);

            //Sets the power and position of the current motor and servo
            motors.get(currentMotor).setPower(gamepad1.left_stick_y);


            //Makes motors not in use stop running
            for(DcMotorEx motor: motors) {
                if(motor != motors.get(currentMotor)) {
                    motor.setPower(0);
                }
            }
        }else {
            telemetry.addData("No Motor","");
        }

        if(servos.size() != 0) {

            //Displays the current servo in telemetry
            telemetry.addData("Current Servo",servoNames.get(currentServo));
            telemetry.addData("Current servo position",gamepad2.left_stick_y);


            //Sets the position of the current servo
            servos.get(currentServo).setPosition(gamepad2.left_stick_y);

            //Makes servos not in use go to its 0 position
            for(Servo servo : servos) {
                if(servo != servos.get(currentServo)) {
                    servo.setPosition(0);
                }
            }
        }else {
            telemetry.addData("No Servo","");
        }


        telemetry.update();
    }

    private DcMotorEx motor(String name) {
        return (DcMotorEx) hardwareMap.dcMotor.get(name);
    }

    private Servo servo(String name) {
        return hardwareMap.servo.get(name);
    }
}
