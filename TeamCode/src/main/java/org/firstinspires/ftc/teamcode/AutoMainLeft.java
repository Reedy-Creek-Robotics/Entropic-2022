package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.*;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainLeft extends AutoMain {

    protected Robot robot;
    private int coneCount=5;
    public static final double BASE_SPEED =0.3;

    @Override
    protected Position getStartPosition() {
        return new Position(1.5,inchesToTiles(robotDescriptor.robotDimensionsInInches.height / 2));
    }

    @Override
    public void runAutoPath() {

        initRobot();

        waitForStart();

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2),new Heading(0),BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(17)+1,2),BASE_SPEED);
        robot.waitForCommandsToFinish();
        robot.getIntake().outake(.5,.5);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getSlide().moveToHeight(TRAVEL);
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

        park();

        //get a new cone(loop*5)
        //getNewCone();

        //deliver to pole(loop*5)
        //deliverToPole(Pole.HIGH);
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(10),2.5),BASE_SPEED);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(.5,.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

    protected void deliverToPole(Pole pole) {
        if(pole == Pole.HIGH) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TOP_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1+inchesToTiles(19),2+inchesToTiles(18.5)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(-45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1+inchesToTiles(19),2+ inchesToTiles(4)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.LOW){
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(-45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19),2+inchesToTiles(4)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),BASE_SPEED);
        }else if(pole == Pole.GROUND){
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(45),BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19),2+inchesToTiles(18.5)),BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5,.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5,2.5),new Heading(0),BASE_SPEED);
        }
        robot.waitForCommandsToFinish();
    }

    private void park() {
        robot.getDriveTrain().moveToTargetPosition(new Position(.5 + getAprilTagPosition(), 1.5),BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

}
