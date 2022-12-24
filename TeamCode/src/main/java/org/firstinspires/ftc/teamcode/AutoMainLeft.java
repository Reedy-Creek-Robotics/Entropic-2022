package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.*;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.util.Heading;
import org.firstinspires.ftc.teamcode.util.Position;
import org.firstinspires.ftc.teamcode.util.Vector2;

@Autonomous
public class AutoMainLeft extends LinearOpMode {

    protected Robot robot;

    public static final double BASE_SPEED =0.3;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2),new Heading(0),BASE_SPEED);
        //robot.getTurret().moveToOrientation(FRONT);
        //robot.getSlide().moveToHeight(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(17)+1,2),BASE_SPEED);
        robot.waitForCommandsToFinish();
        //robot.getIntake().outake(.5,.5);
        //robot.waitForCommandsToFinish();

        //recenter
        //robot.getSlide().moveToHeight(TRAVEL);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2),BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();

        //test
        getNewCone();
        deliverToPole(Pole.HIGH);

        getNewCone();
        deliverToPole(Pole.MEDIUM);

        getNewCone();
        deliverToPole(Pole.LOW);

        getNewCone();
        deliverToPole(Pole.GROUND);

        //get a new cone(loop*5)
        //getNewCone();

        //deliver to pole(loop*5)
        //deliverToPole(Pole.HIGH);

    }

    protected void initRobot() {
        robot = new Robot(this, true);
        robot.init();

        robot.getDriveTrain().setPosition(new Position(1.5,inchesToTiles(8)));
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(10),2.5),BASE_SPEED);
        //robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        //robot.getSlide().moveToHeight(INTAKE);
        //robot.getIntake().intake(.5,.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),BASE_SPEED);
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
            //robot.getSlide().moveToHeight(TOP_POLE);
            //robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1+inchesToTiles(19),2+inchesToTiles(18.5)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            //robot.getIntake().outake(.5,.5);
            //robot.waitForCommandsToFinish();

            //robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.MEDIUM) {
            //robot.getSlide().moveToHeight(MEDIUM_POLE);
            //robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(-45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1+inchesToTiles(19),2+ inchesToTiles(4)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            //robot.getIntake().outake(.5,.5);
            //robot.waitForCommandsToFinish();

            //robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.LOW){
            //robot.getSlide().moveToHeight(SMALL_POLE);
            //robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(-45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19),2+inchesToTiles(4)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            //robot.getIntake().outake(.5,.5);
            //robot.waitForCommandsToFinish();

            //robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.GROUND){
            //robot.getSlide().moveToHeight(GROUND_LEVEL);
            //robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19),2+inchesToTiles(18.5)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            //robot.getIntake().outake(.5,.5);
            //robot.waitForCommandsToFinish();

            //robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),BASE_SPEED);
        }
        robot.waitForCommandsToFinish();
    }

}
