package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_TRIGGER;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;


/*
* TODO:
*  Check why bot running slow
*  Increase slow mode power - bot won't strafe
* */

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();
    }

    @Override
    public void start() {
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();
        robot.getSlide().rotate(LinearSlide.RotationPoints.INTAKE);

        robot.getDroneLauncher().load();
    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

        // Intake
        if (controller.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().outtakeManual();
        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            robot.getIntake().intakeManual();
        } else {
            robot.getIntake().stopIntake();
        }

        // Slide
        if (controller.isPressed(Controller.Button.TRIANGLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.SECOND_LEVEL);
        } else if (controller.isPressed(Controller.Button.CIRCLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.THIRD_LEVEL);
        } else if (controller.isPressed(Controller.Button.CROSS)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.FIRST_LEVEL);
        }

        if (controller.isButtonDown(Controller.Button.DPAD_RIGHT)) {
            robot.getSlide().manualSlideMove(1);
        } else if (controller.isButtonDown(Controller.Button.DPAD_LEFT)) {
            robot.getSlide().manualSlideMove(-1);
        } else if (!robot.getSlide().isBusy()){
            robot.getSlide().stopMotors();
        }

        if (controller.isButtonDown(Controller.Button.DPAD_UP)){
            robot.getRiggingLift().moveUp();
        }else if (controller.isButtonDown(Controller.Button.DPAD_DOWN)){
            robot.getRiggingLift().moveDown();
        }else {
            robot.getRiggingLift().stop();
        }

        // Outtake
        if (controller.isPressed(Controller.Button.LEFT_BUMPER)) {
            robot.getOuttake().toggleLeft();
        }
        if (controller.isPressed(Controller.Button.RIGHT_BUMPER)) {
            robot.getOuttake().toggleRight();
        }

        //Drone launcher
        if (controller.isPressed(Controller.Button.OPTIONS)) {
            robot.getDroneLauncher().launch();
        }

        if( controller.isPressed(Controller.Button.SHARE)){
            robot.getSlide().resetSlideTicks();
        }

        //Stack Knocker
        if (controller.isPressed(Controller.Button.PS)) {
            robot.getDriveTrain().getRoadrunner().setPoseEstimate(new Pose2d(0,0,Math.toRadians(robot.getRobotContext().getAlliance().getRotation())));
        }



        //TODO: Make trackpad disable Geofence!!!

        robot.update();
    }
}
