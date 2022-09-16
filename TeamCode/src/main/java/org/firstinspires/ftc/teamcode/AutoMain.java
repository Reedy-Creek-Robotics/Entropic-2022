package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Point;


public abstract class AutoMain extends LinearOpMode {

    public static final int PARALLEL_TO_TILE_EDGE = 0;
    protected Robot robot;
    protected double fieldSide;

    enum StartPosition {

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

    }

    protected void initRobot() {
        robot = new Robot(this, true);
        robot.init();
    }

}
