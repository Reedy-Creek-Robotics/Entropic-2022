package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;

import java.io.File;
import java.io.IOException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

@Autonomous
public class GroovyTestMode extends LinearOpMode {

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;

    @Override
    public void runOpMode() throws InterruptedException {

        try {
            telemetry.log().setCapacity(10);
            telemetry.log().add("Init robot", "");

            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            if (!isStopRequested()) {
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

    protected Pose2d getStartPosition() {
        return new Pose2d(12, -60, Math.toRadians(90));
    }

    public void runPath() {
        Binding binding = new Binding();

        binding.setVariable("robot", robot);
        binding.setVariable("telemetry", telemetry);
        binding.setVariable("opMode", this);

        GroovyShell shell = new GroovyShell(binding);

        Script script;
        try {
            File scriptFile = new File("MyGroovyAuthPath.groovy");
            script = shell.parse(scriptFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        script.run();
    }

}
