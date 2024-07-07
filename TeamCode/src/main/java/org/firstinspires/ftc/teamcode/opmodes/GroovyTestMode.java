package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

@Autonomous
public class GroovyTestMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Binding binding = new Binding();
        binding.setVariable("x", 5);
        String sourceCode = "y = x * x";
        GroovyShell gs = new GroovyShell();
        Script script = gs.parse(sourceCode);
        script.run();

        Object y = binding.getVariable("y");
        telemetry.addData("y", y);
        telemetry.addData("y.getClass()", y == null ? "null" : y.getClass().getSimpleName());
        telemetry.update();
    }

}
