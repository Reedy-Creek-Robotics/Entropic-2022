package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public abstract class AutoMain extends LinearOpMode {

    protected Robot robot;
    protected RobotDescriptor robotDescriptor;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

    }

    protected void initRobot() {
        robot = new Robot(this);
        robot.init();
        robotDescriptor = robot.getRobotContext().robotDescriptor;

        robot.getDriveTrain().setPosition(getStartPosition());
    }

    protected abstract Position getStartPosition();

    public enum Pole {
        HIGH,
        MEDIUM,
        LOW,
        GROUND
    }

    public enum PoleStackOffset {
        FIRST_CONE(0),
        SECOND_CONE(20),
        THIRD_CONE(40),
        FOURTH_CONE(60),
        FIFTH_CONE(80);

        int ticks;

        PoleStackOffset(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }

        public static PoleStackOffset forConesRemaining(int conesRemaining) {
            if (conesRemaining < 1 || conesRemaining > 5) {
                throw new IllegalArgumentException(String.valueOf(conesRemaining));
            }
            return PoleStackOffset.values()[conesRemaining - 1];
        }
    }

}
