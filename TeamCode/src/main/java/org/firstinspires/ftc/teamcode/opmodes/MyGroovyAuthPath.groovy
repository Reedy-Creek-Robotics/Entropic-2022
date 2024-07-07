package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.components.Robot


def robot = (Robot) binding.getVariable("robot")
def telemetry = (Telemetry) binding.getVariable("telemetry")
def opMode = (GroovyTestMode) binding.getVariable("opMode")

telemetry.log().add("Hello from Groovy!")
telemetry.update()

robot.driveTrain.roadrunner.turn(Math.PI / 2)
robot.waitForCommandsToFinish()

robot.driveTrain.roadrunner.turn(-Math.PI / 2)
robot.waitForCommandsToFinish()

while (!opMode.stopRequested) {
    opMode.idle()
}
