package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.TeamPropDetector;

@Autonomous
public class AutoSpike extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();

        while(opModeInInit()){
            TeamPropDetector.TeamPropPosition detection = robot.getTeamPropDetector().getDetectedPosition();

            robot.getTeamPropDetector().setTargetColor(RobotContext.Alliance.BLUE);

            telemetry.addData("Prop Detection",detection);
            telemetry.update();
        }

        waitForStart();




    }
}
