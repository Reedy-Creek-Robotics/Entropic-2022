package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.TeamPropDetector;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;

public abstract class  AutoMain extends LinearOpMode {

    protected static final double BASE_SPEED = 0.8;

    protected int coneCount = 5;

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;
    protected AprilTagDetection aprilTagDetection;


    TeamPropDetector.TeamPropPosition propPosition = TeamPropDetector.TeamPropPosition.NOTFOUND;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            telemetry.log().clear();

            if (!isStopRequested()){
                runPath();
            }

        } finally {
            // Save the position to disk, so it can be picked up by the TeleOp
            robot.savePositionToDisk();
        }
    }



    protected void initRobot() {
        robot = new Robot(this);
        robot.init();

        telemetry.log().add("here 1");
        telemetry.update();

        robot.setAlliance(getAutoAlliance());
        robot.getTeamPropDetector().activate();

        robot.getDriveTrain().roadrunner.setPoseEstimate(getStartPosition());

        telemetry.addLine(robot.getTeamPropDetector().getColor().toString());


        while (opModeInInit()){
            propPosition = robot.getTeamPropDetector().getDetectedPosition();
            telemetry.addData("position", propPosition);
            telemetry.update();
        }


    }

    protected abstract TrajectorySequence toTileCenter();

    public TrajectorySequence toSpikeTrajectory(){
        TrajectorySequence trajectorySequence;

        if (propPosition == TeamPropDetector.TeamPropPosition.LEFT || propPosition == TeamPropDetector.TeamPropPosition.RIGHT) {
            trajectorySequence = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(toTileCenter().end())
                    .forward(22)
                    .turn(Math.toRadians(propPosition.getRotation()))
                    .forward(3)
                    .build();
        }else {
            trajectorySequence = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(toTileCenter().end())
                    .forward(22)
                    //.turn(Math.toRadians(propPosition.getRotation()))
                    .forward(3)
                    .build();
        }


        return trajectorySequence;
    }

    public abstract TrajectorySequence toStageTrajectory();

    public TrajectorySequence stageLineUp(){
        TrajectorySequence trajectorySequence;

        if(propPosition == TeamPropDetector.TeamPropPosition.LEFT || propPosition == TeamPropDetector.TeamPropPosition.RIGHT){
            trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toStageTrajectory().end())
                    .strafeRight(propPosition.getStrafeDistance())
                    .back(5.5)
                    .build();
        }else {
            trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toStageTrajectory().end())
                    //.strafeRight(propPosition.getStrafeDistance())
                    .back(5.5)
                    .build();
        }



        return trajectorySequence;
    };

    public TrajectorySequence stageBack(){
        TrajectorySequence trajectorySequence = robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(stageLineUp().end())
                .forward(5.5)
                .build();

        return trajectorySequence;
    };

    public abstract TrajectorySequence toParkTrajectory();

    //This method finds how much the bot needs to turn based on the detection, no matter if it is on the red or blue side

    protected abstract Pose2d getStartPosition();
    protected void runPath(){
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();
        robot.getSlide().rotate(LinearSlide.RotationPoints.INTAKE);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toTileCenter());
        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toSpikeTrajectory());

        robot.getIntake().rollOut(0.35);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toStageTrajectory());

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.AUTO);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(stageLineUp());

        robot.getOuttake().outtakeBoth();
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(stageBack());

        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TRANSFER);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().getRoadrunner().followTrajectorySequence(toParkTrajectory());
    }

    protected abstract RobotContext.Alliance getAutoAlliance();




}
