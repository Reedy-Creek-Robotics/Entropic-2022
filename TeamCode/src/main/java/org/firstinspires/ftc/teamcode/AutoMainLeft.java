package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainLeft extends AutoMain {

    @Override
    protected Position getStartPosition() {
        return new Position(
                1 + inchesToTiles(robotDescriptor.robotDimensionsInInches.width / 2),
                inchesToTiles(robotDescriptor.robotDimensionsInInches.height / 2)
        );
    }

    @Override
    public void runAutoPath() {

        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, .5), BASE_SPEED);

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2), new Heading(0), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        // Wait until we get an observation to make sure we are where we expect

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(1.55, 2), BASE_SPEED);
        robot.waitForCommandsToFinish();
        robot.getIntake().outtake(2);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), BASE_SPEED);
        //robot.getDriveTrain().waitForTileEdgeDetection(2.0);
        robot.waitForCommandsToFinish(0.5);
        robot.getSlide().moveToHeight(TRAVEL);
        robot.waitForCommandsToFinish();

        //robot.getDriveTrain().setPosition(new Position(1.5, 1.5));


        /*
        //test
        getNewCone();
        deliverToPole(Pole.HIGH);

        getNewCone();
        deliverToPole(Pole.MEDIUM);

        getNewCone();
        deliverToPole(Pole.LOW);

        getNewCone();
        deliverToPole(Pole.GROUND);*/


        //get a new cone(loop*5)
        //getNewCone();

        //deliver to pole(loop*5)
        //deliverToPole(Pole.HIGH);
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(10), 2.5), BASE_SPEED);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(0.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

    protected void deliverToPole(Pole pole) {
        if (pole == Pole.HIGH) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TOP_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1 + inchesToTiles(19), 2 + inchesToTiles(18.5)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(0), BASE_SPEED);
        } else if (pole == Pole.MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(-45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1 + inchesToTiles(19), 2 + inchesToTiles(4)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(0), BASE_SPEED);
        } else if (pole == Pole.LOW) {
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(-45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19), 2 + inchesToTiles(4)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(0), BASE_SPEED);
        } else if (pole == Pole.GROUND) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19), 2 + inchesToTiles(18.5)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(0), BASE_SPEED);
        }
        robot.waitForCommandsToFinish();
    }

    private void park() {
        // April tag position is
        robot.getDriveTrain().moveToTargetPosition(new Position(.5 + (getAprilTagPosition() - 1), 1.5), BASE_SPEED);
        robot.getSlide().moveToHeight(INTAKE);
        robot.waitForCommandsToFinish();
    }

}
