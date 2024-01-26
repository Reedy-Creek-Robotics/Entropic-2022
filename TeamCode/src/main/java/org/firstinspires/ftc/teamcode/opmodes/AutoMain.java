package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.TeamPropDetector;
import org.openftc.apriltag.AprilTagDetection;

public abstract class  AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = 0.8;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;
    protected AprilTagDetection aprilTagDetection;


    protected boolean usingHough = true;
    protected boolean samProposal = true;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            //detect prop
            //robot.getTeamPropDetector().getDetectedPosition();

            /*while (1) {
                robot.getTeamPropDetector().getDetectedPosition();
            }*/

            // Allow the child class to run its auto path.
            runAuto();


        } finally {
            // Save the position to disk, so it can be picked up by the TeleOp
            robot.savePositionToDisk();
        }
    }



    protected void initRobot() {
        robot = new Robot(this);
    }

    protected abstract void runAuto();

    protected abstract Pose2d getStartPosition();

    //This method finds how much the bot needs to turn based on the detection, no matter if it is on the red or blue side
    public double getPropRotation() {
        //detect prop position
        TeamPropDetector.TeamPropPosition pos = robot.getTeamPropDetector().waitForDetection(1);

        double propDispenseRotation = 270;

        Enum<TeamPropDetector.TargetColor> color = robot.getTeamPropDetector().getColor();

        //coordinated move to dispense prop
        switch (pos) {
            case LEFT:
                propDispenseRotation = -90;
                break;
            case MIDDLE:
                propDispenseRotation = 0;
                break;
            case RIGHT:
                propDispenseRotation = 90;
                break;
            case NOTFOUND:
                propDispenseRotation = 0;
                break;
        }

        if (color == TeamPropDetector.TargetColor.BLUE) {
            propDispenseRotation += 180;
        }
        return propDispenseRotation;
    }



}
