package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
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

    protected boolean usingHough = true;
    protected boolean samProposal = true;

    TeamPropDetector.TeamPropPosition propPosition;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            propPosition =  TeamPropDetector.TeamPropPosition.LEFT; //robot.getTeamPropDetector().waitForDetection(2);
            telemetry.addData("prop pos", propPosition);
            telemetry.update();

            waitForStart();

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

        robot.getDriveTrain().roadrunner.setPoseEstimate(getStartPosition());
    }

    protected abstract TrajectorySequence toTileCenter();

    public TrajectorySequence toSpikeTrajectory(TeamPropDetector.TeamPropPosition detection){
        return robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(toTileCenter().end())
                .forward(24)
                .turn(Math.toRadians(getPropRotation(detection)))
                .forward(3)
                .build();
    }

    public abstract TrajectorySequence toStageTrajectory();

    public TrajectorySequence stageLineUp(){
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(toStageTrajectory().end())
                .back(5)
                .build();
    };

    public TrajectorySequence stageBack(){
        return robot.getDriveTrain().getRoadrunner().trajectorySequenceBuilder(stageLineUp().end())
                .forward(5)
                .build();
    };

    public  abstract TrajectorySequence toParkTrajectory();

    //This method finds how much the bot needs to turn based on the detection, no matter if it is on the red or blue side
    public double getPropRotation(TeamPropDetector.TeamPropPosition detection) {

        //coordinated move to dispense prop
        switch (detection) {
            case LEFT:
                return  75;
            case MIDDLE:
                return 0;
            case RIGHT:
                return -75;
            case NOTFOUND:
                return 0;
        }

        return 0;
    }

    protected abstract Pose2d getStartPosition();
    protected abstract void runPath();
    protected abstract Alliance getAlliance();

    public enum Alliance{
        BLUE(1),
        RED(-1);

        int value;

        Alliance(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }



}
