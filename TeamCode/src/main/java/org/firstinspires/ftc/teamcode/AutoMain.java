package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.*;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.util.Heading;
import org.firstinspires.ftc.teamcode.util.Position;
import org.firstinspires.ftc.teamcode.util.Vector2;


public class AutoMain extends LinearOpMode {

    protected Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(1.7,2),new Heading(0),.7);
        robot.getSlide().moveToPosition(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveTargetDistance(new Vector2(inchesToTiles(3),0),.7);
        robot.getIntake().outake(.5,.5);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getSlide().moveToPosition(TRAVEL);
        robot.getDriveTrain().moveTargetDistance(new Vector2(inchesToTiles(-2),0),.7);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5), .7);
        robot.waitForCommandsToFinish();

        //get a new cone(loop*5)
        getNewCone();

        //deliver to pole(loop*5)
        deliverToPole(Pole.HIGH);

    }

    protected void initRobot() {
        robot = new Robot(this, true);
        robot.init();

        robot.getDriveTrain().setPosition(new Position(1.5,0.5));
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(.3,2.5),.7);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToPosition(INTAKE);
        robot.getIntake().intake(.5,.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),.7);
        robot.waitForCommandsToFinish();
    }

    private enum Pole{
        HIGH,
        MEDIUM,
        LOW,
        GROUND
    }

    protected void deliverToPole(Pole pole) {
        if(pole == Pole.HIGH) {
            robot.getSlide().moveToPosition(TOP_POLE);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(45),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.7,2.7),.7);
            robot.getTurret().moveToOrientation(FRONT);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),.7);
        }else if(pole == Pole.MEDIUM) {
            robot.getSlide().moveToPosition(MEDIUM_POLE);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(-45),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.7,2.3),.7);
            robot.getTurret().moveToOrientation(FRONT);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),.7);
        }else if(pole == Pole.LOW){
            robot.getSlide().moveToPosition(SMALL_POLE);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(-45),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(.7,2.7),.7);
            robot.getTurret().moveToOrientation(FRONT);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),.7);
        }else if(pole == Pole.GROUND){
            robot.getSlide().moveToPosition(GROUND_LEVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(45),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(.7,2.7),.7);
            robot.getTurret().moveToOrientation(FRONT);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),.7);
        }
        robot.waitForCommandsToFinish();
    }

}
