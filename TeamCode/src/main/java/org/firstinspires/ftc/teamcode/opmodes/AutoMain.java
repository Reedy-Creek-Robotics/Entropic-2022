package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.components.TeamPropDetector;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
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

    public TrajectorySequenceBuilder spikeTrajectory(double spikeRotation){
        return robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(getStartPosition())
                .forward(24)
                .turn(Math.toRadians(spikeRotation))
                .forward(3)
                .addDisplacementMarker(()->{
                    robot.getIntake().rollOut(0.1);
                });
    }

    //This method finds how much the bot needs to turn based on the detection, no matter if it is on the red or blue side
    public double getPropRotation() {
        //detect prop position
        TeamPropDetector.TeamPropPosition pos = robot.getTeamPropDetector().waitForDetection(1);

        Enum<TeamPropDetector.TargetColor> color = robot.getTeamPropDetector().getColor();

        //coordinated move to dispense prop
        switch (pos) {
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
