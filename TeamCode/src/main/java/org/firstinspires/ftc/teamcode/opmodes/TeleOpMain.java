package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_TRIGGER;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TeleOpMain extends BaseDrivingTeleOp {

    public static final double MANUAL_SLIDE_POWER = 0.7;
    public static final double RIGGING_LIFT_POWER = 1.0;
    private int coneCount = 4;

    @Override
    public void init() {
        super.init();

        // Load the position from disk, so it can pick up the previous position from the auto path.
        robot.loadPositionFromDisk();


    }

    @Override
    public void loop() {
        // Driving
        applyDriving();

        // Intake
        if (controller.isPressed(LEFT_TRIGGER)) {
            robot.getIntake().intakeManual();
        } else if (controller.isPressed(RIGHT_TRIGGER)) {
            robot.getIntake().outakeManual();
        } else {
            robot.getIntake().stopIntake();
        }
        // Lift
        if (controller.isPressed(Controller.Button.DPAD_LEFT)) {
            robot.getRiggingLift().moveLift(RIGGING_LIFT_POWER);
        } else if (controller.isPressed(Controller.Button.DPAD_RIGHT)) {
            robot.getRiggingLift().moveLift(-RIGGING_LIFT_POWER);
        } else {
            robot.getRiggingLift().stop();
        }
        // Slide
        if (controller.isPressed(Controller.Button.TRIANGLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.THIRD_LEVEL);

        } else if (controller.isPressed(Controller.Button.CIRCLE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.SECOND_LEVEL);

        } else if (controller.isPressed(Controller.Button.X)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.FIRST_LEVEL);

        } else if (controller.isPressed(Controller.Button.SQUARE)) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);

        } else if (controller.isPressed(Controller.Button.DPAD_UP)) {
            robot.getSlide().manualSlideMove(MANUAL_SLIDE_POWER);

        } else if (controller.isPressed(Controller.Button.DPAD_DOWN)) {
            robot.getSlide().manualSlideMove(-MANUAL_SLIDE_POWER);

        } else if (!robot.getSlide().isBusy()){
            robot.getSlide().stopMotors();
        }
        // Outtake
        if (controller.isPressed(Controller.Button.LEFT_BUMPER)) {
            robot.getOuttake().dropLeft();
        } else if (controller.isPressed(Controller.Button.RIGHT_BUMPER)) {
            robot.getOuttake().dropRight();
        }
        //TODO: Make trackpad disable Geofence!!!
        robot.updateStatus();
    }


}
