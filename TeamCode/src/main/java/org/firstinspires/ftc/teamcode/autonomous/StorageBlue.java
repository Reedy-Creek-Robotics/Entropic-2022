package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Robot;

public class StorageBlue extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new Robot(this,false);
        robot.init();

        waitForStart();

        //Write the code here

        //prepare the arm for travel
        robot.getArm().moveToPosition(Arm.Position.TRAVEL);
        robot.waitForCommandsToFinish();

        //move forward to detection area
        robot.getDriveTrain().moveForward(0,.5);
        robot.waitForCommandsToFinish();

        //detect and store the barcode position
        //position = robot.detectPosition()
        //robot.waitForCommandsToFinish();

        //move the arm to the barcode position
        //robot.getArm().moveToPosition(position);

        //turn toward the shipping hub
        //robot.getDriveTrain().turn();

        //move to the shipping hub
        //robot.getDriveTrain().moveForward(0,.5);

        //robot.waitForCommandsToFinish();

        //drop off the preloaded box
        robot.getIntake().getDoor().openDoor();
        robot.waitForCommandsToFinish();
        //sleep for the time to unload the box
        robot.getIntake().getDoor().closeDoor();
        robot.waitForCommandsToFinish();

        //move backward to the carousel
        robot.getDriveTrain().moveBackward(0,.5);
        robot.waitForCommandsToFinish();

        //align up with the carousel
        //robot.getDriveTrain().turn();
        //robot.waitForCommandsToFinish();

        //spin the duck off the carousel
        //robot.getCarousel().spin();
        //robot.waitForCommandsToFinish();

        //turn to get the path to the warehouse
        //robot.getDriveTrain().turn();
        //robot.waitForCommandsToFinish();

        //go forward until parked in warehouse
        robot.getDriveTrain().moveForward(0,.5);
        robot.waitForCommandsToFinish();
    }

}
