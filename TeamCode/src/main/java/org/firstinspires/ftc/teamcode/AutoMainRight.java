package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainRight extends LinearOpMode {

    protected Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        waitForStart();

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2),new Heading(180),.7);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(4.3,2),.7);
        robot.waitForCommandsToFinish();
        robot.getIntake().outake(.5,.5);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getSlide().moveToHeight(TRAVEL);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2),.7);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2.5), .7);
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
        robot = new Robot(this);
        robot.init();

        robot.getDriveTrain().setPosition(new Position(4.5,.5));
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(5.7,2.5),.7);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToHeight(INTAKE);
        robot.getIntake().intake(.5,.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(5.5,2.5),.7);
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
            robot.getSlide().moveToHeight(TOP_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2.5),new Heading(135),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3,2.7),.7);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2.5),new Heading(180),.7);
        }else if(pole == Pole.MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2.5),new Heading(225),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3,2.3),.7);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5,2.5),new Heading(180),.7);
        }else if(pole == Pole.LOW){
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5,2.5),new Heading(225),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3,2.3),.7);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5,2.5),new Heading(180),.7);
        }else if(pole == Pole.GROUND){
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5,2.5),new Heading(135),.7);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3,2.7),.7);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5,2.5),new Heading(180),.7);
        }
        robot.waitForCommandsToFinish();
    }

}
