package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.Y;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.util.Position;

import java.util.Arrays;
import java.util.List;

@TeleOp
public class ComponentTest extends OpMode {
    public Robot robot;

    //button input for gamepad 1
    private ElapsedTime lastButtonTime1;

    //button input for gamepad 2
    private ElapsedTime lastButtonTime2;

    public double limiter;

    public List<LinearSlide.SlideHeight> positions;
    public int currentPosition;

    public double turretLocation;

    @Override
    public void init() {
        robot = new Robot(this, true);
        robot.init();

        limiter = .3;
        lastButtonTime1 = new ElapsedTime();
        lastButtonTime2 = new ElapsedTime();

        positions = Arrays.asList(INTAKE, TRAVEL, GROUND_LEVEL, SMALL_POLE, MEDIUM_POLE, TOP_POLE);
        currentPosition = 0;

        turretLocation = 0;

    }

    private boolean deadZoneCheck(List<Double> values) {
        for (Double value : values) {
            if (Math.abs(value) > .1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        //sets the power to the drivetrain
        if (deadZoneCheck(Arrays.asList(drive, turn, strafe)) || !robot.getDriveTrain().isBusy()) {
            robot.getDriveTrain().drive(drive, turn, strafe, limiter);
        }

        if (deadZoneCheck(Arrays.asList((double) gamepad2.left_stick_y))) {
            turretLocation += -gamepad2.left_stick_y * .05;
            if (turretLocation < 0) {
                turretLocation = 0;
            } else if (turretLocation > 1) {
                turretLocation = 1;
            }
        }

        if (lastButtonTime1.seconds() > 0.25) {
            boolean buttonPress = true;
            if (gamepad1.dpad_up) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, Y, limiter);
            } else if (gamepad1.dpad_left) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, X, limiter);
            } else if (gamepad1.dpad_down) {
                robot.getDriveTrain().moveAlignedToTileCenter(-1, Y, limiter);
            } else if (gamepad1.dpad_right) {
                robot.getDriveTrain().moveAlignedToTileCenter(1, X, limiter);
            } else if (gamepad1.x) {
                robot.getDriveTrain().rotate(-90, limiter);
            } else if (gamepad1.y) {
                robot.getDriveTrain().rotate(90, limiter);
            } else if (gamepad1.b) {
                robot.getDriveTrain().stopAllCommands();
            } else if (gamepad1.start) {
                robot.getDriveTrain().setPosition(new Position(.5,.5));
            } else if (gamepad1.left_bumper) {
                limiter -= 0.05;
            } else if (gamepad1.right_bumper) {
                limiter += 0.05;
            } else {
                buttonPress = false;
            }
            if (buttonPress) {
                lastButtonTime1.reset();
            }
        }

        if (lastButtonTime2.seconds() > .25) {
            boolean buttonPress = true;
            if (gamepad2.a) {
                currentPosition = (1 + currentPosition) % positions.size();
            } else if (gamepad2.right_bumper) {
                //robot.getIntake().intake(.5);
            } else if (gamepad2.left_bumper) {
                //robot.getIntake().intake(-.5);
            } else {
                buttonPress = false;
            }
            if (buttonPress) {
                lastButtonTime2.reset();
            }
        }

        robot.getSlide().moveToPosition(positions.get(currentPosition));
        robot.getTurret().moveToPosition(turretLocation);

        telemetry.addData("Slide Position", positions.get(currentPosition));
        telemetry.addData("Limiter", limiter);
        robot.updateStatus();
    }
}
