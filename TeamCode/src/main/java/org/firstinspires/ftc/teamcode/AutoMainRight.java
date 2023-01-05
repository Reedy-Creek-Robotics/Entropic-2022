package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.AutoMain.Pole.*;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TOP_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainRight extends AutoMain {

    protected Robot robot;
    private int coneCount=5;
    public static final double BASE_SPEED = 0.3;

    @Override
    protected Position getStartPosition() {
        return new Position(4.5, inchesToTiles(robotDescriptor.robotDimensionsInInches.height / 2));
    }

    @Override
    public void runAutoPath() {

        initRobot();

        waitForStart();

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2), new Heading(180), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(4.3, 2), BASE_SPEED);
        robot.waitForCommandsToFinish();
        robot.getIntake().outake(.5, .5);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getSlide().moveToHeight(TRAVEL);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();

        //test
        getNewCone();
        deliverToPole(HIGH);

        getNewCone();
        deliverToPole(MEDIUM);

        getNewCone();
        deliverToPole(LOW);

        getNewCone();
        deliverToPole(GROUND);

        park();


        //get a new cone(loop*5)
        //getNewCone();
        //deliver to pole(loop*5)
        //deliverToPole(Pole.HIGH);
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(5.7, 2.5), BASE_SPEED);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(.5, .5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

    protected void deliverToPole(Pole pole) {
        if (pole == HIGH) {
            robot.getSlide().moveToHeight(TOP_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(135), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3, 2.7), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5, .5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(225), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3, 2.3), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5, .5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == LOW) {
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(225), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3, 2.3), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5, .5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == GROUND) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(135), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3, 2.7), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outake(.5, .5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(180), BASE_SPEED);
        }
        robot.waitForCommandsToFinish();
    }

    private void park() {
        robot.getDriveTrain().moveToTargetPosition(new Position(3.5 + getAprilTagPosition(), 1.5), BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

}
